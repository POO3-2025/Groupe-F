package be.helha.labos.Authentification;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Filtre pour gérer l'authentification JWT.
 * Il extrait le token JWT de la requête et valide l'authentification de l'utilisateur.
 */
public class JwtAuthentificationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    public JwtAuthentificationFilter(JwtUtils jwtUtils,UserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Méthode pour filtrer les requêtes HTTP.
     * Elle extrait le token JWT et valide l'authentification de l'utilisateur.
     *
     * @param request  La requête HTTP
     * @param response La réponse HTTP
     * @param chain    La chaîne de filtres
     * @throws ServletException Si une erreur se produit lors du filtrage
     * @throws IOException      Si une erreur d'entrée/sortie se produit
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String jwtToken = jwtUtils.extractJwtFromRequest(request);

        if (jwtToken != null && jwtUtils.validateJwtToken(jwtToken)) {
            String username = jwtUtils.getUsernameFromJwtToken(jwtToken);

            if (username != null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Créer une authentification basée sur le token
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        System.out.println("Token valide ? " + jwtUtils.validateJwtToken(jwtToken));

        chain.doFilter(request, response);
    }
}
