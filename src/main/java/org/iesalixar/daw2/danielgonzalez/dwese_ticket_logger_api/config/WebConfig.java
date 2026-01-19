package org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Clase de configuración para habilitar la gestión de recursos estáticos en Spring MVC.
 * Permite servir archivos desde un directorio externo utilizando las propiedades cargadas
 * desde el archivo .env.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);

    /**
     * Ruta de subida de archivos. Se inyecta desde la variable de entorno UPLOAD_PATH
     * definida en el .env.
     */
    @Value("${UPLOAD_PATH}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (uploadPath != null && !uploadPath.isEmpty()) {
            // Asegurarse de que las barras sean compatibles con Java (Windows)
            String normalizedPath = uploadPath.replace("\\", "/");
            logger.info("UPLOAD_PATH configurado correctamente: {}", normalizedPath);

            // Configurar Spring para servir archivos desde la ruta obtenida
            registry.addResourceHandler("/uploads/**")
                    .addResourceLocations("file:" + normalizedPath + "/");
        } else {
            logger.error("La variable de entorno UPLOAD_PATH no está configurada o está vacía.");
        }
    }
}
