/*
 * The MIT License
 * Copyright © 2023 Landeshauptstadt München | it@M
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package de.muenchen.mobidam.configuration;

import de.muenchen.mobidam.security.MobidamJwtAuthenticationConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * The central class for configuration of all security aspects.
 */
@Configuration
@Profile("!no-security")
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@Import(RestTemplateAutoConfiguration.class)
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final String[] whitelist;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // @formatter:off
        return http
                .authorizeHttpRequests(request -> request
                        .requestMatchers(getPathMatchersForPermitAll()).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/**")).authenticated())
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(new MobidamJwtAuthenticationConverter()))
                )
                .build();
        // @formatter:on
    }

    private AntPathRequestMatcher[] getPathMatchersForPermitAll() {
        return Stream
                .concat(
                        Stream.of(
                                // allow access to /actuator/info
                                AntPathRequestMatcher.antMatcher("/actuator/info"),
                                // allow access to /actuator/health for OpenShift Health Check
                                AntPathRequestMatcher.antMatcher("/actuator/health"),
                                // allow access to /actuator/health/liveness for OpenShift Liveness Check
                                AntPathRequestMatcher.antMatcher("/actuator/health/liveness"),
                                // allow access to /actuator/health/readiness for OpenShift Readiness Check
                                AntPathRequestMatcher.antMatcher("/actuator/health/readiness"),
                                // allow access to /actuator/metrics for Prometheus monitoring in OpenShift
                                AntPathRequestMatcher.antMatcher("/actuator/metrics")),
                        Arrays.stream(whitelist).map(AntPathRequestMatcher::antMatcher))
                .toArray(AntPathRequestMatcher[]::new);
    }

    /*
     * private CorsConfiguration corsConfiguration() {
     * CorsConfiguration corsConfig = new CorsConfiguration();
     * corsConfig.addAllowedOrigin("<OAUTH-SERVER>");
     * corsConfig.addAllowedHeader("*");
     * corsConfig.addAllowedMethod(HttpMethod.GET);
     * corsConfig.addAllowedMethod(HttpMethod.POST);
     * corsConfig.applyPermitDefaultValues();
     * corsConfig.setAllowCredentials(true);
     *
     * return corsConfig;
     * }
     *
     * @Bean
     *
     * @Primary
     * public CorsConfigurationSource corsConfigurationsrc() {
     * CorsConfiguration corsConfig = new CorsConfiguration();
     * corsConfig.setAllowedOrigins(List.of("<OAUTH-SERVER>"));
     * corsConfig.setAllowedMethods(List.of("GET", "POST"));
     * corsConfig.setAllowCredentials(true);
     *
     * UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
     * source.registerCorsConfiguration("/**", corsConfig);
     * return source;
     * }
     */
}
