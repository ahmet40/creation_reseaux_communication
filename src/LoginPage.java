package src;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import modele.bd.UtilisateurBd;

/**
 * La classe LoginPage représente la page de connexion de l'application. Elle affiche une interface utilisateur
 * permettant à l'utilisateur de saisir son nom d'utilisateur et son mot de passe pour se connecter à la base de données.
 */
public class LoginPage {
    private Stage stage;

    /**
     * Constructeur de la classe LoginPage.
     *
     * @param stage Le stage JavaFX sur lequel afficher la page.
     */
    public LoginPage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Affiche la page de connexion.
     */
    public void show() {
        stage.setTitle("Connexion à la base de données");

        // Création des composants
        Label labelUsername = new Label("Nom d'utilisateur:");
        TextField textFieldUsername = new TextField();

        Label labelPassword = new Label("Mot de passe:");
        PasswordField passwordField = new PasswordField();

        Button buttonConnect = new Button("Se connecter");
        buttonConnect.setOnAction(e -> handleButtonConnect(textFieldUsername.getText(), passwordField.getText()));
        Button buttonCreateAccount = new Button("Créer un compte");
        buttonCreateAccount.setOnAction(e -> openCreateAccountPage());

        // Mise en page avec GridPane
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(8);
        gridPane.setHgap(10);

        GridPane.setConstraints(labelUsername, 0, 0);
        GridPane.setConstraints(textFieldUsername, 1, 0);
        GridPane.setConstraints(labelPassword, 0, 1);
        GridPane.setConstraints(passwordField, 1, 1);
        GridPane.setConstraints(buttonConnect, 1, 2);
        GridPane.setConstraints(buttonCreateAccount, 0, 2);

        gridPane.getChildren().addAll(labelUsername, textFieldUsername, labelPassword, passwordField, buttonConnect,buttonCreateAccount);

        Scene scene = new Scene(gridPane, 400, 400);
        stage.setScene(scene);
        stage.show();
    }


    /**
     * Gère l'événement du clic sur le bouton de connexion.
     *
     * @param username Le nom d'utilisateur saisi.
     * @param password Le mot de passe saisi.
     */
    private void handleButtonConnect(String username, String password) {
        try {
            // Effectuer la connexion à la base de données avec les informations de connexion
            if (UtilisateurBd.getUtilisateur(username, password) != null){
                Client client = new Client(username,false);
                
                client.lancement();
                
                System.out.println("connexion réussie");
                Main.getInstance().setUtilisateurBd(UtilisateurBd.getUtilisateur(username, password));
                PagePrincipale pagePrincipale = new PagePrincipale(stage,client);
                pagePrincipale.show();
                
            } else {
                System.out.println("Connexion échouée");
            }
            
            // Vous pouvez ajouter des vérifications supplémentaires ici, par exemple,
            // afficher un message d'erreur si la connexion échoue
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Ouvre la page de création de compte.
     */
    private void openCreateAccountPage() {
        CreateAccountPage createAccountPage = new CreateAccountPage(stage);
        createAccountPage.show();
    }
}
