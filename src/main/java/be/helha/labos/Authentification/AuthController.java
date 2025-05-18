package be.helha.labos.Authentification;

import be.helha.labos.Authentification.Http.AuthHttpService;
import be.helha.labos.Authentification.Http.UserHttp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Classe de contrôleur pour gérer l'authentification des utilisateurs.
 */
@RestController
public class AuthController {
    /**
     * Constructeur.
     */
    @Autowired
    private JwtUtils jwtUtils;

    private final AuthenticationManager authenticationManager;

    AuthHttpService authHttpService;

    /**
     * Service pour gérer les détails de l'utilisateur.
     */
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    public AuthController(AuthenticationManager authenticationManager, AuthHttpService authHttpService) {
        this.authenticationManager = authenticationManager;
        this.authHttpService = authHttpService;
    }
    /**
     * Méthode pour authentifier un utilisateur.
     *
     * @param loginRequest Contient le nom d'utilisateur et le mot de passe
     * @return Un token JWT si l'authentification réussit
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            // Stocker l'authentification dans le SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Génération du token JWT
            String jwtToken = jwtUtils.generateToken(authentication);
            return ResponseEntity.ok(new AuthResponse(jwtToken, "Authentification réussie !"));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    "Échec de l'authentification : " + e.getMessage());
        }
    }

    @GetMapping("/whoami")
    public ResponseEntity<UserHttp> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        String jwt = authHeader.replace("Bearer ", "");
        if (!jwtUtils.validateJwtToken(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = jwtUtils.getUsernameFromJwtToken(jwt);

        // Construis ton UserHttp à partir du username (et éventuellement des infos du token)
        UserHttp user = new UserHttp();
        user.setUsername(username);

        return ResponseEntity.ok(user);
    }
    /**
     * Classe interne pour représenter la requête de connexion.
     */
    static class LoginRequest {
        private String username;
        private String password;
        private boolean active;

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }
}
