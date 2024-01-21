package src;
import java.util.List;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modele.bd.LikeBd;
import modele.bd.MessageBd;
import modele.code.Message;

/**
 * La classe PagePrincipale représente la page principale de l'application. Elle affiche une interface utilisateur
 * permettant à l'utilisateur de voir les messages de ses amis et d'envoyer des messages.
 */
public class PagePrincipale {
    private Stage stage;
    private static Client client;
    private static VBox listeNonAmis = new VBox();
    private static VBox Amis = new VBox();
    private static  VBox messageArea = new VBox();
    private ScrollPane scrollPane = new ScrollPane();

    /**
     * Constructeur de la classe PagePrincipale.
     *
     * @param stage Le stage JavaFX sur lequel afficher la page.
     * @param client Le client connecté.
     */
    public PagePrincipale(Stage stage,Client client) {
        this.stage = stage;
        this.client=client;
        stage.setTitle("Page principale");
        listeNonAmis.getChildren().addAll(new Label("Non amis"));
        Amis.getChildren().addAll(new Label("amis"));
    }

    /**
     * Crée un affichage pour un ami.
     *
     * @param friendName Le nom de l'ami.
     * @param client Le client connecté.
     * @param isAmi Si l'ami est un ami ou non.
     * @return Un affichage pour un ami.
     */
    private static HBox createFriendDisplay(String friendName, Client client, boolean isAmi) {
            HBox interieur = new HBox();
            Label label = new Label(friendName);
            Button button = new Button(isAmi ? "Supprimer" : "Ajouter");

            button.setOnAction(e -> {
                try {
                    handleFriendAction(friendName, client, isAmi);
                    updateFriendDisplay(interieur, friendName, client, !isAmi);
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            });

            interieur.getChildren().addAll(label, button);
            return interieur;
        }

    /**
     * Gère l'action d'ajout ou de suppression d'un ami.
     * @param friendName Le nom de l'ami.
     * @param client Le client connecté.
     * @param isAmi Si l'ami est un ami ou non.
     * @throws ClassNotFoundException En cas d'erreur lors de l'accès à la base de données.
     */
    private static void handleFriendAction(String friendName, Client client, boolean isAmi) throws ClassNotFoundException {
        if (isAmi) {
            client.supprimeAmis(friendName);
        } else {
            client.Suivre(friendName);
        }
    }
    
    /**
     * Met à jour l'affichage d'un ami.
     * @param container Le conteneur de l'affichage.
     * @param friendName Le nom de l'ami.
     * @param client Le client connecté.
     * @param isAmi Si l'ami est un ami ou non.
     * @throws ClassNotFoundException En cas d'erreur lors de l'accès à la base de données.
     */
    private static void updateFriendDisplay(HBox container, String friendName, Client client, boolean isAmi) {
        container.getChildren().clear();
        Label label = new Label(friendName);
        Button button = new Button();
        
        if (isAmi) {
            button.setText("Supprimer");
            button.setOnAction(e -> {
                try {
                    handleFriendAction(friendName, client, true);
                    updateFriendDisplay(container, friendName, client, false);
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            });
            Amis.getChildren().add(container);
            listeNonAmis.getChildren().remove(container);
        } else {
            button.setText("Ajouter");
            button.setOnAction(e -> {
                try {
                    handleFriendAction(friendName, client, false);
                    updateFriendDisplay(container, friendName, client, true);
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            });
            listeNonAmis.getChildren().add(container);
            Amis.getChildren().remove(container);
        }

        container.getChildren().addAll(label, button);
    }
    
    /**
     * Affiche la page principale.
     * @throws ClassNotFoundException En cas d'erreur lors de l'accès à la base de données.
     */
    public void show() throws ClassNotFoundException {
        BorderPane borderPane = new BorderPane();
        // liste non amis
        // Affichage des non-amis
        Platform.runLater(() -> {
            for (String string : client.getNonSuivi()) {
                HBox interieur = createFriendDisplay(string, client, false);
                listeNonAmis.getChildren().add(interieur);
            }
        });
        
        borderPane.setLeft(listeNonAmis);

        Platform.runLater(() -> {
            for (String string : client.getRecevoirMessage()) {
                HBox interieur = createFriendDisplay(string, client, true);
                Amis.getChildren().add(interieur);
            }
        });
        borderPane.setRight(Amis);


        // Panel en bas pour écrire des messages
        TextField messageInput = new TextField();
        Button sendButton = new Button("Envoyer");
        sendButton.setOnAction(e -> {
            String message = messageInput.getText();
            if (message.isEmpty() || message.equals("") || message.isBlank()) {
                return;
            }
            client.sendMessage(message);
            messageInput.clear();
        });
        HBox messageBox = new HBox(messageInput, sendButton);
        messageBox.setSpacing(10);
        messageBox.setPadding(new Insets(10));
        borderPane.setBottom(messageBox);

        // Panel au centre avec la zone des messages

        scrollPane.setContent(messageArea);
        borderPane.setCenter(scrollPane);

        Scene scene = new Scene(borderPane, 850, 600);
        stage.setScene(scene);
        stage.show();
    }
    

    /**
     * Affiche un message dans la zone de messages.
     * @param message Le message à afficher.
     */
    public static void afficheMessage(String message) {
        // Déclarez un tableau pour stocker la valeur de nbLike
        int[] nbLikeArray = new int[2];
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (message.contains("///like")) {
                    MettreAJourAffichage(message);
                    
                }
                else{
                    //====================
                    // récuperer les valeurs
                    String date="";
                    String pseudo="";
                    String contenu="";
                    String[] parts = message.split("\\|\\|\\|");
                    
                    if (parts.length <= 3) {
                        date = parts[0].trim();
                        pseudo = parts[1].trim();
                        contenu = "";
                        for (int i = 2; i < parts.length; i++) {
                            contenu = parts[i].trim();
                        }
                    }
                    Message m = null;
                    int userLikeMessage = 0;
                    int userDislikeMessage = 0;
                    try {
                        m = MessageBd.recupererMessage(date, pseudo, contenu);
                        if (m != null) {
                            nbLikeArray[0] = LikeBd.countLikeToMessage(m.getIdMessage());
                            nbLikeArray[1] = LikeBd.countDisikeToMessage(m.getIdMessage());
                            userLikeMessage = LikeBd.countLikeToMessageByUser(m.getIdMessage(), client.getIdUser());
                            userDislikeMessage = LikeBd.countDislikeToMessageByUser(m.getIdMessage(), client.getIdUser());
                        }

        
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    
    
                    HBox hbox = new HBox();
                    TextArea marea = new TextArea();
                    marea.setText("Date : " + date + "\n" + "Pseudo : " + pseudo + "\n" + contenu + "\n" + "Nombre de like : " + nbLikeArray[0] + "\n" + "Nombre de dislike : " + nbLikeArray[1]);
                    
                    marea.setEditable(false);
                    Button button = new Button("Like");
                    Button button2 = new Button("Dislike");
                    final String finalContenu = contenu;
                    final Message finalM = m;
                    final String finalPseudo = pseudo;
                    final String finalDate = date;
                    button.setOnAction(e -> {
                        try {
                            client.like(finalDate,finalPseudo, finalContenu);
                            button.setDisable(true);
    
                            // Récupérez le nombre de likes mis à jour après le "Like"
                            nbLikeArray[0] = LikeBd.countLikeToMessage(finalM.getIdMessage());
                            marea.setText("Date : " + finalDate + "\n" + "Pseudo : " + finalPseudo + "\n" + finalContenu + "\n" + "Nombre de like : " + nbLikeArray[0]);
                            // Récupérez le nombre de likes mis à jour après le "Like"
                        } catch (ClassNotFoundException e1) {
                            e1.printStackTrace();
                        }
                    });
                    button2.setOnAction(e -> {
                        try {
                            client.dislike(finalDate,finalPseudo, finalContenu);
                            button2.setDisable(true);
    
                            // Récupérez le nombre de likes mis à jour après le "Like"
                            nbLikeArray[1] = LikeBd.countDisikeToMessage(finalM.getIdMessage());
                            marea.setText("Date : " + finalDate + "\n" + "Pseudo : " + finalPseudo + "\n" + finalContenu + "\n" + "Nombre de dislike : " + nbLikeArray[1]);
                            // Récupérez le nombre de likes mis à jour après le "Like"
                        } catch (ClassNotFoundException e1) {
                            e1.printStackTrace();
                        }
                    });
                    Button button3 = null;
                    if (client.getPseudoClient().equals(pseudo)) {
                        button3 = new Button("Supprimer");
                        button3.setOnAction(e -> {
                            try {
                                System.out.println("Supprimer");
                                client.supprimerMessage(finalDate,finalPseudo, finalContenu);
                                
                            } catch (ClassNotFoundException e1) {
                                e1.printStackTrace();
                            }
                        });
                    }
                    if (userLikeMessage != 0) {
                        button.setDisable(true);
                    }
                    hbox.getChildren().addAll(marea, button, button2);
                    if (button3 != null) {
                        hbox.getChildren().add(button3);
                    }
                    messageArea.getChildren().add(0, hbox);

                }
            }
        });
    }



    /**
     * Met à jour l'affichage d'un message.
     * @param message Le message à mettre à jour.
     */
    public static void MettreAJourAffichage(String message){
        String[] partsLikes = message.split("\\|\\|\\|"); // partsLikes[0] = "///like", partsLikes[1] = date, partsLikes[2] = pseudo, partsLikes[3] = contenu
        String date = partsLikes[1].trim();
        String pseudo = partsLikes[2].trim();
        String contenu = "";
        for (int i = 3; i < partsLikes.length; i++) {
            contenu = partsLikes[i].trim();
        }       
        for (Node node : messageArea.getChildren()) { // Parcourir tous les noeuds dans messageArea
            if (node instanceof HBox) { // Si le noeud est un HBox
                HBox hbox = (HBox) node;
                for (Node child : hbox.getChildren()) { //  Parcourir tous les noeuds dans hbox
                    if (child instanceof TextArea) { // Si le noeud est un TextArea
                        TextArea marea = (TextArea) child;
                        if (marea.getText().contains(date) && marea.getText().contains(pseudo)) { // Si le contenu de marea contient la date et le pseudo
                            try {
                                marea.setText("Date : " + date + "\n" + "Pseudo : " + pseudo + "\n" + contenu + "\n" + "Nombre de like : " + LikeBd.countLikeToMessage(MessageBd.recupererMessage(date, pseudo, contenu).getIdMessage())+"\n"+"Nombre de dislike : "+LikeBd.countDisikeToMessage(MessageBd.recupererMessage(date, pseudo, contenu).getIdMessage())+"\n");
                                // Récupérez le nombre de likes mis à jour après le "Like"
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * Supprime un message et met à jour l'affichage.
     * @param Message Le message à supprimer.
     */
    public static void supprimerMessageEtMettreAjourAffichage(String Message){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String[] partsMessage=Message.split("\\|\\|\\|");
                String date=partsMessage[1].trim();
                String pseudo=partsMessage[2].trim();
                HBox hboxASupprimer=null;
                for (Node node : messageArea.getChildren()) { // Parcourir tous les noeuds dans messageArea
                    if (node instanceof HBox) { // Si le noeud est un HBox
                        HBox hbox = (HBox) node;
                        for (Node child : hbox.getChildren()) { //  Parcourir tous les noeuds dans hbox
                            if (child instanceof TextArea) { // Si le noeud est un TextArea
                                TextArea marea = (TextArea) child;
                                if (marea.getText().contains(date) && marea.getText().contains(pseudo)) { // Si le contenu de marea contient la date et le pseudo
                                    hboxASupprimer=hbox;
                                }
                            }
                        }
                    }
                }
                if (hboxASupprimer!=null){
                    messageArea.getChildren().remove(hboxASupprimer);
                }
            }
        });
        
    }

    /**
     * Affiche une pop-up de bannissement.
     * 
     */
    public static void afficherPopUpBannissement() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Bannissement");
            alert.setHeaderText(null);
            alert.setContentText("Vous avez été banni. La connexion sera fermée.");
            alert.showAndWait();
    
            // Fermer l'application après la confirmation
            Platform.exit();
            
            System.exit(0);
        });
    }

    /**
     * Met à jour la liste des personnes non suivies.
     * @param message Le message contenant la liste des personnes non suivies.
     */
    public static void MettreAjourListeNonSuivi(List<String> listeNonSuivi, List<String> listeAmis) {
        Platform.runLater(() -> {
            listeNonAmis.getChildren().clear();
            listeNonAmis.getChildren().addAll(new Label("Non amis"));
            for (String utilisateur : listeNonSuivi) {
                HBox interieur = createFriendDisplay(utilisateur, client, false);
                System.out.println("utilisateur : " + utilisateur);
                listeNonAmis.getChildren().add(interieur);
            }
            Amis.getChildren().clear();
            Amis.getChildren().addAll(new Label("amis"));
            for (String utilisateur : listeAmis) {
                HBox interieur = createFriendDisplay(utilisateur, client, true);
                Amis.getChildren().add(interieur);
            }
        });
    }
    
}
