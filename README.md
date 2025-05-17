# JSP to Thymeleaf Converter Library

## Vue d'ensemble

Cette bibliothèque Java permet de convertir du code JSP (JavaServer Pages) en code Thymeleaf équivalent en utilisant les capacités des grands modèles de langage (LLM), notamment l'API Gemini de Google.

## Fonctionnalités

- **Conversion intelligente** : Utilisation d'un LLM pour analyser et transformer le code JSP en Thymeleaf
- **Support des constructions courantes** : Gestion des tags JSTL, expressions EL, inclusions, etc.
- **Identification des problèmes** : Mise en évidence des éléments impossibles à convertir automatiquement
- **API Java simple** : Utilisation facile dans n'importe quel projet Java
- **Clé API intégrée** : Fonctionne immédiatement avec une clé API par défaut

## Installation

### Maven

```xml
<dependency>
    <groupId>org.mql.llm</groupId>
    <artifactId>jsp2thymeleaf-converter</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

```groovy
implementation 'org.mql.llm:jsp2thymeleaf-converter:1.0.0'
```

## Utilisation

### Exemple de base

```java
import org.mql.llm.Jsp2ThymeleafConverter;

// Création du convertisseur avec la clé API par défaut
Jsp2ThymeleafConverter converter = new Jsp2ThymeleafConverter();

// Code JSP à convertir
String jspCode = "<%@ page language=\"java\" contentType=\"text/html; charset=UTF-8\" %>" +
                "<h1>Hello ${user.name}!</h1>";

// Conversion en Thymeleaf
String thymeleafCode = converter.convert(jspCode);

System.out.println(thymeleafCode);
```

### Avec une clé API personnalisée

```java
// Utilisation d'une clé API personnalisée
Jsp2ThymeleafConverter converter = new Jsp2ThymeleafConverter("YOUR_API_KEY_HERE");
String thymeleafCode = converter.convert(jspCode);
```

### Configuration avancée

```java
// Configuration personnalisée complète
Jsp2ThymeleafConverter converter = new Jsp2ThymeleafConverter(
    "YOUR_API_KEY_HERE",        // Clé API
    "gemini-1.5-pro",           // Modèle
    0.3,                        // Température
    16384                       // Tokens max
);
```

## Hiérarchie des clés API

La bibliothèque cherche la clé API dans l'ordre suivant :

1. Variable d'environnement `GEMINI_API_KEY`
2. Propriété système `gemini.api.key`
3. Clé API fournie au constructeur
4. Clé API par défaut intégrée

## Architecture

- **Jsp2ThymeleafConverter** : Façade principale pour utiliser la bibliothèque
- **GeminiService** : Service responsable de l'interaction avec l'API Gemini
- **JspToThymeleafService** : Service orchestrant la conversion, incluant la préparation du prompt et le nettoyage du résultat
- **ApiKeyProvider** : Gestion hiérarchique des clés API

## Compatibilité

- Java 17+
- Compatible avec Spring/Spring Boot (facultatif)

## Licence

MIT
