package src;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import modele.bd.AmisBd;
import modele.bd.MessageBd;
import modele.bd.UtilisateurBd;
import modele.code.Utilisateur;
import modele.code.Message;

/**
 * La classe ClientHandler gère les connexions des clients.
 */
public class ClientHandler extends Thread{
    private Socket clientSocket;
    private BufferedReader lire;
    private PrintWriter ecrire;
    private String pseudoClient;
    private Map<String, PrintWriter> clients;
    private static List<String> EnvoyerMassage = new ArrayList<>();
    private static List<String> RecevoirMessage= new ArrayList<>();
    private static List<String> NonSuivi= new ArrayList<>();

    /**
     * Initialise un nouveau gestionnaire de client.
     *
     * @param socket  Le socket du client.
     * @param clients La liste des clients connectés.
     */
    public ClientHandler(Socket socket,Map<String, PrintWriter> clients) {
        this.clientSocket = socket;
        this.clients = clients;

        try{
            this.lire = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));  // lire les messages du client
            this.ecrire = new PrintWriter(clientSocket.getOutputStream(), true);    // Envoyer des messages au client
            this.pseudoClient = this.lire.readLine();      // Lire le pseudoClient du client
            clients.put(pseudoClient, this.ecrire);   // Enregistrer le pseudoClient et le flux de sortie du client
            try {
                for (Utilisateur u:AmisBd.RecevoirMessage(pseudoClient)){
                    RecevoirMessage.add(u.getPseudo());
                }
            } 
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                for (Utilisateur u:AmisBd.EnvoyerMessage(pseudoClient)){
                    EnvoyerMassage.add(u.getPseudo());
                }
            } 
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                for (Utilisateur u:AmisBd.NonSuivi(pseudoClient)){
                    NonSuivi.add(u.getPseudo());
                }
            } 
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            System.out.println(pseudoClient + " est connecté.");

        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Exécute le thread du gestionnaire de client.
     */
    @Override
    public void run() {
        try {
            this.lire = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));  // lire les messages du client
            this.ecrire = new PrintWriter(clientSocket.getOutputStream(), true);    // Envoyer des messages au client

            // Demander et enregistrer le pseudo du client
             
            // lire les messages du client et diffuser à tous les autres clients
            String message;
            while ((message = lire.readLine()) != null) {
                System.out.println(message);
                this.broadcast(message);
            }
        } 
        
        catch (IOException e) {
            e.printStackTrace();
        }
        
        finally {
            // Retirer le pseudoClient et le flux de sortie du client de la liste des clients
            clients.remove(this.pseudoClient);
            System.out.println(this.pseudoClient + " est déconnecté.");
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Diffuse un message à tous les clients connectés.
     *
     * @param message Le message à diffuser.
     */
    private void broadcast(String message) {

        System.out.println("message : "+message);
        if (message.equals("Votre compte a été supprimé. La connexion sera fermée.")){
            // si le message est supprimer le compte alors le message est envoyer au client et le client est deconnecter
            this.ecrire.println(message);
            System.out.println("pseudo : "+pseudoClient);
        }
        if (message.contains("/")){
            // permet de gerer les evenement de like dislike follow unfollow
            System.out.println("hahahaha : "+message);
            String[] recipientMessage = message.split(" ");
            if (recipientMessage[0].equals("/follow")) {
                follow(recipientMessage);
            }

            else if (recipientMessage[0].equals("/unfollow")){
                unfollow(recipientMessage);
            }
            else if(recipientMessage[0].equals("/likeDislike")){
                likerDislike(recipientMessage);
            }
            else if(recipientMessage[0].equals("/supprimerMessage")){
                supprimerMessage(recipientMessage);
            }
            else if (recipientMessage[0].equals("/nouveau")){
                for (String ps:clients.keySet()){
                    PrintWriter recipientWriter = clients.get(ps);
                    if (recipientWriter != null){
                        recipientWriter.println("///nouveau");
                    }
                    
                }
            }
        }
        else{
            // permet d'envoyer les messages
            List<String> utilisateurs = new ArrayList<>();

            try{
                MessageBd.ajouteMessage(pseudoClient, message);
            }
            catch (ClassNotFoundException e){
                e.printStackTrace();
            }
            try {
                for (Utilisateur u:UtilisateurBd.AllUtilisateur())
                {
                    utilisateurs.add(u.getPseudo());
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            for (String pseudo : utilisateurs) {
                PrintWriter recipientWriter = clients.get(pseudo);
                if (recipientWriter != null){
                    System.out.println("pseudo : "+recipientWriter);
                    LocalDateTime now = LocalDateTime.now();

                    // pour le format de la date
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                    // Formate la date en chaîne de caractères selon le format spécifié
                    String formattedDateTime = now.format(formatter);

                    String messageWithDate = formattedDateTime +"|||" + pseudoClient + "|||" + message;
                    recipientWriter.println(messageWithDate);
                }
            }
        }
    }

    /**
     * Permet de follow un client et de recevoir ses messages.
     *
     * @param message Le message à envoyer.
     */
    private void follow(String[] recipientMessage){
        for (String pseudo : clients.keySet()) {
            if (pseudo.equals(recipientMessage[1])) {
                RecevoirMessage.add(recipientMessage[1]);
                System.out.println(RecevoirMessage);
            }
        }
        
        return;
    }

    /**
     * Permet de unfollow un client et de ne plus recevoir ses messages.
     *
     * @param message Le message à envoyer.
     */
    private void unfollow(String[] recipientMessage){
        for (String pseudo : clients.keySet()) {
            if (pseudo.equals(recipientMessage[1])) {
                RecevoirMessage.remove(recipientMessage[1]);
            }
        }
        return;
    }

    /**
     * Permet de liker ou disliker un message.
     *
     * @param message Le message à envoyer.
     */
    private void likerDislike(String[] recipientMessage) {
        String idMessage = recipientMessage[1];
        int id = Integer.parseInt(idMessage);
        Message message=null;
        try {
            message = MessageBd.recupererMessageById(id);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("t"+message.getDate());
        String messageWithDate = "///likeDislike"+"|||"+message.getDate() +"|||" + message.getPseudo() + "|||" + message.getContenu();
        
        envoieMessageAllUser(messageWithDate);
    }



    public void supprimerMessage(String[] recipientMessage){
        String idMessage = recipientMessage[1];
        int id = Integer.parseInt(idMessage);
        Message message=null;
        System.out.println("id : "+id);
        try {
            message = MessageBd.recupererMessageById(id);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String messageWithDate = "///SUPPRIMER"+"|||"+message.getDate() +"|||" + message.getPseudo() + "|||" + message.getContenu();
        try {
            MessageBd.supprimerMessage(id);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        envoieMessageAllUser(messageWithDate);
    }




    
    /**
     * Permet d'envoyer un message à tous les clients connectés.
     *
     * @param message Le message à envoyer.
     */
    private void envoieMessageAllUser(String message) {
        List<String> utilisateurs = new ArrayList<>();
        try {
            for (Utilisateur u:UtilisateurBd.AllUtilisateur()) 
            // tous les client de la bd
            {
                System.out.println("pseudo : "+u.getPseudo());
                utilisateurs.add(u.getPseudo());

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        for (String pseudo : utilisateurs) {
            PrintWriter recipientWriter = clients.get(pseudo);
            if (recipientWriter != null){  // on regarde si il est connecter
                recipientWriter.println(message);
            }
        }
    }

}