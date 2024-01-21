package src;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import modele.bd.AmisBd;
import modele.bd.LikeBd;
import modele.bd.MessageBd;
import modele.bd.UtilisateurBd;
import modele.code.Message;

/**
 * La classe Client gère les connexions des clients.
 */
public class Client {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 5555;

    private static List<String> EnvoyerMassage = new ArrayList<>();
    private static List<String> RecevoirMessage= new ArrayList<>();
    private static List<String> NonSuivi= new ArrayList<>();
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String pseudoClient;
    private boolean nouveau;

    /**
     * Initialise une nouvelle instance de la classe Client avec le pseudo spécifié.
     *
     * @param pseudo Le pseudo du client.
     * @throws ClassNotFoundException En cas d'erreur lors de l'accès à la base de données.
     */
    public Client(String pseudo,boolean nouveau) throws ClassNotFoundException {
        this.pseudoClient = pseudo;
        this.nouveau=nouveau;
        List<modele.code.Utilisateur> liste;
                try {
                    liste = AmisBd.RecevoirMessage(pseudo);
                    for (modele.code.Utilisateur utilisateur : liste) {
                        System.out.println("amis : "+utilisateur.getPseudo());
                        RecevoirMessage.add(utilisateur.getPseudo());
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                List<modele.code.Utilisateur> liste2;
                try {
                    liste2 = AmisBd.EnvoyerMessage(pseudo);
                    for (modele.code.Utilisateur utilisateur : liste2) {
                        EnvoyerMassage.add(utilisateur.getPseudo());
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                List<modele.code.Utilisateur> liste3;
                try {
                    liste3 = AmisBd.NonSuivi(pseudo);
                    for (modele.code.Utilisateur utilisateur : liste3) {
                        NonSuivi.add(utilisateur.getPseudo());
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
        for (Message m:MessageBd.recupererLesMessageDeTousSesAmisDansOrdreDate(pseudo)){
            String s = m.getDate()+"|||"+m.getPseudo()+"|||"+m.getContenu();
            PagePrincipale.afficheMessage(s);
        }

        try{
            this.socket = new Socket(SERVER_IP, SERVER_PORT);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Lance le client en établissant une connexion avec le serveur et en gérant les messages en arrière-plan.
     */
    public void lancement() {

                System.out.println("Connexion au serveur réussie.");

                // Lire et afficher les messages du serveur en arrière-plan
                new Thread(() -> {
                    try {
                        String serverMessage;
                        while ((serverMessage = this.in.readLine()) != null) {
                            if (serverMessage.equals("Votre compte a été supprimé. La connexion sera fermée.")){
                                PagePrincipale.afficherPopUpBannissement();
                            }
                            else if (serverMessage.contains("///likeDislike")){ // permet de mettre a jour le nombre de like et dislike du message concerné
                                PagePrincipale.afficheMessage(serverMessage);
                            }
                            else if (serverMessage.contains("///SUPPRIMER")){ // permet de supprimer le message concerné
                                PagePrincipale.supprimerMessageEtMettreAjourAffichage(serverMessage);
                            }
                            else if (serverMessage.contains("///nouveau")){ // permet de mettre a jour la liste des personnes non suivies
                                Platform.runLater(() -> {
                                    List<modele.code.Utilisateur> liste3;
                                    try {
                                        liste3 = AmisBd.NonSuivi(pseudoClient);
                                        NonSuivi.clear();
                                        for (modele.code.Utilisateur utilisateur : liste3) {
                                            System.out.println("non suivi : "+utilisateur.getPseudo());
                                            NonSuivi.add(utilisateur.getPseudo());
                                        }
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    }

                                    List<modele.code.Utilisateur> liste;
                                    try {
                                        liste = AmisBd.RecevoirMessage(pseudoClient);
                                        RecevoirMessage.clear();
                                        for (modele.code.Utilisateur utilisateur : liste) {
                                            System.out.println("amis : "+utilisateur.getPseudo());
                                            RecevoirMessage.add(utilisateur.getPseudo());
                                        }
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    PagePrincipale.MettreAjourListeNonSuivi(NonSuivi,RecevoirMessage);
                                });
                               
                            }
                            else{
                                for (String s:RecevoirMessage){ //affiche les messages des amis
                                    if (serverMessage.contains(s)){
                                        PagePrincipale.afficheMessage(serverMessage);
                                    }
                                }
                                if (serverMessage.contains(pseudoClient)){
                                    PagePrincipale.afficheMessage(serverMessage);
                                }
                            }
                            
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();


                // Envoyer des messages depuis le terminal
                out.println(this.pseudoClient);
                System.out.println("Connecté avec le pseudo: " + this.pseudoClient);
                
                if (nouveau){
                    sendMessage("/nouveau");
                }
    }


    /**
     * Envoie un message au serveur.
     *
     * @param message Le message à envoyer.
     */
    public void sendMessage(String message) {
        this.out.println(message); // envoie le message au serveur
    }


    /**
     * Permet de suivre un autre utilisateur.
     *
     * @param pseudo Le pseudo de l'utilisateur à suivre.
     * @throws ClassNotFoundException En cas d'erreur lors de l'accès à la base de données.
     */
    public void Suivre(String pseudo) throws ClassNotFoundException{
        RecevoirMessage.add(pseudo);  // ajoute le pseudo a la liste des personnes suivies
        AmisBd.ajouteAmis(this.pseudoClient, pseudo);   // ajoute le pseudo a la base de données
        sendMessage("/follow "+pseudo); // envoie le message au serveur pour qu'il le traite
        if (NonSuivi.contains(pseudo)){
            NonSuivi.remove(pseudo);    // retire le pseudo de la liste des personnes non suivies
        }

    }

    /**
     * Supprime un ami de la liste d'amis.
     *
     * @param pseudo Le pseudo de l'ami à supprimer.
     * @throws ClassNotFoundException En cas d'erreur lors de l'accès à la base de données.
     */
    public void supprimeAmis(String pseudo) throws ClassNotFoundException{
        RecevoirMessage.remove(pseudo);
        AmisBd.supprimeAmis(this.pseudoClient, pseudo);
        System.out.println("supprime amis : "+pseudo);
        sendMessage("/unfollow "+pseudo);
        if (!NonSuivi.contains(pseudo)){
            NonSuivi.add(pseudo);
        }
    }

    /**
     * Récupère la liste des utilisateurs qui envoient des messages.
     *
     * @return La liste des utilisateurs qui envoient des messages.
     */
    public List<String> getRecevoirMessage(){
        return RecevoirMessage;
    }

    public List<String> getNonSuivi(){
        return NonSuivi;
    }

    /**
     * Envoie un "J'aime" à un message spécifié.
     *
     * @param date La date du message.
     * @param pseudo Le pseudo de l'auteur du message.
     * @param contenu Le contenu du message.
     * @throws ClassNotFoundException En cas d'erreur lors de l'accès à la base de données.
     */
    public void like(String date,String pseudo, String contenu) throws ClassNotFoundException{
        Message m=MessageBd.recupererMessage(date,pseudo,contenu);
        LikeBd.ajouteLike(m.getIdMessage(), this.pseudoClient);
        int compteur=LikeBd.countLikeToMessage(m.getIdMessage());
        sendMessage("/likeDislike "+m.getIdMessage()+" "+compteur);
    }

    /**
     * Récupère l'ID de l'utilisateur.
     *
     * @return L'ID de l'utilisateur.
     * @throws ClassNotFoundException En cas d'erreur lors de l'accès à la base de données.
     */
    public int getIdUser() throws ClassNotFoundException{
        return UtilisateurBd.recupererUtilisateur(pseudoClient).getId();
    
    }

    /**
     * Envoie un "Je n'aime pas" à un message spécifié.
     *
     * @param date La date du message.
     * @param pseudo Le pseudo de l'auteur du message.
     */
    public void dislike(String date,String pseudo, String contenu) throws ClassNotFoundException{
        Message m=MessageBd.recupererMessage(date,pseudo,contenu);
        LikeBd.ajouteDislike(m.getIdMessage(), this.pseudoClient);
        int compteur=LikeBd.countDisikeToMessage(m.getIdMessage());
        sendMessage("/likeDislike "+m.getIdMessage()+" "+compteur);
    }

    /**
     * Récupère le pseudo de l'utilisateur.
     *
     * @return Le pseudo de l'utilisateur.
     */
    public String getPseudoClient() {
        return pseudoClient;
    }

    /**
     * Permet de suivre un autre utilisateur.
     *
     * @param recipientMessage Le pseudo de l'utilisateur à suivre.
     * @throws ClassNotFoundException En cas d'erreur lors de l'accès à la base de données.
     */
    public void supprimerMessage(String date,String pseudo, String contenu) throws ClassNotFoundException{
        System.out.println("supprimer message");
        Message m=MessageBd.recupererMessage(date,pseudo,contenu);
        sendMessage("/supprimerMessage "+m.getIdMessage());
    }
}