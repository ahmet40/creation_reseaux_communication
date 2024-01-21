package modele.code;

/**
 * La classe Amis représente une relation d'amitié entre deux utilisateurs.
 */
public class Amis {

    private int suiveur;
    private int suivi;

    /**
     * Construit un objet Amis avec les identifiants des utilisateurs concernés.
     *
     * @param idUtilisateur1 Identifiant du premier utilisateur (suiveur).
     * @param idUtilisateur2 Identifiant du deuxième utilisateur (suivi).
     */
    public Amis(int idUtilisateur1, int idUtilisateur2) {
        this.suiveur = idUtilisateur1;
        this.suivi = idUtilisateur2;
    }

    /**
     * Obtient l'identifiant du suiveur.
     *
     * @return L'identifiant du suiveur.
     */
    public int getSuiveur() {
        return suiveur;
    }

    /**
     * Obtient l'identifiant du suivi.
     *
     * @return L'identifiant du suivi.
     */
    public int getSuivi() {
        return suivi;
    }
}
