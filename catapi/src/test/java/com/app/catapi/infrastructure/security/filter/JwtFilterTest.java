package com.app.catapi.infrastructure.security.filter;

import com.app.catapi.infrastructure.security.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtFilterTest {

    @Mock
    private JwtService jwtService;
    @Mock private UserDetailsService userDetailsService;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private FilterChain filterChain;

    @InjectMocks
    private JwtFilter jwtFilter;

    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        userDetails = User.builder()
                .username("jean")
                .password("password")
                .roles("USER")
                .build();
    }

    @Test
    @DisplayName("Should continue filter chain when Authorization header is missing")
    void shouldContinueChain_whenNoAuthorizationHeader() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService);
    }

    @Test
    @DisplayName("Should continue filter chain when Authorization header does not start with Bearer")
    void shouldContinueChain_whenAuthorizationHeaderIsInvalid() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Basic sometoken");

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService);
    }

    @Test
    @DisplayName("Should continue filter chain when token is expired and cannot be renewed")
    void shouldContinueChain_whenTokenExpiredAndCannotBeRenewed() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer expiredtoken");
        when(jwtService.isTokenExpired("expiredtoken")).thenReturn(true);
        when(jwtService.canBeTokenRenewed("expiredtoken")).thenReturn(false);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(jwtService, never()).getUsername(any());
    }

    @Test
    @DisplayName("Should not authenticate user when token is invalid")
    void shouldContinueChain_whenTokenIsInvalid() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer invalidtoken");
        when(jwtService.isTokenExpired("invalidtoken")).thenReturn(false);
        when(jwtService.getUsername("invalidtoken")).thenReturn("jean");
        when(userDetailsService.loadUserByUsername("jean")).thenReturn(userDetails);
        when(jwtService.isValidToken("invalidtoken", userDetails)).thenReturn(false);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("Should authenticate user when token is valid")
    void shouldAuthenticateUser_whenTokenIsValid() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer validtoken");
        when(jwtService.isTokenExpired("validtoken")).thenReturn(false);
        when(jwtService.getUsername("validtoken")).thenReturn("jean");
        when(userDetailsService.loadUserByUsername("jean")).thenReturn(userDetails);
        when(jwtService.isValidToken("validtoken", userDetails)).thenReturn(true);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("Should renew token and set it in response header when token is expired but renewable")
    void shouldRenewToken_whenTokenExpiredButCanBeRenewed() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer expiredtoken");
        when(jwtService.isTokenExpired("expiredtoken")).thenReturn(true);
        when(jwtService.canBeTokenRenewed("expiredtoken")).thenReturn(true);
        when(jwtService.getUsername("expiredtoken")).thenReturn("jean");
        when(userDetailsService.loadUserByUsername("jean")).thenReturn(userDetails);
        when(jwtService.isValidToken("expiredtoken", userDetails)).thenReturn(true);
        when(jwtService.renewToken("expiredtoken", userDetails)).thenReturn("newtoken");

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(response).setHeader("Authorization", "Bearer newtoken");
        verify(filterChain).doFilter(request, response);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }
}