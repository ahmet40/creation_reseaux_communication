package modele.code;

/**
 * La classe Message représente un message publié par un utilisateur.
 */
public class Message {

    private int idMessage;
    private int idUtilisateur;
    private String contenu;
    private String date;
    private String pseudo;

    /**
     * Construit un objet Message avec les détails spécifiés.
     *
     * @param idMessage      Identifiant unique du message.
     * @param idUtilisateur  Identifiant de l'utilisateur qui a publié le message.
     * @param contenu        Contenu du message.
     * @param date           Date de publication du message.
     * @param pseudo         Pseudo de l'utilisateur qui a publié le message.
     */
    public Message(int idMessage, int idUtilisateur, String contenu, String date, String pseudo) {
        this.idMessage = idMessage;
        this.idUtilisateur = idUtilisateur;
        this.contenu = contenu;
        this.date = date;
        this.pseudo = pseudo;
    }

    /**
     * Obtient le contenu du message.
     *
     * @return Le contenu du message.
     */
    public String getContenu() {
        return contenu;
    }

    /**
     * Obtient la date de publication du message.
     *
     * @return La date de publication du message.
     */
    public String getDate() {
        return date;
    }

    /**
     * Obtient l'identifiant unique du message.
     *
     * @return L'identifiant unique du message.
     */
    public int getIdMessage() {
        return idMessage;
    }

    /**
     * Obtient l'identifiant de l'utilisateur qui a publié le message.
     *
     * @return L'identifiant de l'utilisateur qui a publié le message.
     */
    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    /**
     * Obtient le pseudo de l'utilisateur qui a publié le message.
     *
     * @return Le pseudo de l'utilisateur qui a publié le message.
     */
    public String getPseudo() {
        return pseudo;
    }
}
