# ChriOnline – Application E-Commerce Client-Serveur en Java

Description du projet

ChriOnline est une application e-commerce développée en Java basée sur une architecture client-serveur utilisant les sockets TCP/UDP.
Elle permet aux utilisateurs de consulter des produits, gérer un panier et effectuer des achats en ligne avec un système de paiement simulé.

Ce projet a été réalisé dans le cadre du module Programmation Réseau / Systèmes Distribués afin de mettre en pratique les concepts de :

programmation réseau avec Java Sockets

>architecture client-serveur
>gestion de base de données
>gestion de plusieurs clients simultanés
>conception d’un système e-commerce simplifié
# Architecture du système
L'application suit une architecture client-serveur.

Client Java
   |
   | TCP Socket
   |
Serveur Java
   |
   | JDBC
   |
Base de données (MySQL / SQLite)
Client

Le client permet à l'utilisateur de :

>se connecter au serveur
>consulter les produits
>gérer le panier
>effectuer une commande

Le serveur :

>gère les connexions des clients
>traite les requêtes
>communique avec la base de données
>gère les commandes et paiements simulés

# Fonctionnalités principales
Gestion des utilisateurs

>inscription
>authentification
>gestion des sessions

Gestion des produits

>affichage des produits
>consultation des détails d’un produit

Gestion du panier

>ajouter un produit
>supprimer un produit
>calculer le total du panier

Gestion des commandes

>validation de commande
>génération d’un identifiant unique
>enregistrement des commandes

Paiement simulé

>paiement fictif par carte
>confirmation de la commande

# Technologies utilisées

Langage :

>Java

Communication réseau :

>Socket
>ServerSocket
>DatagramSocket (optionnel)

Base de données :

>MySQL / SQLite

Accès base de données :

>JDBC

IDE recommandés :

>Eclipse
>IntelliJ IDEAà
>NetBeans

Gestion de version :

>Git
>GitHub

# Structure du projet
ChriOnline
│
├── client
│   └── code du client Java
│
├── server
│   └── code du serveur Java
│
├── models
│   └── classes User, Product, Cart, Order
│
├── database
│   └── connexion base de données
│
├── services
│   └── logique métier
│
└── README.md


# Base de données

Tables principales :

# Users

id
name
email
password

# Products

id
name
price
description
stock

# Cart

id
user_id
product_id

quantity

Orders

id
user_id
total
date
