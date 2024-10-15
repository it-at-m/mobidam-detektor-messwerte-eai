package de.muenchen.test.security;

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
class JwtUserInfoAuthenticationConverterTest {

    private JwtUserInfoAuthenticationConverter converter = new JwtUserInfoAuthenticationConverter();

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
