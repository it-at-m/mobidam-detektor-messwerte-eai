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

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.LinkedHashMap;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MobidamJwtAuthenticationConverterTest {

    private MobidamJwtAuthenticationConverter converter = new MobidamJwtAuthenticationConverter();

    @Test
    void convertWithMissingResourceAccess() {
        final var jwt = Jwt
                .withTokenValue("test")
                .header("test", "test")
                .claim("test", new Object())
                .build();

        final var result = converter.convert(jwt);

        final var expected = new JwtAuthenticationToken(jwt, List.of());

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    void convertWithMissingMobidamVerkehrsdetektorEaiClaim() {
        final var notMobidamVerkehrsdetektorEai = new LinkedHashMap<>();
        notMobidamVerkehrsdetektorEai.put("test", new Object());

        final var jwt = Jwt
                .withTokenValue("test")
                .header("test", "test")
                .claim("resource_access", notMobidamVerkehrsdetektorEai)
                .build();

        final var result = converter.convert(jwt);

        final var expected = new JwtAuthenticationToken(jwt, List.of());

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    void convertWithMissingRoleClaim() {
        final var mobidamVerkehrsdetektorEai = new LinkedHashMap<>();
        final var notRoles = new LinkedHashMap<>();
        notRoles.put("test", new Object());
        mobidamVerkehrsdetektorEai.put("mobidam-verkehrsdetektor-eai", notRoles);

        final var jwt = Jwt
                .withTokenValue("test")
                .header("test", "test")
                .claim("resource_access", mobidamVerkehrsdetektorEai)
                .build();

        final var result = converter.convert(jwt);

        final var expected = new JwtAuthenticationToken(jwt, List.of());

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    void convertWithRoleClaim() {
        final var mobidamVerkehrsdetektorEai = new LinkedHashMap<>();
        final var notRoles = new LinkedHashMap<>();
        notRoles.put("roles", List.of("foo", "ROLE_bar"));
        mobidamVerkehrsdetektorEai.put("mobidam-verkehrsdetektor-eai", notRoles);

        final var jwt = Jwt
                .withTokenValue("test")
                .header("test", "test")
                .claim("resource_access", mobidamVerkehrsdetektorEai)
                .build();

        final var result = converter.convert(jwt);

        final var expected = new JwtAuthenticationToken(jwt, List.of(new SimpleGrantedAuthority("ROLE_foo"), new SimpleGrantedAuthority("ROLE_bar")));

        Assertions.assertThat(result).isEqualTo(expected);
    }

}
