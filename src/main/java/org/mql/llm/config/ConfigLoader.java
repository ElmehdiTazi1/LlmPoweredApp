package org.mql.llm.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utilitaire pour charger les propriétés de configuration depuis le fichier de propriétés.
 */
public class ConfigLoader {
    
    private static final String PROPERTIES_FILE = "jsp2thymeleaf.properties";
    private static Properties properties;
    
    /**
     * Charge les propriétés depuis le fichier de propriétés.
     * 
     * @return Properties chargées ou null si erreur
     */
    public static Properties loadProperties() {
        if (properties != null) {
            return properties;
        }
        
        properties = new Properties();
        
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (input != null) {
                properties.load(input);
                return properties;
            } else {
                System.err.println("Impossible de trouver le fichier " + PROPERTIES_FILE);
                return null;
            }
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement des propriétés: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Récupère une propriété par sa clé.
     * 
     * @param key Clé de la propriété
     * @return Valeur de la propriété ou null si non trouvée
     */
    public static String getProperty(String key) {
        if (properties == null) {
            loadProperties();
        }
        
        return properties != null ? properties.getProperty(key) : null;
    }
    
    /**
     * Récupère une propriété par sa clé avec une valeur par défaut.
     * 
     * @param key Clé de la propriété
     * @param defaultValue Valeur par défaut si la propriété n'existe pas
     * @return Valeur de la propriété ou la valeur par défaut si non trouvée
     */
    public static String getProperty(String key, String defaultValue) {
        if (properties == null) {
            loadProperties();
        }
        
        return properties != null ? properties.getProperty(key, defaultValue) : defaultValue;
    }
}
