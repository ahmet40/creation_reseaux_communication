package src;

import java.sql.SQLException;
import javafx.application.Application;
import javafx.stage.Stage;
import modele.bd.ConnexionBd;
import modele.code.Utilisateur;

/**
 * La classe Main est la classe principale de l'application. Elle gère l'initialisation de l'application JavaFX
 * et maintient une instance unique de la connexion à la base de données.
 */
public class Main extends Application {
    private ConnexionBd sqlConnect;
    private static Main instance;
    private Utilisateur utilisateurBd;

    /**
     * Obtient l'instance unique de la classe Main.
     *
     * @return L'instance unique de la classe Main.
     */
    public static Main getInstance() {
        if (instance == null) {
            instance = new Main();
        }
        return instance;
    }

    /**
     * Initialise l'application en créant une connexion à la base de données.
     *
     * @throws SQLException Si une erreur survient lors de la connexion à la base de données.
     * @throws ClassNotFoundException Si la classe du pilote de base de données n'est pas trouvée.
     */
    @Override
    public void init() throws SQLException, ClassNotFoundException {
        instance = this;
        this.utilisateurBd = null;
        try {
            if (sqlConnect == null) {
                sqlConnect = new ConnexionBd();
                sqlConnect.connect("localhost", "SAE_RESEAUX", "temha", "temha1011");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtient la connexion à la base de données.
     *
     * @return La connexion à la base de données.
     * @throws ClassNotFoundException Si la classe du pilote de base de données n'est pas trouvée.
     */
    public ConnexionBd getSqlConnect() throws ClassNotFoundException {
        try {
            if (sqlConnect == null) {
                sqlConnect = new ConnexionBd();
                sqlConnect.connect("localhost", "SAE_RESEAUX", "temha", "temha1011");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this.sqlConnect;
    }

    /**
     * Obtient l'utilisateur associé à l'application.
     *
     * @return L'utilisateur associé à l'application.
     */
    public Utilisateur getUtilisateurBd() {
        return utilisateurBd;
    }

    /**
     * Définit l'utilisateur associé à l'application.
     *
     * @param utilisateurBd L'utilisateur à associer à l'application.
     */
    public void setUtilisateurBd(Utilisateur utilisateurBd) {
        this.utilisateurBd = utilisateurBd;
    }

    /**
     * Le point d'entrée principal de l'application.
     *
     * @param args Les arguments de la ligne de commande.
     */
    public static void main(String[] args) {
        // Lancer l'application JavaFX
        launch(args);
    }

    /**
     * Démarre l'application en affichant la page de connexion.
     *
     * @param primaryStage La fenêtre principale de l'application.
     * @throws Exception Si une exception survient lors du démarrage de l'application.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // La page de connexion est gérée dans la classe LoginPage
        LoginPage loginPage = new LoginPage(primaryStage);
        loginPage.show();
    }
}
 


// "lib/**/*.jar",
// "/home/baba/Téléchargements/openjfx-21.0.1_linux-x64_bin-sdk/*",


// , "java.jdt.ls.vmargs": "--module-path /home/baba/Téléchargements/openjfx-21.0.1_linux-x64_bin-sdk/javafx-sdk-21.0.1/lib --add-modules javafx.controls,javafx.fxml"

// /usr/bin/env /usr/lib/jvm/openjdk-21/bin/java @/tmp/cp_40ogxzvjd7rdfyi07xufk8kfi.argfile src.Serve