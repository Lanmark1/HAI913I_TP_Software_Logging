# HAI913I - TP5 Journalisation et observabilité logicielle

## TP réalisé par :
- BENTOLILA Jérémie

## Pré-requis

- Node.js >= 6.14.10 
- Java JDK 17

## Installation / Exécution

- Extraire l'archive du TP dans un dossier
- Ouvrir une console de commande à la racine du TP
- En fonction de ce que vous voulez exécuter, taper les commandes correspondantes :
  - Lancer l'API :
  ``` 
  java -jar ./softlog_api/softlog_api.jar
  ```
  - Lancer la CLI 
  ``` 
  java -jar ./softlog_cli/softlog_cli.jar
  ``` 
  - Lancer l'application front-end
  ```
  cd softlog_logs_consumer
  npm install
  npm start
  ```
## Utilisation

Commencer par lancer l'API. 
Puis au choix :
- Lancer la CLI pour tester le bon fonctionnement de celle-ci et de l'API
- Lancer l'application consommatrice des logs pour s'assurer de la bonne consommation des logs :
  - Cliquer sur 'Use data from the local logs' pour utiliser les logs locaux
  - Cliquer sur 'Use data from the API' pour utiliser les profils utilisateurs pré-construit en base de données

La trace de la construction des profils utilisateurs est visible depuis la console. (Ctrl + Maj + i)
