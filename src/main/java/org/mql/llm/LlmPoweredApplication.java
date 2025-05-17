package org.mql.llm;

/**
 * Classe principale pour démontrer l'utilisation de la bibliothèque JSP to Thymeleaf Converter.
 * Cette classe remplace l'ancienne application Spring Boot.
 */
public class LlmPoweredApplication {
    
    public static void main(String[] args) {
        System.out.println("JSP to Thymeleaf Converter Library");
        System.out.println("=================================");
        System.out.println("Pour utiliser cette bibliothèque, créez une instance de Jsp2ThymeleafConverter.");
        System.out.println("Exemple: Jsp2ThymeleafConverter converter = new Jsp2ThymeleafConverter();");
        System.out.println("         String thymeleafCode = converter.convert(jspCode);");
        
        // Exemple d'utilisation
        Jsp2ThymeleafConverter converter = new Jsp2ThymeleafConverter();
        String jspCode = "<%@ page language=\"java\" contentType=\"text/html; charset=UTF-8\" pageEncoding=\"UTF-8\"%>\n" +
                          "<h1>Hello ${user.name}!</h1>";
        
        try {
            String thymeleafCode = converter.convert(jspCode);
            System.out.println("\nExemple de conversion:");
            System.out.println("-----------------------");
            System.out.println("JSP original:");
            System.out.println(jspCode);
            System.out.println("\nThymeleaf converti:");
            System.out.println(thymeleafCode);
        } catch (Exception e) {
            System.err.println("Erreur lors de la conversion: " + e.getMessage());
        }
    }
}
