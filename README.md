# SAE_RESEAU

## Origine du projet :
    Ce projet à était réliser par des étudiant de deuxième année de BUT informatique. Il consiste à crée un mini réseau permettant de lire les messages des personnes auquel on est abbonées et d'écrire aux personnes qui sont abonnées à nous. Un système de like,dislike et de suppression de message devait aussi être implementée.

## Membre du projet :
    - JACQUET NOA 2A3B
    - BABA Ahmet 2A3B

## Que peut on y faire :
    - Client
    Lorsque vous lancez l'application, il vous est proposé de créer un compte ou de vous connecter si vous en avez déjà un. Une fois connecter, vous pouvez voir les anciens messages qui ont été écrits, par les personnes auxquelles vous êtes abonnées.
    De plus, si un nouveau message est écrit alors apparaîtra sur la partie centrale de l'application, vous pouvez aussi liker, disliker et si les messages vous appartiennent alors vous pourrez les supprimer (Dans ce cas tous les autre utilisateurs verront le nombre de like ou dislike du message augmenter ou bien ils ne verront plus le message si vous l'avez supprimer). À gauche, vous pouvez voir tous les utilisateurs auxquels vous n'êtes pas abonnées, mais que vous pouvez. À droite, il y a les utilisateurs auxquels vous êtes abonnes et que vous pouvez, vous désabonnez. Sur cette page en bas, vous trouverez aussi une plage pour écrire et envoyez un message dans le cas où il ne serait pas vide.

    - Server
    Le server va devoir écrire les messages sur le terminale (pas d'interface graphique). Si il écrit le message "/deleteMessage idMessage" alors il supprimera le message avec cette id et les utilisateur ne pourront plus le voir. Il peut aussi faire "/deleteUser pseudo", dans ce cas l'utilisateur avec ce pseudo sera supprimer de la bd et dans le cas ou il est connecter il verra apparaitre une pop-up lui disant qu'il à était banni de l'application.

## Lancement

    Pour lancer ce projet il vous faut les libraries javafx vous devez donc installer ces libraries puis dans vscode vous configurer launch.json vous allez inserer la ligne '"vmArgs": "--module-path /chemin/vers/openjfx/lib --add-modules javafx.controls,javafx.fxml", aprés la ligne '"request": "launch",'. Ensuite vous ouvrer settings.json et vous faite sa : 

    {
    "java.project.referencedLibraries": {
        "include": [
            "lib**/*.jar",
            "/chemin/projet/SAE_RESEAU/lib/*.jar" ,
            "/chemin/vers/openjfx/lib/javafx-swt.jar",
            "/chemin/vers/openjfx/lib/javafx.base.jar",
            "/chemin/vers/openjfx/lib/javafx.controls.jar",
            "/chemin/vers/openjfx/lib/javafx.fxml.jar",
            "/chemin/vers/openjfx/lib/javafx.web.jar",
            "/chemin/vers/openjfx/lib/javafx.swing.jar",
            "/chemin/vers/openjfx/lib/javafx.media.jar",
            "/chemin/vers/openjfx/lib/javafx.graphics.jar"
        ],
        
    }
    }
    
    Ps : nous n'avons pas eu le temps de faire un fichier de lancement je tiens à m'en excuser.

