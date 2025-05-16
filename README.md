# JSP to Thymeleaf Converter

## Description

Cette application Spring Boot utilise l'API Gemini de Google pour convertir automatiquement le code JSP en code Thymeleaf équivalent. L'application expose des endpoints REST pour effectuer la conversion et inclut une interface utilisateur web simple pour faciliter son utilisation.

## Fonctionnalités

- Conversion de code JSP vers Thymeleaf en utilisant l'IA
- Support des tags JSTL courants
- Support des expressions EL
- Gestion des cas particuliers avec commentaires explicatifs
- Interface REST pour l'intégration avec d'autres applications
- Interface utilisateur web pour les conversions manuelles

## Endpoints API

### 1. Conversion JSP vers Thymeleaf (Réponse JSON)

```
POST /api/conversion/jsp-to-thymeleaf
```

**Payload**:
```json
{
  "jspCode": "<votre code JSP ici>"
}
```

**Réponse**:
```json
{
  "thymeleafCode": "<code Thymeleaf convertit>",
  "timestamp": "2023-05-16T14:25:36.123",
  "success": "true"
}
```

### 2. Conversion JSP vers Thymeleaf (HTML brut)

```
POST /api/conversion/jsp-to-thymeleaf/raw
```

**Payload**:
```json
{
  "jspCode": "<votre code JSP ici>"
}
```

**Réponse**: Code Thymeleaf au format HTML brut

## Technologies utilisées

- Spring Boot
- Gemini API (Google LLM)
- Thymeleaf
- Swagger/OpenAPI pour la documentation API

## Configuration

La configuration principale se trouve dans le fichier `application.properties`:

- `gemini.api.key`: Clé API pour accéder à Gemini
- `conversion.model`: Modèle Gemini à utiliser
- `conversion.temperature`: Température (créativité) pour la génération
- `conversion.max-tokens`: Nombre maximum de tokens pour la réponse
- `conversion.prompt.template`: Template de prompt utilisé pour guider le modèle LLM

## Démarrage rapide

1. Assurez-vous que Java 17+ est installé
2. Clonez le dépôt
3. Configurez la clé API Gemini dans `application.properties` (si nécessaire)
4. Exécutez l'application avec `./mvnw spring-boot:run`
5. Accédez à l'interface web à l'adresse http://localhost/
6. Pour l'API, consultez la documentation Swagger à l'adresse http://localhost/swagger-ui/index.html

## Comment ça fonctionne

L'application utilise un prompt soigneusement conçu qui guide le modèle LLM pour:

1. Analyser la structure du code JSP
2. Identifier les éléments JSTL, expressions EL, scriptlets, etc.
3. Appliquer les règles de conversion appropriées
4. Générer le code Thymeleaf équivalent
5. Marquer les parties impossibles à convertir automatiquement

## Exemples

### Exemple de conversion JSP vers Thymeleaf

**Code JSP original**:
```jsp
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Liste des utilisateurs</title>
</head>
<body>
    <h1>Liste des utilisateurs</h1>
    <c:if test="${empty users}">
        <p>Aucun utilisateur trouvé</p>
    </c:if>
    <c:if test="${!empty users}">
        <ul>
            <c:forEach var="user" items="${users}">
                <li>
                    <a href="${pageContext.request.contextPath}/user/${user.id}">
                        <c:out value="${user.name}" />
                    </a>
                </li>
            </c:forEach>
        </ul>
    </c:if>
</body>
</html>
```

**Code Thymeleaf converti**:
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Liste des utilisateurs</title>
</head>
<body>
    <h1>Liste des utilisateurs</h1>
    <p th:if="${empty users}">Aucun utilisateur trouvé</p>
    <ul th:if="${!empty users}">
        <li th:each="user : ${users}">
            <a th:href="@{/user/{id}(id=${user.id})}" th:text="${user.name}"></a>
        </li>
    </ul>
</body>
</html>
```

## Contribution

Les contributions sont bienvenues ! N'hésitez pas à ouvrir une issue ou une pull request.
