# friendtracker
FriendTraker
Contexte
Dans le but de faciliter les sorties entre amis, l’organisation et la gestion de ces dernières, nous souhaitons mettre en place une application permettant aux utilisateurs de pouvoir créer et gérer des groupes afin d’organiser des événements. On pourra déterminer les lieux, dates et heures de rencontre de ces événements ainsi que suivre l’avancée des personnes (le souhaitant) vers le lieu de rencontre. L’itinéraires des membres vers le lieu de rencontre sera calculé et affiché.
Fonctionnalités de l’application
L’application permettra de : 
-	Ajouter des amis
-	Créer un groupe d’amis
-	Obtenir la localisation des amis (si activé)
-	Partager sa position avec des amis, et des groupes d’amis
-	Créer un événement pour un groupe d’amis
-	Création et calcul d’itinéraire vers un événement
Technologies utilisées
API Google Direction : création des itinéraires.
API Google Map : affichage de la carte et de la géolocalisation des membres et des évènements.
API Google Activity Recognition : afin de détecter le type de transport des membres.
API Google Places : afficher les informations de lieux publics (bar, restaurants, cinémas).
Android KitKat 

Persistance des données
Persistance des données en local seront utilisé pour sauvegarder les préférences utilisateurs, ainsi que certaines informations sur les rendez-vous comme le lieu, et l’horaire.
Les données globales de l’application seront sauvegardées sur Firebase Realtime Database
App Widget
Mise en place d’une app Widget permettant d’afficher les prochains événements concernant l’utilisateur.
 
Fragments 
Les fragments seront utilisés dans au sein de notre application dans l’affichage des menus des groupes et des fonctionnalités liées au groupes.

