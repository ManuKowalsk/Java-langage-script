lisez moi
=========

groupe: KOCA MONA
Membres:  
    * KOWALSKI Emmanuel
    * CARIOU Nathan
    * MOUREAU Maxence
    * NADAL Alexandre

## Rendu

Nous avons rendu de l'exercice 1 à 4.2 de la partie Robi et la partie 1 à 4 de la partie serveur client.
Le dossier client_robi contient le la partie client et l'interface graphique qui envoie des commandes et affiche l'image du résultat.
Le dossier Serveur_robi contient le serveur qui traite les commandes recus et renvoie une image en base64.

## Utilisation

Si les étapes ont été rigoureusement suivies, une interface graphique s'est lancée.  
Elle contient les éléments suivants:
- Champ de saisie : vous pouvez ici entrer les instructions sous forme de S-expression.
- Bouton « Envoyer » : Il permet d’envoyer l’ensemble le script au serveur.
- Bouton « Exécuter » : Il permet d’exécuter les commandes envoyées.
- Boutons radio : Ils permettent de choisir le mode d’exécution : 
    * Pas à pas : permet d’exécuter une à une les instructions.
    * Bloc : permet d’exécuter les instructions d’une traite.
- Console :  retranscrit les commandes reçues par le client. 
- menu défilant:
    * Fichier : permet d’ouvrir d’un fichier .txt présent dans sa machine, l’ouvrir et envoyer le texte dans le champ de saisie.
    * Quitter : permet de se déconnecter du serveur et de fermer la fenêtre.

Voici les étapes à respecter pour afficher le dessin de votre choix:
1. Entrez le script d'instruction dans le champ de saisie
2. Cliquez sur le bouton "Envoyer" pour envoyer au serveur les instructions.
3. Choisir le mode d'execution en selectionnant le bouton radio "pas à pas" ou "bloc".
4. Cliquez sur le bouton "Executer".
5. Dans le cas du mode "pas à pas", il faut cliquer plusieurs fois sur "Executer" pour voir le résultat des instructions les unes après les autres.
6. Le résultat du script apparait dans la console.

## Bilan critique

Nous aurions pu modifier la façon de prendre le screenshot et la façon de l'afficher sur l'interface. Nous aurions pu par exemple améliorer la lisibilité du code en créant plus de classe pour isoler la partie connexion.

## Nous n'avons pas pu faire :
- La partie 5 de l'interface et nous n'avons pas finis non plus la question 5 et 6 de la partie Robi.



