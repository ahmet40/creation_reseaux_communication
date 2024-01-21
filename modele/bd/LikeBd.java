package modele.bd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import modele.code.Utilisateur;
import src.Main;


/**
 * La classe LikeBd gère les opérations liées aux likes et dislikes sur les messages.
 */
public class LikeBd {
    private LikeBd() {}
    
    /**
     * Compte le nombre de likes pour un message spécifié.
     *
     * @param idMessage L'identifiant du message.
     * @return Le nombre de likes pour le message.
     * @throws ClassNotFoundException Si la classe n'est pas trouvée lors de l'accès à la base de données.
     */
    public static int countLikeToMessage(int idMessage) throws ClassNotFoundException{
        int cpt=0;
        try{
            PreparedStatement ps = Main.getInstance().getSqlConnect().prepareStatement("SELECT count(*) FROM LIKES where id_M = ?");
            ps.setInt(1, idMessage);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                cpt = rs.getInt(1);
            }
            return cpt;
        }
        catch(SQLException e){
            e.printStackTrace();
            return cpt;
        }
    }

    /**
     * Compte le nombre de dislikes pour un message spécifié.
     *
     * @param idMessage L'identifiant du message.
     * @return Le nombre de dislikes pour le message.
     * @throws ClassNotFoundException Si la classe n'est pas trouvée lors de l'accès à la base de données.
     */
    public static int countDisikeToMessage(int idMessage) throws ClassNotFoundException{
        int cpt=0;
        try{
            PreparedStatement ps = Main.getInstance().getSqlConnect().prepareStatement("SELECT count(*) FROM DISLIKES where id_M = ?");
            ps.setInt(1, idMessage);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                cpt = rs.getInt(1);
            }
            return cpt;
        }
        catch(SQLException e){
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Ajoute un dislike pour un message spécifié par son identifiant et le pseudo du client.
     *
     * @param id           L'identifiant du message.
     * @param pseudoClient Le pseudo du client.
     * @throws ClassNotFoundException Si la classe n'est pas trouvée lors de l'accès à la base de données.
     */
    public static void ajouteDislike(int id, String pseudoClient) throws ClassNotFoundException{
        try{
            PreparedStatement ps = Main.getInstance().getSqlConnect().prepareStatement("INSERT INTO DISLIKES VALUES (?,(SELECT id_U FROM UTILISATEUR WHERE pseudo =?))");
            ps.setInt(1, id);
            ps.setString(2, pseudoClient);
            ps.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Ajoute un like pour un message spécifié par son identifiant et le pseudo du client.
     *
     * @param id           L'identifiant du message.
     * @param pseudoClient Le pseudo du client.
     * @throws ClassNotFoundException Si la classe n'est pas trouvée lors de l'accès à la base de données.
     */
    public static void ajouteLike(int id, String pseudoClient) throws ClassNotFoundException{
        try{
            PreparedStatement ps = Main.getInstance().getSqlConnect().prepareStatement("INSERT INTO LIKES VALUES (?,(SELECT id_U FROM UTILISATEUR WHERE pseudo =?))");
            ps.setInt(1, id);
            ps.setString(2, pseudoClient);
            ps.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Compte le nombre de likes pour un message spécifié par son identifiant et l'identifiant de l'utilisateur.
     *
     * @param idMessage L'identifiant du message.
     * @param idUser    L'identifiant de l'utilisateur.
     * @return Le nombre de likes pour le message et l'utilisateur.
     * @throws ClassNotFoundException Si la classe n'est pas trouvée lors de l'accès à la base de données.
     */
    public static int countLikeToMessageByUser(int idMessage,int idUser) throws ClassNotFoundException{
        int cpt=0;
        try{
            PreparedStatement ps = Main.getInstance().getSqlConnect().prepareStatement("SELECT count(*) FROM LIKES where id_M = ? and id_U = ?");
            ps.setInt(1, idMessage);
            ps.setInt(2, idUser);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                cpt = rs.getInt(1);
            }
            return cpt;
        }
        catch(SQLException e){
            e.printStackTrace();
            return cpt;
        }
    }

    /**
     * Compte le nombre de dislikes pour un message spécifié par son identifiant et l'identifiant de l'utilisateur.
     *
     * @param idMessage L'identifiant du message.
     * @param idUser    L'identifiant de l'utilisateur.
     * @return Le nombre de dislikes pour le message et l'utilisateur.
     * @throws ClassNotFoundException Si la classe n'est pas trouvée lors de l'accès à la base de données.
     */
    public static int countDislikeToMessageByUser(int idMessage,int idUser) throws ClassNotFoundException{
        int cpt=0;
        try{
            PreparedStatement ps = Main.getInstance().getSqlConnect().prepareStatement("SELECT count(*) FROM DISLIKES where id_M = ? and id_U = ?");
            ps.setInt(1, idMessage);
            ps.setInt(2, idUser);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                cpt = rs.getInt(1);
            }
            return cpt;
        }
        catch(SQLException e){
            e.printStackTrace();
            return cpt;
        }
    }
}