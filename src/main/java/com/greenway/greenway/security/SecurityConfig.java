package com.greenway.greenway.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .cors(cors -> {}) // habilita CORS

            .csrf(csrf -> csrf.disable()) // desabilita CSRF

            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // stateless

            .authorizeHttpRequests(auth -> auth
                // Permitir acesso às páginas HTML e recursos estáticos
                .requestMatchers(
                    "/", 
                    "/index.html",
                    "/cadastro-endereco.html",
                    "/cadastro-usuario.html",
                    "/login.html",
                    "/lista-enderecos.html",
                    "/enderecos",
                    "/login",
                    "/home",
                    "/usuarios/cadastrar",
                    "/static/**",
                    "/js/**",
                    "/css/**",
                    "/images/**",
                    "/favicon.ico"
                ).permitAll()

                // Endpoints públicos da API
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/maps/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                
                .requestMatchers("/api/enderecos/**").permitAll()

                // Rotas administrativas
                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                // Demais requisições precisam estar autenticadas
                .anyRequest().authenticated()
            );

        // Permite uso do H2 console dentro de iframe
        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));

        // Adiciona o filtro JWT antes do filtro de autenticação padrão
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // AuthenticationManager para autenticação
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // PasswordEncoder para criptografia de senha
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationProvider usando UserDetailsService e PasswordEncoder
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
}
