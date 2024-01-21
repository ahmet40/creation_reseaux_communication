package modele.bd;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * La classe ConnexionBd gère la connexion à une base de données MySQL.
 */
public class ConnexionBd {
    
    private Connection connection;
	private boolean isConnected;
	
    /**
     * Constructeur de la classe ConnexionBd.
     *
     * @throws ClassNotFoundException Si la classe du pilote JDBC n'est pas trouvée.
     */
    public ConnexionBd() throws ClassNotFoundException{
        this.connection = null;
        this.isConnected = false;
	}

	/**
     * Établit une connexion à la base de données avec les paramètres fournis.
     *
     * @param nomServeur   Le nom du serveur de la base de données.
     * @param nomBase      Le nom de la base de données.
     * @param nomLogin     Le nom d'utilisateur pour la connexion.
     * @param motDePasse   Le mot de passe pour la connexion.
     * @throws SQLException Si une erreur survient lors de la tentative de connexion.
     */
	public void connect(String nomServeur, String nomBase, String nomLogin, String motDePasse) throws SQLException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			this.connection = DriverManager.getConnection("jdbc:mysql://"+nomServeur+":3306/"+nomBase,nomLogin,motDePasse);
		} catch ( SQLException ex ) {
			System.out.println("Msg : " + ex.getMessage() + "   " + ex.getErrorCode());
		}
		catch(ClassNotFoundException ex){
			System.out.println("Msg : " + ex.getMessage());
		}
		this.isConnected = this.connection != null;
	}


    /**
     * Ferme la connexion à la base de données.
     *
     * @throws SQLException Si une erreur survient lors de la fermeture de la connexion.
     */
	public void close() throws SQLException {
        this.connection.close();
        this.connection = null;
        this.isConnected = false;
	}

	/**
     * Vérifie si la connexion à la base de données est établie.
     *
     * @return True si la connexion est établie, sinon false.
     */
    public boolean isConnected() { 
        return this.isConnected;
    }
    
    /**
     * Crée un objet Statement pour exécuter des requêtes SQL.
     *
     * @return Un objet Statement.
     * @throws SQLException Si une erreur survient lors de la création de l'objet Statement.
     */
	public Statement createStatement() throws SQLException {
		return this.connection.createStatement();
	}


    /**
     * Prépare une requête SQL pour l'exécution.
     *
     * @param requete La requête SQL à préparer.
     * @return Un objet PreparedStatement.
     * @throws SQLException Si une erreur survient lors de la préparation de la requête.
     */
	public PreparedStatement prepareStatement(String requete) throws SQLException{
		return this.connection.prepareStatement(requete);
	}
}

