package be.helha.labos.Authentification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
/**
 * Configuration de sécurité pour l'application.
 * Elle configure les filtres de sécurité, l'authentification et la gestion des sessions.
 */
public class SecurityConfig  {
    @Autowired
    private JwtUtils jwtUtils;

    /**
     * Méthode pour configurer le filtre de sécurité.
     *
     * @param http La configuration de sécurité HTTP
     * @return La chaîne de filtres de sécurité
     * @throws Exception Si une erreur se produit lors de la configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Désactiver CSRF pour les tests (facultatif pour les APIs REST)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login").permitAll() // Permettre un accès libre à /login
                        .requestMatchers("/jeu/**").authenticated() // Nécessite un JWT
                        .anyRequest().authenticated() // Toute autre requête nécessite une authentification
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS))
                .addFilterBefore(
                        new JwtAuthentificationFilter(jwtUtils,customUserDetailsService),
                        UsernamePasswordAuthenticationFilter.class)
        ;

        return http.build();
    }

    @Autowired

    private CustomUserDetailsService customUserDetailsService;


    @Bean
    /**
     * Méthode pour configurer le gestionnaire d'authentification.
     *
     * @param http La configuration de sécurité HTTP
     * @return Le gestionnaire d'authentification
     * @throws Exception Si une erreur se produit lors de la configuration
     */
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        // service de chargement d'utilisateur
        authenticationManagerBuilder.userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());

        return authenticationManagerBuilder.build();
    }

    @Bean
    /**
     * Méthode pour configurer le mot de passe encodeur.
     *
     * @return Un encodeur de mot de passe
     */
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

