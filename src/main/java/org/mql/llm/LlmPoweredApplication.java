package org.mql.llm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class LlmPoweredApplication {

	public static void main(String[] args) {
		SpringApplication.run(LlmPoweredApplication.class, args);
	}
	
	/**
	 * Configuration pour les ressources statiques et l'interface utilisateur
	 */
	@Configuration
	public static class WebConfig implements WebMvcConfigurer {
	    
	    /**
	     * Configure la page d'accueil pour afficher l'interface de conversion JSP vers Thymeleaf
	     */
	    @Override
	    public void addViewControllers(ViewControllerRegistry registry) {
	        registry.addViewController("/").setViewName("redirect:/index.html");
	    }
	    
	    /**
	     * Configure les gestionnaires de ressources pour servir les fichiers statiques
	     */
	    @Override
	    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	        registry.addResourceHandler("/**")
	            .addResourceLocations("classpath:/static/");
	    }
	}

}
