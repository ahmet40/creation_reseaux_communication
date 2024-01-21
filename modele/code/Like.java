package modele.code;

/**
 * La classe Like représente un "j'aime" (like) attribué par un utilisateur à un message.
 */
public class Like {

    private int idMessage;
    private int idUtilisateur;

    /**
     * Construit un objet Like associé à un message et un utilisateur.
     *
     * @param idMessage      Identifiant du message liké.
     * @param idUtilisateur  Identifiant de l'utilisateur qui a attribué le like.
     */
    public Like(int idMessage, int idUtilisateur) {
        this.idMessage = idMessage;
        this.idUtilisateur = idUtilisateur;
    }

    /**
     * Obtient l'identifiant du message liké.
     *
     * @return L'identifiant du message liké.
     */
    public int getIdMessage() {
        return idMessage;
    }

    /**
     * Obtient l'identifiant de l'utilisateur ayant attribué le like.
     *
     * @return L'identifiant de l'utilisateur ayant attribué le like.
     */
    public int getIdUtilisateur() {
        return idUtilisateur;
    }
}
