package be.helha.labos.Authentification;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
/**
 * Classe utilitaire pour gérer les opérations liées aux tokens JWT.
 * Elle permet de générer, valider et extraire des informations à partir des tokens JWT.
 */
public class JwtUtils {

    private final SecretKey key;
    private final int jwtExpirationMs = 3600000; // période de validité du token (1 heure)

    /**
     * Constructeur avec une clé fixe
     */
    public JwtUtils() {
        // cette clé est absente pour des raisons de confidentialité
        // Vous pouvez générer une clé de 64 caractères via un site web et la placé dans les "".
        String secret = ""; // clé secrete
        byte[] keyBytes = Base64.getEncoder().encode(secret.getBytes());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Méthode pour générer un token JWT à partir de l'authentification de l'utilisateur.
     *
     * @param authentication L'authentification de l'utilisateur
     * @return Le token JWT généré
     */
    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(",")))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key)
                .compact();
    }

    /**
     * Méthode pour extraire le token JWT de la requête HTTP.
     *
     * @param request La requête HTTP
     * @return Le token JWT extrait, ou null si aucun token n'est trouvé
     */
    public String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * Méthode pour extraire le nom d'utilisateur à partir du token JWT.
     *
     * @param token Le token JWT
     * @return Le nom d'utilisateur extrait du token
     */
    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Méthode pour valider le token JWT.
     *
     * @param token Le token JWT à valider
     * @return true si le token est valide, false sinon
     */
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    /**
     * Méthode pour extraire les rôles à partir du token JWT.
     *
     * @param token Le token JWT
     * @return La liste des rôles extraits du token
     */
    public List<String> getRolesFromJwtToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
        return Arrays.asList(claims.get("roles").toString());
    }

}