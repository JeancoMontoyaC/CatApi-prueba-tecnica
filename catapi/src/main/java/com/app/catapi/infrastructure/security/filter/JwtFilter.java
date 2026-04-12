package com.app.catapi.infrastructure.security.filter;

import com.app.catapi.infrastructure.security.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService  jwtService;
    private final UserDetailsService userDetailsService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException
    {
        String authorization = request.getHeader("Authorization");

        if(authorization == null || !authorization.startsWith("Bearer ")) {
            log.error("Authorization header is invalid");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.substring(7);

        try {
            boolean isTokenExpired = jwtService.isTokenExpired(token);
            boolean canTokenBeRenewed = jwtService.canBeTokenRenewed(token);

            if(isTokenExpired && !canTokenBeRenewed) {
                log.error("Token is expired and cannot be renewed");
                filterChain.doFilter(request, response);
                return;
            }

            String username = jwtService.getUsername(token);
            UserDetails user = userDetailsService.loadUserByUsername(username);

            boolean isValidToken = jwtService.isValidToken(token, user);

            if(!isValidToken || SecurityContextHolder.getContext().getAuthentication() != null) {
                log.error("Invalid token or user is already logged in");
                filterChain.doFilter(request, response);
                return;
            }

            if(isTokenExpired) {
                String newToken = jwtService.renewToken(token, user);
                response.setHeader("Authorization","Bearer " + newToken);
            }

            UsernamePasswordAuthenticationToken authenticationToken =  new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    user.getAuthorities()
            );

            authenticationToken.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Error processing JWT token: {}", e.getMessage());
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }
}
