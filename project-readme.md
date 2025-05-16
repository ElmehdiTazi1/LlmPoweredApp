# JSP to Thymeleaf Converter

## Vue d'ensemble

Ce projet est une application Spring Boot spécialisée dans la conversion de code JSP (JavaServer Pages) en code Thymeleaf équivalent en utilisant les capacités des grands modèles de langage (LLM), notamment l'API Gemini de Google.

## Fonctionnalités

- **Conversion intelligente** : Utilisation d'un LLM pour analyser et transformer le code JSP en Thymeleaf
- **Support des constructions courantes** : Gestion des tags JSTL, expressions EL, inclusions, etc.
- **Identification des problèmes** : Mise en évidence des éléments impossibles à convertir automatiquement
- **API REST** : Endpoints permettant l'intégration avec d'autres applications
- **Interface utilisateur** : Interface web simple pour effectuer des conversions manuelles


## Comment ça fonctionne

1. L'utilisateur soumet du code JSP via l'API REST ou l'interface web
2. L'application génère un prompt détaillé contenant des instructions spécifiques pour le modèle LLM
3. Le code JSP est envoyé au modèle Gemini avec le prompt spécialisé
4. Le modèle analyse le code et génère l'équivalent Thymeleaf
5. Le code converti est retourné à l'utilisateur

## Architecture

- **GeminiService** : Service responsable de l'interaction avec l'API Gemini
- **JspToThymeleafService** : Service principal orchestrant la conversion, y compris la préparation du prompt et le nettoyage du résultat
- **ConversionController** : Contrôleur REST exposant les endpoints de conversion
