package de.muenchen.test.security;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.LinkedHashMap;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class JwtUserInfoAuthenticationConverterTest {

    private JwtUserInfoAuthenticationConverter converter = new JwtUserInfoAuthenticationConverter();

    @Test
    void convertWithExistingClaimPathFromRealToken() {
        final var tokenValue = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJFaFNhRnhiWXdfUExmNm5XeWVJc0Nkdkl2VDJ4X09FTU9QR2ZDM2ZETE0wIn0.eyJleHAiOjE3Mjg5OTQ1NjksImlhdCI6MTcyODk5NDI2OSwianRpIjoiNzY0NWM5ODctZjRkMC00MWNmLThkNDctNTYwMzRmMThmOTZmIiwiaXNzIjoiaHR0cHM6Ly9zc290ZXN0Lm11ZW5jaGVuLmRlL2F1dGgvcmVhbG1zL2ludHJhcCIsImF1ZCI6WyJtb2JpZGFtLXZlcmtlaHJzZGV0ZWt0b3ItZWFpIiwic3RhcGVsdWViZXJsYXVmIiwiYWNjb3VudCJdLCJzdWIiOiJlYmE3NTZlNC01YTRjLTQ5ZjYtYTJiNi0xYWUwYjllZDU3MWUiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJkYXZlLmVhaS5tb2JpZGFtIiwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iLCJkZWZhdWx0LXJvbGVzLWludHJhcCJdfSwicmVzb3VyY2VfYWNjZXNzIjp7Im1vYmlkYW0tdmVya2VocnNkZXRla3Rvci1lYWkiOnsicm9sZXMiOlsidmVya2VocnNkZXRla3Rvci12aWV3ZXIiXX0sInN0YXBlbHVlYmVybGF1ZiI6eyJyb2xlcyI6WyJsaG0tYWItc3RhcGVsdWViZXJsYXVmLXVzZXIiXX0sImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoiTEhNIiwiY2xpZW50SWQiOiJkYXZlLmVhaS5tb2JpZGFtIiwiY2xpZW50SG9zdCI6IjEwLjE2Ni44NS41NyIsImNsaWVudEFkZHJlc3MiOiIxMC4xNjYuODUuNTciLCJ1c2VybmFtZSI6InNlcnZpY2UtYWNjb3VudC1kYXZlLmVhaS5tb2JpZGFtIn0.mHUiA_Wr2O0mmNO3by9KPPnFQ42rVyNzvHKBl3YlstJlHv8WZu1wa6UOzQvgYdqcwN76gdM-OINPu3oGF08_peMyOAI1ZXzT0vOPhb-VgeUtEDr7g6c-MaisCMI02enexeS0mSgR-xnHwsZm6l7vJR79aRFS3SN0IL13L9y31SGGLcZkriU9plbPbfhQg8HoCETnKsrPm-7AU5zBN8jaft-3EmspcORE7IsqxWILeLWeYTcq05PhsFA55VwsGI8e1dbfYgRq10sI9PAiJX23B2SOBjQE2LD0eDyRkcFuO8qRfLknqofY5tJfyJLleLJqBgzxO3ukiOjqvG3oLUL36ZEQ2Pkeclq8yRYa5CRZAlXPlAL9sg4MKRH_6WyQfneFy-NU8k83qFzghcaOPcVsjaD0GwyUT78Z_8x4_3dUZK3EXZQ83hlruVL7FzJ7S9swbasBc0P3HkdcSc89ZxXuGkf0Bm0y17zETFw-Q8OvRq8j8md8ZsGXxncQjNJByvj6zofnGxIBcJ4kwuKBCX5VjVzSOvOxUMRsUX_Vp4A6MMTN6g0jy7nuf6KHMQkaQhckckXqozLwv2OZtvfyjtiE3Xv1qSi4yhLeZVt2JBngtL344El6w089aDtaqNlQ3W_A7J0JLDKQ9nATreH-zhAdy820-9WYZKiha4aJN_-zv8s";
        final var jwtDecoder = (NimbusJwtDecoder) JwtDecoders.fromIssuerLocation("https://ssotest.muenchen.de/auth/realms/intrap");

        final var tokenValidator = new OAuth2TokenValidator<Jwt>() {

            public OAuth2TokenValidatorResult validate(Jwt token) {
                return OAuth2TokenValidatorResult.success();
            }

        };
        jwtDecoder.setJwtValidator(tokenValidator);

        final var jwt = jwtDecoder.decode(tokenValue);

        final var result = converter.convert(jwt);

        final var expected = new JwtAuthenticationToken(jwt, List.of(new SimpleGrantedAuthority("ROLE_verkehrsdetektor-viewer")));

        Assertions.assertThat(result).isEqualTo(expected);
    }

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
