package org.mql.llm.services;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service spécialisé pour la conversion de code JSP vers Thymeleaf
 * utilisant les modèles LLM pour transformer intelligemment le code.
 */
public class JspToThymeleafService {
    private final GeminiService geminiService;
    private final String promptTemplate;

    /**
     * Initialise le service avec le service Gemini pour la conversion.
     * Configure également le modèle de prompt utilisé pour guider l'IA.
     * 
     * @param geminiService Service d'accès à l'API Gemini
     */
    public JspToThymeleafService(GeminiService geminiService) {
        this.geminiService = geminiService;

        // Définition du prompt template directement dans le code
        this.promptTemplate = "Tu es un expert en développement Java et en frameworks web, spécialisé dans la migration de JSP vers Thymeleaf. "
                + "Ta mission est de convertir le code JSP que je vais te soumettre en code Thymeleaf équivalent selon les spécifications suivantes:"
                + "\n\n# Règles de conversion JSP vers Thymeleaf:"
                + "\n\n## Éléments à convertir systématiquement:"
                + "\n- Remplacer `<%= expression %>` par `th:text=\"${expression}\"`"
                + "\n- Convertir `${pageContext.request.contextPath}` en `@{/}`"
                + "\n- Transformer `<c:if test=\"condition\">` en `th:if=\"${condition}\"`"
                + "\n- Transformer `<c:forEach var=\"item\" items=\"collection\">` en `th:each=\"item : ${collection}\"`"
                + "\n- Remplacer `<c:out value=\"expression\"/>` par `th:text=\"${expression}\"`"
                + "\n- Convertir `<c:choose>`, `<c:when>`, `<c:otherwise>` en utilisant `th:if`, `th:unless`, `th:switch`, et `th:case`"
                + "\n- Transformer les inclusions `<jsp:include page=\"...\"/>` en `th:insert` ou `th:replace`"
                + "\n- Remplacer les EL expressions comme `${variable}` en maintenant la même syntaxe `${variable}` (Thymeleaf utilise la même syntaxe)"
                + "\n\n## Éléments impossibles à convertir automatiquement:"
                + "\nPour chaque élément impossible à convertir automatiquement, encadre-le avec des commentaires HTML dans le format suivant:"
                + "\n<!--"
                + "\n❌ **[Type d'élément]** → nécessite réécriture manuelle❌"
                + "\n[Code JSP original]"
                + "\n-->"
                + "\n\nExemples d'éléments impossibles à convertir:"
                + "\n- Scriptlets JSP (`<% code %>`)"
                + "\n- Déclarations JSP (`<%! code %>`)"
                + "\n- Directives JSP complexes (`<%@ directive %>`)"
                + "\n- Code Java intégré dans des scriptlets"
                + "\n- Expressions EL complexes qui dépendent de fonctions JSP personnalisées"
                + "\n- Tags personnalisés spécifiques à votre application"
                + "\n\n## Format de sortie:"
                + "\n1. Commence par une déclaration HTML5 et inclus les attributs Thymeleaf nécessaires dans la balise HTML: `<html xmlns:th=\"http://www.thymeleaf.org\">`"
                + "\n2. Ajoute les namespaces supplémentaires si nécessaire (layout, etc.)"
                + "\n3. Assure-toi que toutes les balises sont correctement fermées (Thymeleaf est plus strict que JSP)"
                + "\n4. Fournir le code converti avec une indentation propre et cohérente"
                + "\n5. Ajouter en commentaire les explications pour les transformations complexes"
                + "\n\n## Note importante:"
                + "\nSi tu rencontres des constructions JSP complexes (comme des taglibs spécifiques) que tu ne peux pas convertir avec certitude, signale-les clairement dans les commentaires selon le format spécifié."
                + "\n\n## INSTRUCTIONS DE FORMATAGE IMPORTANTES:"
                + "\nRetourne uniquement le code HTML/Thymeleaf converti sans aucun autre commentaire, texte d'explication ou balise de formatage Markdown (comme ```html ou ```)."
                + "\n\nTransforme maintenant le code JSP suivant en Thymeleaf équivalent:";
    }

    /**
     * Convertit le code JSP en Thymeleaf équivalent.
     * 
     * @param jspCode Code JSP à convertir
     * @return Code Thymeleaf résultant
     */
    public String convertJspToThymeleaf(String jspCode) {
        // Prétraitement du code JSP pour améliorer la conversion
        String preprocessedJsp = preprocessJspCode(jspCode);

        // Construction du prompt et conversion
        String prompt = buildPrompt(preprocessedJsp);
        String conversion = processConversion(prompt);

        // Nettoyage et validation du résultat
        String cleanedHtml = cleanConvertedHtml(conversion);
        return cleanedHtml;
    }

