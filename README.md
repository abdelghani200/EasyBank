# Gestion Bancaire



## Name
Application Java pour gérer les simulations et demandes de crédits.

## Contexte du projet

Après le test et la mise en préproduction de la première version de l’application de gestion des comptes bancaire.

EasyBank prépare ses nouveaux besoins.

L'objectif est d’alimenter le backlog par les user stories correspondant aux besoins suivants et de les implémenter:


Un compte bancaire appartient à une agence.
Une agence est caractérisée par un code, nom, adresse, numéro de téléphone.
Un employé est affecté à une agence.
Un employé peut être muté d’une agence à une autre(les dates doivent être sauvegardées).
Le transfert d’argent(virement) est un troisième type de transaction qu’il faut prendre en charge, il est caractérisé par un compte source et destination et un montant.
Il faut aussi enregistrer le temps de la transaction.
Une simulation est calculée selon la formule en pièces jointes.
Une demande de crédit est enregistrée après la validation de la simulation par un employé, elle est caractérisée par: numéro, client, agence, date, état, montant, durée et remarques.