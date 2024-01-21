package modele.bd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import src.Main;
import java.util.ArrayList;
import java.util.List;
import modele.code.Utilisateur;

/**
 * La classe UtilisateurBd gère les opérations liées à la manipulation des utilisateurs dans la base de données.
 */
public class UtilisateurBd {
    private UtilisateurBd() {}

    /**
     * Récupère la liste de tous les utilisateurs présents dans la base de données.
     *
     * @return Une liste d'objets Utilisateur représentant tous les utilisateurs.
     * @throws ClassNotFoundException Si la classe n'est pas trouvée lors de l'accès à la base de données.
     */
    public static List<Utilisateur> AllUtilisateur() throws ClassNotFoundException{
        List<Utilisateur> utilisateurs = new ArrayList<>();
        try{
            PreparedStatement ps = Main.getInstance().getSqlConnect().prepareStatement("SELECT * FROM UTILISATEUR");
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
     * Récupère un utilisateur à partir de son pseudo et mot de passe.
     *
     * @param pseudo   Le pseudo de l'utilisateur.
     * @param password Le mot de passe de l'utilisateur.
     * @return Un objet Utilisateur si les informations sont valides, sinon null.
     * @throws ClassNotFoundException Si la classe n'est pas trouvée lors de l'accès à la base de données.
     */
    public static Utilisateur getUtilisateur(String pseudo, String password) throws ClassNotFoundException{
        try{
            PreparedStatement ps = Main.getInstance().getSqlConnect().prepareStatement("SELECT * FROM UTILISATEUR WHERE pseudo = ? AND mdp = ?");
            ps.setString(1, pseudo);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return new Utilisateur(rs.getInt("id_U"), rs.getString("pseudo"),rs.getString("email"),rs.getString("mdp"));
            }
            else{
                return null;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }

    }



    /**
     * Récupère un utilisateur à partir de son pseudo.
     *
     * @param pseudo Le pseudo de l'utilisateur.
     * @return Un objet Utilisateur si le pseudo est trouvé, sinon null.
     * @throws ClassNotFoundException Si la classe n'est pas trouvée lors de l'accès à la base de données.
     */
    public static Utilisateur recupererUtilisateur(String pseudo) throws ClassNotFoundException{
        try {
            PreparedStatement ps = Main.getInstance().getSqlConnect().prepareStatement("SELECT * FROM UTILISATEUR WHERE pseudo = ?");
            ps.setString(1, pseudo);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return new Utilisateur(rs.getInt("id_U"), rs.getString("pseudo"),rs.getString("email"),rs.getString("mdp"));
            }
            else{
                return null;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }


    public static boolean ajouterUtilisateur(String pseudo, String email, String mdp) throws ClassNotFoundException{
        try{
            if(UtilisateurExiste(pseudo,email)){
                return false;
            }
            PreparedStatement ps = Main.getInstance().getSqlConnect().prepareStatement("INSERT INTO UTILISATEUR (id_U,pseudo, email, mdp) VALUES (?, ?, ?, ?)");
            ps.setInt(1, getProchainId());
            ps.setString(2, pseudo);
            ps.setString(3, email);
            ps.setString(4, mdp);
            ps.executeUpdate();
            return true;
        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    private static int getProchainId() throws ClassNotFoundException{
        try{
            PreparedStatement ps = Main.getInstance().getSqlConnect().prepareStatement("SELECT MAX(id_U) FROM UTILISATEUR");
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getInt(1) + 1;
            }
            else{
                return 1;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            return 0;
        }
    }
    private static boolean UtilisateurExiste(String pseudo,String mail) throws ClassNotFoundException{
        try{
            PreparedStatement ps = Main.getInstance().getSqlConnect().prepareStatement("SELECT * FROM UTILISATEUR WHERE pseudo = ?");
            PreparedStatement ps2 = Main.getInstance().getSqlConnect().prepareStatement("SELECT * FROM UTILISATEUR WHERE email = ?");
            PreparedStatement ps3 = Main.getInstance().getSqlConnect().prepareStatement("SELECT * FROM UTILISATEUR WHERE pseudo = ? AND email = ?");
            ps.setString(1, pseudo);
            ps2.setString(1, mail);
            ps3.setString(1, pseudo);
            ps3.setString(2, mail);
            ResultSet rs = ps.executeQuery();
            ResultSet rs2 = ps2.executeQuery();
            ResultSet rs3 = ps3.executeQuery();
            if(rs.next() || rs2.next() || rs3.next()){
                return true;
            }
            else{
                return false;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Supprime un utilisateur spécifié par son pseudo.
     *
     * @param pseudo Le pseudo de l'utilisateur à supprimer.
     * @throws ClassNotFoundException Si la classe n'est pas trouvée lors de l'accès à la base de données.
     */
    public static void deleteUser(String pseudo) throws ClassNotFoundException{
        try{
            
            
            PreparedStatement ps1 = Main.getInstance().getSqlConnect().prepareStatement("DELETE FROM LIKES WHERE id_U=(SELECT id_U FROM UTILISATEUR WHERE pseudo=?)");
            PreparedStatement ps2 = Main.getInstance().getSqlConnect().prepareStatement("DELETE FROM DISLIKES WHERE id_U=(SELECT id_U FROM UTILISATEUR WHERE pseudo=?)");
            PreparedStatement ps3 = Main.getInstance().getSqlConnect().prepareStatement("DELETE FROM MESSAGES WHERE id_U=(SELECT id_U FROM UTILISATEUR WHERE pseudo=?)");
            PreparedStatement ps4 = Main.getInstance().getSqlConnect().prepareStatement("DELETE FROM AMIS WHERE suiveur=(SELECT id_U FROM UTILISATEUR WHERE pseudo=?)");
            PreparedStatement ps5 = Main.getInstance().getSqlConnect().prepareStatement("DELETE FROM AMIS WHERE suivi=(SELECT id_U FROM UTILISATEUR WHERE pseudo=?)");
            PreparedStatement ps6 = Main.getInstance().getSqlConnect().prepareStatement("DELETE FROM UTILISATEUR WHERE pseudo=?");
            ps1.setString(1, pseudo);
            ps2.setString(1, pseudo);
            ps3.setString(1, pseudo);
            ps4.setString(1, pseudo);
            ps5.setString(1, pseudo);
            ps6.setString(1, pseudo);
            ps1.executeUpdate(); ps2.executeUpdate(); ps3.executeUpdate(); ps4.executeUpdate(); ps5.executeUpdate();ps6.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }


    /**
     * Modifie le mot de passe d'un utilisateur spécifié par son pseudo.
     *
     * @param pseudo Le pseudo de l'utilisateur.
     * @param mdp    Le nouveau mot de passe de l'utilisateur.
     * @throws ClassNotFoundException Si la classe n'est pas trouvée lors de l'accès à la base de données.
     */
    public static boolean pseudoExiste(String pseudo) throws ClassNotFoundException{
        try{
            PreparedStatement ps = Main.getInstance().getSqlConnect().prepareStatement("SELECT * FROM UTILISATEUR WHERE pseudo = ?");
            ps.setString(1, pseudo);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return true;
            }
            else{
                return false;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }
}
