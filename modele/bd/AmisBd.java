package modele.bd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import src.Main;
import java.util.ArrayList;
import java.util.List;
import modele.code.Utilisateur;

/**
 * La classe AmisBd contient des méthodes pour gérer les relations d'amitié entre utilisateurs dans la base de données.
 */
public class AmisBd {
    private AmisBd() {}

    /**
     * Récupère la liste des utilisateurs qui ont reçu des messages de l'utilisateur spécifié.
     *
     * @param pseudo Le pseudo de l'utilisateur.
     * @return Une liste d'objets Utilisateur représentant les destinataires des messages.
     * @throws ClassNotFoundException Si la classe n'est pas trouvée lors de l'accès à la base de données.
     */
    public static List<Utilisateur> RecevoirMessage(String pseudo) throws ClassNotFoundException{
        List<Utilisateur> utilisateurs = new ArrayList<>();
        try{
            PreparedStatement ps = Main.getInstance().getSqlConnect().prepareStatement("SELECT * FROM AMIS join UTILISATEUR on AMIS.suivi = UTILISATEUR.id_U where AMIS.suiveur = (select id_U from UTILISATEUR where pseudo=?)");
            ps.setString(1, pseudo);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                utilisateurs.add(new Utilisateur(rs.getInt("id_U"), rs.getString("pseudo"),rs.getString("email"),rs.getString("mdp")));
            }
            return utilisateurs;
        }
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Récupère la liste des utilisateurs à qui l'utilisateur spécifié a envoyé des messages.
     *
     * @param pseudo Le pseudo de l'utilisateur.
     * @return Une liste d'objets Utilisateur représentant les destinataires des messages envoyés.
     * @throws ClassNotFoundException Si la classe n'est pas trouvée lors de l'accès à la base de données.
     */
    public static List<Utilisateur> EnvoyerMessage(String pseudo) throws ClassNotFoundException{
        List<Utilisateur> utilisateurs = new ArrayList<>();
        try{
            PreparedStatement ps = Main.getInstance().getSqlConnect().prepareStatement("SELECT * FROM AMIS join UTILISATEUR on AMIS.suiveur = UTILISATEUR.id_U where AMIS.suivi = (select id_U from UTILISATEUR where pseudo=?)");
            ps.setString(1, pseudo);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                utilisateurs.add(new Utilisateur(rs.getInt("id_U"), rs.getString("pseudo"),rs.getString("email"),rs.getString("mdp")));
            }
            return utilisateurs;
        }
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Récupère la liste des utilisateurs qui ne sont pas amis avec l'utilisateur spécifié.
     *
     * @param pseudo Le pseudo de l'utilisateur.
     * @return Une liste d'objets Utilisateur représentant les utilisateurs non amis.
     * @throws ClassNotFoundException Si la classe n'est pas trouvée lors de l'accès à la base de données.
     */
    public static List<Utilisateur> NonSuivi(String pseudo) throws ClassNotFoundException{
        List<Utilisateur> utilisateurs = new ArrayList<>();
        try{
            PreparedStatement ps = Main.getInstance().getSqlConnect().prepareStatement("SELECT * FROM UTILISATEUR U WHERE U.id_U != (SELECT id_U FROM UTILISATEUR WHERE pseudo = ?) AND U.id_U NOT IN (SELECT A.suivi FROM AMIS A WHERE A.suiveur = (SELECT id_U FROM UTILISATEUR WHERE pseudo = ?))");            
            ps.setString(1, pseudo);
            ps.setString(2, pseudo);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                utilisateurs.add(new Utilisateur(rs.getInt("id_U"), rs.getString("pseudo"),rs.getString("email"),rs.getString("mdp")));
            }
            return utilisateurs;
        }
        
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Récupère la liste des utilisateurs qui ne sont pas amis avec l'utilisateur spécifié.
     *
     * @param pseudo Le pseudo de l'utilisateur.
     * @return Une liste d'objets Utilisateur représentant les amis de l'utilisateur.
     * @throws ClassNotFoundException Si la classe n'est pas trouvée lors de l'accès à la base de données.
     */
    public static List<Utilisateur> ListeNonAmis(String pseudo) throws ClassNotFoundException{
        List<Utilisateur> utilisateurs = new ArrayList<>();
        try{
            PreparedStatement ps = Main.getInstance().getSqlConnect().prepareStatement("SELECT * FROM UTILISATEUR WHERE id_U <> (SELECT id_U FROM UTILISATEUR WHERE pseudo = ?) AND id_U NOT IN (SELECT suivi FROM AMIS WHERE suiveur = (SELECT id_U FROM UTILISATEUR WHERE pseudo = ?)) AND id_U <> (SELECT id_U FROM UTILISATEUR WHERE pseudo = ?)");
            ps.setString(1, pseudo);
            ps.setString(2, pseudo);
            ps.setString(3, pseudo);
            
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                utilisateurs.add(new Utilisateur(rs.getInt("id_U"), rs.getString("pseudo"),rs.getString("email"),rs.getString("mdp")));
            }
            return utilisateurs;
        }
        
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Ajoute un utilisateur à la liste d'amis de l'utilisateur spécifié.
     *
     * @param pseudo      Le pseudo de l'utilisateur.
     * @param pseudoAmis  Le pseudo de l'utilisateur à ajouter en tant qu'ami.
     * @throws ClassNotFoundException Si la classe n'est pas trouvée lors de l'accès à la base de données.
     */
    public static void ajouteAmis(String pseudo, String pseudoAmis) throws ClassNotFoundException{
        try{
            PreparedStatement ps = Main.getInstance().getSqlConnect().prepareStatement("INSERT INTO AMIS (suiveur, suivi) VALUES ((SELECT id_U FROM UTILISATEUR WHERE pseudo = ?), (SELECT id_U FROM UTILISATEUR WHERE pseudo = ?))");
            ps.setString(1, pseudo);
            ps.setString(2, pseudoAmis);
            ps.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Supprime un utilisateur de la liste d'amis de l'utilisateur spécifié.
     *
     * @param pseudo      Le pseudo de l'utilisateur.
     * @param pseudoAmis  Le pseudo de l'utilisateur à supprimer de la liste d'amis.
     * @throws ClassNotFoundException Si la classe n'est pas trouvée lors de l'accès à la base de données.
     */
    public static void supprimeAmis(String pseudo, String pseudoAmis) throws ClassNotFoundException{
        try{
            PreparedStatement ps = Main.getInstance().getSqlConnect().prepareStatement("DELETE FROM AMIS WHERE suiveur = (SELECT id_U FROM UTILISATEUR WHERE pseudo = ?) AND suivi = (SELECT id_U FROM UTILISATEUR WHERE pseudo = ?)");
            ps.setString(1, pseudo);
            ps.setString(2, pseudoAmis);
            ps.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
}
