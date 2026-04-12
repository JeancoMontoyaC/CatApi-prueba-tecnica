package com.app.catapi.infrastructure.security.filter;
import com.app.catapi.infrastructure.security.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private UserDetails userDetails;

    private static final String TEST_SECRET = Encoders.BASE64.encode(
            Keys.hmacShaKeyFor(new byte[32]).getEncoded()
    );

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService, "SECRET_KEY", TEST_SECRET);

        userDetails = User.builder()
                .username("testuser@mail.com")
                .password("password")
                .authorities(new SimpleGrantedAuthority("ROLE_USER"))
                .build();
    }

    @Test
    void generateToken_shouldReturnNonNullToken() {
        String token = jwtService.generateToken(userDetails);
        assertThat(token).isNotNull().isNotBlank();
    }

    @Test
    void generateToken_shouldContainUsername() {
        String token = jwtService.generateToken(userDetails);
        assertThat(jwtService.getUsername(token)).isEqualTo("testuser@mail.com");
    }

    @Test
    void generateToken_withClaims_shouldReturnValidToken() {
        Map<String, Object> claims = Map.of("role", "ADMIN");
        String token = jwtService.generateToken(claims, "admin@mail.com");
        assertThat(jwtService.getUsername(token)).isEqualTo("admin@mail.com");
    }

    @Test
    void getUsername_shouldReturnCorrectSubject() {
        String token = jwtService.generateToken(userDetails);
        assertThat(jwtService.getUsername(token)).isEqualTo(userDetails.getUsername());
    }

    @Test
    void getUsername_shouldThrow_forMalformedToken() {
        assertThatThrownBy(() -> jwtService.getUsername("not.a.valid.jwt"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Invalid JWT token");
    }

    @Test
    void isTokenExpired_shouldReturnFalse_forFreshToken() {
        String token = jwtService.generateToken(userDetails);
        assertThat(jwtService.isTokenExpired(token)).isFalse();
    }

    @Test
    void isTokenExpired_shouldReturnTrue_forExpiredToken() {
        String expiredToken = Jwts.builder()
                .subject("testuser@mail.com")
                .expiration(new Date(System.currentTimeMillis() - 1000))
                .issuedAt(new Date(System.currentTimeMillis() - 5000))
                .signWith(Keys.hmacShaKeyFor(java.util.Base64.getDecoder().decode(TEST_SECRET)))
                .compact();

        assertThat(jwtService.isTokenExpired(expiredToken)).isTrue();
    }

    @Test
    void canBeTokenRenewed_shouldReturnTrue_forFreshToken() {
        String token = jwtService.generateToken(userDetails);
        assertThat(jwtService.canBeTokenRenewed(token)).isTrue();
    }

    @Test
    void isValidToken_shouldReturnTrue_whenUsernameMatches() {
        String token = jwtService.generateToken(userDetails);
        assertThat(jwtService.isValidToken(token, userDetails)).isTrue();
    }

    @Test
    void isValidToken_shouldReturnFalse_whenUsernameDoesNotMatch() {
        String token = jwtService.generateToken(userDetails);

        UserDetails otherUser = User.builder()
                .username("other@mail.com")
                .password("password")
                .authorities(List.of())
                .build();

        assertThat(jwtService.isValidToken(token, otherUser)).isFalse();
    }

    @Test
    void renewToken_shouldReturnNewToken_whenTokenIsValid() {
        String token = jwtService.generateToken(userDetails);
        String newToken = jwtService.renewToken(token, userDetails);

        assertThat(newToken).isNotNull().isNotEqualTo(token);
        assertThat(jwtService.getUsername(newToken)).isEqualTo(userDetails.getUsername());
    }

    @Test
    void renewToken_shouldThrow_whenTokenCannotBeRenewed() {
        String farFutureToken = Jwts.builder()
                .subject("testuser@mail.com")
                .expiration(new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 3)))
                .issuedAt(new Date())
                .signWith(Keys.hmacShaKeyFor(java.util.Base64.getDecoder().decode(TEST_SECRET)))
                .compact();

        assertThatThrownBy(() -> jwtService.renewToken(farFutureToken, userDetails))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Invalid token");
    }
}
