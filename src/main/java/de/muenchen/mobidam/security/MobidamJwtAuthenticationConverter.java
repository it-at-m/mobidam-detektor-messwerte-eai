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
package de.muenchen.mobidam.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.muenchen.mobidam.domain.Constants;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Ein custom
 * {@link org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter},
 * der die Authorities ermittelt
 */
@Slf4j
public class MobidamJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    public static final String RESOURCE_ACCESS = "resource_access";

    /**
     * Die Methode extrahiert aus dem JWT die Rollen aus folgendem Claim-Pfad.
     *
     * - resource_access.mobidam-verkehrsdetektor-eai.roles
     *
     * @param source als JWT
     * @return den Token mit den Rollen als Authorties.
     */
    @Override
    public AbstractAuthenticationToken convert(final Jwt source) {
        final var authorities = new ArrayList<SimpleGrantedAuthority>();
        final var resourceAccessClaim = source.getClaimAsMap(RESOURCE_ACCESS);
        if (MapUtils.isNotEmpty(resourceAccessClaim)) {
            try {
                final var resourceAccess = new ObjectMapper().convertValue(resourceAccessClaim, ResourceAccess.class);
                final var roles = ObjectUtils.defaultIfNull(resourceAccess.getMobidamVerkehrsdetektorEai(), new MobidamVerkehrsdetektorEai()).getRoles();
                CollectionUtils.emptyIfNull(roles)
                        .stream()
                        .map(role -> StringUtils.prependIfMissing(role, Constants.ROLE_PREFIX))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toCollection(() -> authorities));
            } catch (Exception exception) {
                log.error("Folgende Resource ist nicht im Access-Token vorhanden: {}", "resource_access.mobidam-verkehrsdetektor-eai.roles");
            }
        }
        return new JwtAuthenticationToken(source, authorities);
    }

    /**
     * Bildet den folgenden Token-Claim ab:
     *
     * "resource_access": { "mobidam-verkehrsdetektor-eai": { "roles": [ "verkehrsdetektor-viewer" ] },
     * ... }, }
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class ResourceAccess {

        @JsonProperty("mobidam-verkehrsdetektor-eai")
        private MobidamVerkehrsdetektorEai mobidamVerkehrsdetektorEai;

    }

    /**
     * Bildet den folgenden Token-Claim ab:
     *
     * "mobidam-verkehrsdetektor-eai": { "roles": [ "verkehrsdetektor-viewer" ] }
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class MobidamVerkehrsdetektorEai {

        private List<String> roles;

    }

}
