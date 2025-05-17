package org.mql.llm.config;

/**
 * Classe responsable de la gestion hiérarchique des clés API.
 * Recherche la clé API dans l'ordre suivant:
 * 1. Variable d'environnement
 * 2. Propriété système
 * 3. Fichier de propriétés
 * 4. Valeur configurée
 * 5. Clé API par défaut
 */
public class ApiKeyProvider {

    private static final String ENV_VAR_NAME = "GEMINI_API_KEY";
    private static final String SYSTEM_PROPERTY_NAME = "gemini.api.key";
    private static final String CONFIG_PROPERTY_NAME = "gemini.api.key";
    private static final String DEFAULT_API_KEY = "AIzaSyDrd6yle9q8tW70cMc1jkxRkjGUSDnfu3s";
    
    private String configuredApiKey;
    
    /**
     * Initialise le provider avec une clé API configurée (optionnelle).
     * 
     * @param configuredApiKey La clé API configurée (peut être null)
     */
    public ApiKeyProvider(String configuredApiKey) {
        this.configuredApiKey = configuredApiKey;
    }
    
    /**
     * Initialise le provider avec la clé API par défaut.
     */
    public ApiKeyProvider() {
        this(null);
    }
    
    /**
     * Récupère la clé API selon l'ordre de priorité défini.
     * 
     * @return La clé API à utiliser
     */
    public String getApiKey() {
        // 1. Vérifier la variable d'environnement
        String apiKey = System.getenv(ENV_VAR_NAME);
        if (apiKey != null && !apiKey.isEmpty()) {
            return apiKey;
        }
        
        // 2. Vérifier la propriété système
        apiKey = System.getProperty(SYSTEM_PROPERTY_NAME);
        if (apiKey != null && !apiKey.isEmpty()) {
            return apiKey;
        }
        
        // 3. Vérifier dans le fichier de propriétés
        try {
            Class.forName("org.mql.llm.config.ConfigLoader");
            apiKey = ConfigLoader.getProperty(CONFIG_PROPERTY_NAME);
            if (apiKey != null && !apiKey.isEmpty()) {
                return apiKey;
            }
        } catch (ClassNotFoundException e) {
            // ConfigLoader n'est pas disponible, continuer avec les autres sources
        }
        
        // 4. Vérifier la valeur configurée
        if (configuredApiKey != null && !configuredApiKey.isEmpty()) {
            return configuredApiKey;
        }
        
        // 5. Utiliser la clé API par défaut
        return DEFAULT_API_KEY;
    }
    
    /**
     * Configure une nouvelle clé API.
     * 
     * @param apiKey La nouvelle clé API à configurer
     */
    public void setConfiguredApiKey(String apiKey) {
        this.configuredApiKey = apiKey;
    }
}