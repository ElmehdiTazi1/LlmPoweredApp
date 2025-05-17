package org.mql.llm.examples;

import org.mql.llm.Jsp2ThymeleafConverter;

/**
 * Exemple d'utilisation simple de la bibliothèque JSP to Thymeleaf Converter.
 * Classe de démonstration pour l'utilisation simple de Jsp2ThymeleafConverter.
 * Fournit un exemple concret d'exécution de la bibliothèque.
 */
public class Jsp2ThymeleafExample {
    
    /**
     * Point d'entrée principal pour la démonstration.
     * Exécute un exemple de conversion simple.
     * 
     * @param args Arguments de la ligne de commande (non utilisés)
     */
    public static void main(String[] args) {
        System.out.println("JSP to Thymeleaf Converter Library - Example");
        System.out.println("==========================================");
        
        // Exemple d'utilisation
        Jsp2ThymeleafConverter converter = new Jsp2ThymeleafConverter();
        String jspCode = "<%@ page language=\"java\" contentType=\"text/html; charset=UTF-8\" pageEncoding=\"UTF-8\"%>\n" +
                          "<%@ taglib prefix=\"c\" uri=\"http://java.sun.com/jsp/jstl/core\" %>\n" +
                          "<!DOCTYPE html>\n" +
                          "<html>\n" +
                          "<head>\n" +
                          "    <title>Hello JSP</title>\n" +
                          "</head>\n" +
                          "<body>\n" +
                          "    <h1>Hello, ${user.name}!</h1>\n" +
                          "    <c:if test=\"${user.admin}\">\n" +
                          "        <p>You are an admin.</p>\n" +
                          "    </c:if>\n" +
                          "    <ul>\n" +
                          "        <c:forEach var=\"item\" items=\"${items}\">\n" +
                          "            <li>${item.name} - ${item.price}</li>\n" +
                          "        </c:forEach>\n" +
                          "    </ul>\n" +
                          "</body>\n" +
                          "</html>";
        
        try {
            String thymeleafCode = converter.convert(jspCode);
            System.out.println("\nExemple de conversion:");
            System.out.println("-----------------------");
            System.out.println("JSP original:");
            System.out.println(jspCode);
            System.out.println("\nThymeleaf converti:");
            System.out.println(thymeleafCode);
            
            // Exemple avec détails
            System.out.println("\nExemple de conversion avec détails:");
            System.out.println("----------------------------------");
            var result = converter.convertWithDetails(jspCode, true);
            System.out.println("Succès: " + result.get("success"));
            System.out.println("Thymeleaf: " + result.get("thymeleafCode").toString().substring(0, 50) + "...");
        } catch (Exception e) {
            System.err.println("Erreur lors de la conversion: " + e.getMessage());
        }
    }
}