    /**
     * Prétraite le code JSP pour faciliter sa conversion.
     * Identifie et marque les structures complexes pour un traitement spécial.
     *
     * @param jspCode Code JSP original
     * @return Code JSP prétraité
     */
    private String preprocessJspCode(String jspCode) {
        if (jspCode == null || jspCode.isEmpty()) {
            return jspCode;
        }

        // Marquer les scriptlets pour une attention particulière
        String marked = jspCode.replaceAll("<%\\s(?!@|=|!)(.*?)%>",
                "<!-- COMPLEX_SCRIPTLET_START --><%$1%><!-- COMPLEX_SCRIPTLET_END -->");

        // Marquer les déclarations JSP
        marked = marked.replaceAll("<%!\\s(.*?)%>",
                "<!-- DECLARATION_START --><%!$1%><!-- DECLARATION_END -->");

        // Normaliser les expressions EL pour faciliter leur conversion
        // Solution : échapper le $ dans la chaîne de remplacement
        marked = marked.replaceAll("\\$\\{pageContext\\.request\\.contextPath\\}([^}]*?)\\}",
                "\\${pageContext.request.contextPath}$1}<!-- CONTEXT_PATH -->");

        return marked;
    }

    /**
     * Construit le prompt complet avec les instructions et le code JSP.
     * 
     * @param jspCode Code JSP à inclure dans le prompt
     * @return Prompt complet
     */
    private String buildPrompt(String jspCode) {
        return promptTemplate + "\n\n" + jspCode;
    }

    /**
     * Traite la demande de conversion via le service LLM.
     * 
     * @param prompt Prompt complet pour le LLM
     * @return Résultat de la conversion
     */
    private String processConversion(String prompt) {
        try {
            Map<String, String> response = geminiService.processJspConversion(prompt);
            if (response.containsKey("error")) {
                return "Erreur: " + response.get("error");
            }
            return response.getOrDefault("conversion", "Erreur: Pas de résultat de conversion");
        } catch (Exception e) {
            return "Erreur lors de la conversion: " + e.getMessage();
        }
    }

    /**
     * Nettoie le code HTML/Thymeleaf des balises de formatage Markdown et autres
     * artefacts.
     * 
     * @param html Code HTML/Thymeleaf potentiellement avec des balises de formatage
     * @return Code HTML/Thymeleaf nettoyé
     */
    private String cleanConvertedHtml(String html) {
        if (html == null || html.isEmpty()) {
            return html;
        }

        // Supprimer les blocs de code Markdown (```html et ```)
        String cleaned = html.replaceAll("```html", "").replaceAll("```", "");

        // Extraire le HTML du bloc de code si présent
        Pattern pattern = Pattern.compile("(?s).*?(<html.*?>.*</html>).*?", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(cleaned);
        if (matcher.matches()) {
            cleaned = matcher.group(1);
        }

        // Enlever les espaces/sauts de ligne au début et à la fin
        cleaned = cleaned.trim();

        // Valider la syntaxe Thymeleaf
        if (!validateThymeleafOutput(cleaned)) {
            // Log d'avertissement si la validation échoue
            System.out.println(
                    "AVERTISSEMENT: La validation du code Thymeleaf a échoué. Le résultat peut contenir des erreurs.");
        }

        return cleaned;
    }

    /**
     * Valide que le code Thymeleaf généré est syntaxiquement correct.
     * Vérifie les erreurs de syntaxe basiques comme les balises non fermées
     * et les attributs Thymeleaf mal formés.
     *
     * @param thymeleafCode Code Thymeleaf à valider
     * @return true si le code passe les validations de base, false sinon
     */
    private boolean validateThymeleafOutput(String thymeleafCode) {
        if (thymeleafCode == null || thymeleafCode.isEmpty()) {
            return false;
        }

        // Validation 1: Vérifier que chaque balise ouvrante a une fermante
        boolean hasOpenHtmlTag = thymeleafCode.contains("<html");
        boolean hasCloseHtmlTag = thymeleafCode.contains("</html>");

        if (hasOpenHtmlTag != hasCloseHtmlTag) {
            return false;
        }

        // Validation 2: Vérifier les attributs Thymeleaf
        Pattern thAttributePattern = Pattern.compile("th:[a-z]+=\"[^\"]*\"");
        Matcher thAttributeMatcher = thAttributePattern.matcher(thymeleafCode);

        // Validation 3: Vérifier que les namespaces Thymeleaf sont définis
        boolean hasThNamespace = thymeleafCode.contains("xmlns:th=\"http://www.thymeleaf.org\"");

        // Une validation plus complète nécessiterait un parser HTML/XML

        return hasOpenHtmlTag && hasCloseHtmlTag && (thAttributeMatcher.find() || !thymeleafCode.contains("th:")) &&
                (hasThNamespace || !thymeleafCode.contains("th:"));
    }
}
