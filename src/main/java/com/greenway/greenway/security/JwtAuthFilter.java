package com.greenway.greenway.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();

        // 🔓 ROTAS PÚBLICAS (NÃO VALIDAR TOKEN)
        if (path.startsWith("/api/auth/")
                || path.startsWith("/api/maps/")
                || path.startsWith("/h2-console/")
                || path.startsWith("/actuator/")) {

            filterChain.doFilter(request, response);
            return;
        }

        // ─────────────────────────────────────
        // 1. PEGAR O HEADER DO TOKEN
        // ─────────────────────────────────────
        String authHeader = request.getHeader("Authorization");

        // ❗ Se não tem token ou não é Bearer → deixa passar
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // ─────────────────────────────────────
        // 2. EXTRAI TOKEN
        // ─────────────────────────────────────
        String token = authHeader.substring(7);

        // ─────────────────────────────────────
        // 3. EXTRAIR USERNAME DO TOKEN
        // ─────────────────────────────────────
        String username = null;

        try {
            username = jwtService.getUsernameFromToken(token);
        } catch (Exception ignored) {}

        // ─────────────────────────────────────
        // 4. SE O USUÁRIO EXISTE E NÃO ESTÁ LOGADO AINDA
        // ─────────────────────────────────────
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Valida token
            if (jwtService.isValid(token, userDetails)) {

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // AUTENTICA O USUÁRIO
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continua fluxo normal
        filterChain.doFilter(request, response);
    }
}
