package bookshop.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * CORS Configuration to allow frontend React app to communicate with the backend
 * 
 * Configured Endpoints:
 * - /api/products/**      - Product management (GET, POST, PUT, DELETE)
 * - /api/users/**         - User management and registration
 * - /api/inventory/**     - Inventory management and stock tracking
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // Allow credentials (cookies, authorization headers)
        config.setAllowCredentials(true);
        
        // Allowed origins - React development server and production
        config.setAllowedOrigins(Arrays.asList(
            "http://localhost:3000",           // React dev server
            "http://localhost:3001",           // Alternative React dev port
            "http://127.0.0.1:3000"            // Alternative localhost format
        ));
        
        // Allowed headers - including common REST headers
        config.setAllowedHeaders(Arrays.asList(
            "Origin",
            "Content-Type",
            "Accept",
            "Authorization",
            "X-Requested-With",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers"
        ));
        
        // Exposed headers - headers that the browser can access
        config.setExposedHeaders(Arrays.asList(
            "Access-Control-Allow-Origin",
            "Access-Control-Allow-Credentials"
        ));
        
        // Allowed HTTP methods for all endpoints
        config.setAllowedMethods(Arrays.asList(
            "GET",
            "POST",
            "PUT",
            "DELETE",
            "OPTIONS",
            "PATCH"
        ));
        
        // Cache preflight response for 1 hour
        config.setMaxAge(3600L);
        
        // Register CORS configuration for all API endpoints
        // Products: /api/products, /api/products/{id}, /api/products/search, /api/products/category/{categoryId}
        source.registerCorsConfiguration("/api/products/**", config);
        
        // Users: /api/users, /api/users/{id}, /api/users/register, /api/users/email/{email}
        source.registerCorsConfiguration("/api/users/**", config);
        
        // Inventory: /api/inventory, /api/inventory/{productId}, /api/inventory/{productId}/quantity
        source.registerCorsConfiguration("/api/inventory/**", config);
        
        // Catch-all for any other API endpoints
        source.registerCorsConfiguration("/api/**", config);
        
        return new CorsFilter(source);
    }
}
