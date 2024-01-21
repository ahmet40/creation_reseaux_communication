package modele.code;

/**
 * La classe Utilisateur représente un utilisateur du système.
 */
public class Utilisateur {

    private int id;
    private String pseudo;
    private String email;
    private String motDePasse;

    /**
     * Construit un objet Utilisateur avec les détails spécifiés.
     *
     * @param id          Identifiant unique de l'utilisateur.
     * @param pseudo      Pseudo de l'utilisateur.
     * @param email       Adresse e-mail de l'utilisateur.
     * @param motDePasse  Mot de passe de l'utilisateur.
     */
    public Utilisateur(int id, String pseudo, String email, String motDePasse) {
        this.id = id;
        this.pseudo = pseudo;
        this.email = email;
        this.motDePasse = motDePasse;
    }

    /**
     * Obtient l'adresse e-mail de l'utilisateur.
     *
     * @return L'adresse e-mail de l'utilisateur.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Obtient l'identifiant unique de l'utilisateur.
     *
     * @return L'identifiant unique de l'utilisateur.
     */
    public int getId() {
        return id;
    }

    /**
     * Obtient le mot de passe de l'utilisateur.
     *
     * @return Le mot de passe de l'utilisateur.
     */
    public String getMotDePasse() {
        return motDePasse;
    }

    /**
     * Obtient le pseudo de l'utilisateur.
     *
     * @return Le pseudo de l'utilisateur.
     */
    public String getPseudo() {
        return pseudo;
    }

    /**
     * Définit toutes les propriétés de l'utilisateur.
     *
     * @param id          Nouvel identifiant unique de l'utilisateur.
     * @param pseudo      Nouveau pseudo de l'utilisateur.
     * @param email       Nouvelle adresse e-mail de l'utilisateur.
     * @param motDePasse  Nouveau mot de passe de l'utilisateur.
     */
    public void setAll(int id, String pseudo, String email, String motDePasse) {
        this.id = id;
        this.pseudo = pseudo;
        this.email = email;
        this.motDePasse = motDePasse;
    }
}
