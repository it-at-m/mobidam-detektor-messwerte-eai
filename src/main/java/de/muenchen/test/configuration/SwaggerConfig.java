package de.muenchen.test.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;

@Configuration
public class SwaggerConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/swagger-ui/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    private final String authServer;
    private final String realm;

    @Value("${info.application.version}")
    private String buildVersion;

    @Autowired
    public SwaggerConfig(
            @Value("${SSO_BASE_URL}") final String authServer,
            @Value("${SSO_REALM}") final String realm) {
        this.authServer = authServer;
        this.realm = realm;
    }

    @Bean
    public OpenAPI openAPI() {
        final String authUrl = String.format("%s/auth/realms/%s/protocol/openid-connect", this.authServer, this.realm);
        return new OpenAPI()
                .components(
                        new Components()
                                .addSecuritySchemes("spring_oauth", new SecurityScheme()
                                        .type(SecurityScheme.Type.OAUTH2)
                                        .description("OAuth2 flow")
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .flows(new OAuthFlows()
                                                .clientCredentials(new OAuthFlow()
                                                        .authorizationUrl(authUrl + "/auth")
                                                        //                                                        .refreshUrl(authUrl + "/token")
                                                        .tokenUrl(authUrl + "/token")
                                                        .scopes(new Scopes().addString("lhm_extended", "lhm_extended"))))))
                .security(Collections.singletonList(
                        new SecurityRequirement().addList("spring_oauth")))
                .info(new Info()
                        .title("MobidaM - Detektor-Messwerte-EAI")
                        .version(this.buildVersion)
                        .description("MobidaM - Mobilitätsdatenplattform der LH München")
                        .contact(new Contact()
                                .name("MobidaM")
                                .email("svc-mobidam@muenchen.de")))
                .externalDocs(new ExternalDocumentation()
                        .description("Externe Dokumentation auf unserer Confluence-Seite")
                        .url("https://confluence.muenchen.de/display/MOR01069/"));
    }

    @Bean
    @Profile("!prod")
    public String[] whitelist() {
        return new String[] {
                // -- swagger ui
                "/v2/api-docs",
                "/v3/api-docs/**",
                "/swagger-resources/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
        };
    }

    @Bean
    @Profile("prod")
    public String[] whitelistProd() {
        return new String[] {};
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // Allow anyone and anything access. Probably ok for Swagger spec
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("<OAUTH-SERVER>");
        config.addAllowedHeader("*");
        config.addAllowedMethod("POST");

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}
