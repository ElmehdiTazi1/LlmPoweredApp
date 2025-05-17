package org.mql.llm.examples;

import org.mql.llm.Jsp2ThymeleafConverter;

/**
 * Classe simple pour tester l'API de conversion JSP vers Thymeleaf.
 */
public class SimpleTestMain {

    public static void main(String[] args) {
        // Exemple de code JSP à convertir
        String jspCode = "<%@ page language=\"java\" contentType=\"text/html; charset=UTF-8\" pageEncoding=\"UTF-8\"%>\n"
                + "<%@ taglib prefix=\"c\" uri=\"http://java.sun.com/jsp/jstl/core\" %>\n"
                + "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<head>\n"
                + "    <title>Test JSP</title>\n"
                + "    <link href=\"<c:url value='/css/style.css'/>\" rel=\"stylesheet\">\n"
                + "</head>\n"
                + "<body>\n"
                + "    <h1>Bonjour ${user.name}!</h1>\n"
                + "    <c:if test=\"${user.admin}\">\n"
                + "        <div class=\"admin-panel\">\n"
                + "            <h2>Panel administrateur</h2>\n"
                + "        </div>\n"
                + "    </c:if>\n"
                + "    <nav>\n"
                + "        <a href=\"${pageContext.request.contextPath}/home\">Accueil</a>\n"
                + "        <a href=\"${pageContext.request.contextPath}/logout\">Déconnexion</a>\n"
                + "    </nav>\n"
                + "    <footer>&copy; 2025</footer>\n"
                + "</body>\n"
                + "</html>";

        System.out.println("=== CODE JSP ORIGINAL ===");
        System.out.println(jspCode);
        System.out.println("\n=== CONVERSION EN COURS... ===\n");

        try {
            // Création de l'instance du convertisseur
            Jsp2ThymeleafConverter converter = new Jsp2ThymeleafConverter();
            
            // Conversion du code JSP en Thymeleaf
            String thymeleafCode = converter.convert(jspCode);
            
            // Affichage du résultat
            System.out.println("=== CODE THYMELEAF CONVERTI ===");
            System.out.println(thymeleafCode);
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la conversion: " + e.getMessage());
            e.printStackTrace();
        }
    }
}