<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Exemple JSP</title>
<link rel="stylesheet" href="/css/style.css"></head>
<body>
    <h1>Bienvenue, ${utilisateur.nom}!</h1>
    
    <c:if test="${utilisateur.admin}">
        <div class="admin-panel">
            <h2>Panneau d'administration</h2>
            <p>Vous avez accès à des fonctionnalités spéciales.</p>
        </div>
    </c:if>
    
    <div class="content">
        <h3>Liste des produits</h3>
        <c:forEach var="produit" items="${produits}">
            <div class="produit">
                <h4>${produit.nom}</h4>
                <p>Prix: ${produit.prix} €</p>
                <c:choose>
                    <c:when test="${produit.enStock}">
                        <p class="en-stock">En stock</p>
                    </c:when>
                    <c:otherwise>
                        <p class="rupture">Rupture de stock</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:forEach>
    </div>
    
    <footer>
        <p>&copy; ${annee} - Ma Boutique</p>
    </footer>
</body>
</html>
