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
package de.muenchen.test.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * The central class for configuration of all security aspects.
 */
@Configuration
@Profile("!no-security")
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@Import(RestTemplateAutoConfiguration.class)
public class SecurityConfiguration {

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Value("${security.oauth2.resource.user-info-uri}")
    private String userInfoUri;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests.requestMatchers(
                        // allow access to /swagger-ui
                        AntPathRequestMatcher.antMatcher("/swagger-ui/*"),
                        // allow acces to api-docs
                        AntPathRequestMatcher.antMatcher("/v3/api-docs"),
                        AntPathRequestMatcher.antMatcher("/v3/api-docs/*"),
                        // allow access to /actuator/info
                        AntPathRequestMatcher.antMatcher("/actuator/info"),
                        // allow access to /actuator/health for OpenShift Health Check
                        AntPathRequestMatcher.antMatcher("/actuator/health"),
                        // allow access to /actuator/health/liveness for OpenShift Liveness Check
                        AntPathRequestMatcher.antMatcher("/actuator/health/liveness"),
                        // allow access to /actuator/health/readiness for OpenShift Readiness Check
                        AntPathRequestMatcher.antMatcher("/actuator/health/readiness"),
                        // allow access to /actuator/metrics for Prometheus monitoring in OpenShift
                        AntPathRequestMatcher.antMatcher("/actuator/metrics"))
                        .permitAll())
                .authorizeHttpRequests((requests) -> requests.requestMatchers("/**")
                        .authenticated())
                .oauth2ResourceServer(httpSecurityOAuth2ResourceServerConfigurer -> httpSecurityOAuth2ResourceServerConfigurer
                        .jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(new JwtUserInfoAuthenticationConverter(
                                new UserInfoAuthoritiesService(userInfoUri, restTemplateBuilder)))));

        return http.build();
    }

}
