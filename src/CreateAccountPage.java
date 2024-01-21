package src;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import modele.bd.UtilisateurBd;

/**
 * La classe CreateAccountPage gère la page de création de compte.
 */
public class CreateAccountPage {
    private Stage stage;

    /**
     * Crée une instance de CreateAccountPage.
     *
     * @param stage Le stage.
     */
    public CreateAccountPage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Affiche la page de création de compte.
     */
    public void show() {
        stage.setTitle("Création de compte");

        // Création des composants
        Label labelUsername = new Label("Nom d'utilisateur:");
        TextField textFieldUsername = new TextField();

        Label labelPassword = new Label("Mot de passe:");
        PasswordField passwordField = new PasswordField();

        Label labelEmail = new Label("Email:");
        TextField textFieldEmail = new TextField();

        Button buttonCreateAccount = new Button("Créer le compte");
        buttonCreateAccount.setOnAction(e -> handleButtonCreateAccount(
                textFieldUsername.getText(),
                passwordField.getText(),
                textFieldEmail.getText()
        ));
        Button buttonBackToLogin = new Button("Retour à la connexion");
        buttonBackToLogin.setOnAction(e -> openLoginPage());
        // Mise en page avec GridPane
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(8);
        gridPane.setHgap(10);

        // On place les composants dans le GridPane
        // Le username
        GridPane.setConstraints(labelUsername, 0, 0);
        GridPane.setConstraints(textFieldUsername, 1, 0);
        
        // L'email
        GridPane.setConstraints(labelEmail, 0, 1);
        GridPane.setConstraints(textFieldEmail, 1, 1);
        
        // Le mot de passe
        GridPane.setConstraints(labelPassword, 0, 2);
        GridPane.setConstraints(passwordField, 1, 2);
        
        // Le bouton de création de compte
        GridPane.setConstraints(buttonCreateAccount, 1, 3);
        GridPane.setConstraints(buttonBackToLogin, 0, 4, 2, 1); // 2 colonnes de large
        
        gridPane.getChildren().addAll(labelUsername, textFieldUsername, labelEmail, textFieldEmail, labelPassword, passwordField, buttonCreateAccount, buttonBackToLogin);
        Scene scene = new Scene(gridPane, 400, 400);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Gère le clic sur le bouton de création de compte.
     *
     * @param username Le nom d'utilisateur.
     * @param password Le mot de passe.
     * @param email    L'email.
     */
    private void handleButtonCreateAccount(String username, String password, String email) {
        Boolean isCreated = false;
        try {
            isCreated = UtilisateurBd.ajouterUtilisateur(username, email, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (isCreated){
            try{
                if (UtilisateurBd.getUtilisateur(username, password) != null){
                    Client client = new Client(username,true);
                    
                    client.lancement();
                    Main.getInstance().setUtilisateurBd(UtilisateurBd.getUtilisateur(username, password));
                    PagePrincipale pagePrincipale = new PagePrincipale(stage,client);
                    pagePrincipale.show();
                    
                }
            }
            catch(ClassNotFoundException e){
                e.printStackTrace();
            }
        }
        else {
            showAlert("Erreur de création de compte", "Ce pseudo est déjà utilisé. Veuillez en choisir un autre.");
        }
    }

    /**
     * Affiche une alerte.
     *
     * @param title   Le titre de l'alerte.
     * @param content Le contenu de l'alerte.
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Ouvre la page de connexion.
     */
    private void openLoginPage() {
        LoginPage loginPage = new LoginPage(stage);
        loginPage.show();
    }
}
