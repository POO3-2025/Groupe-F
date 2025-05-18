package be.helha.labos.Authentification;

import be.helha.labos.Authentification.Http.AuthHttpService;
import be.helha.labos.Authentification.Http.UserHttp;
import be.helha.labos.collection.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jeu")
/**
 * Classe de contrôleur pour gérer les opérations liées aux utilisateurs dans le jeu.
 */
public class jeuController {
    /**
     * Service pour gérer les opérations liées aux utilisateurs dans le jeu.
     */
    private final jeuService jeuService;


    /**
     * Constructeur pour initialiser le service de jeu.
     *
     * @param jeuService le service de jeu
     */
    @Autowired
    public jeuController(jeuService jeuService) {
        this.jeuService = jeuService;
    }

    /**
     * Méthode pour récupérer tous les utilisateurs.
     *
     * @return liste des utilisateurs
     */
    @GetMapping
    public List<User> getAlluser() {
        return jeuService.getAllUsers();
    }

    /**
     * Méthode pour récupérer un utilisateur par son ID.
     *
     * @param id l'ID de l'utilisateur
     * @return l'utilisateur correspondant à l'ID
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id, Authentication authentication) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // ADMIN peut tout voir
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin || userDetails.getId() == id) {
            return ResponseEntity.ok(jeuService.getUserById(id));
        }

        // Interdit aux autres
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }


    /**
     * Méthode pour créer un nouvel utilisateur.
     *
     * @param user l'utilisateur à créer
     * @return l'utilisateur créé
     */
    @PostMapping
    public User createUser(@RequestBody User user) {
        return jeuService.saveUser(user);
    }

    @GetMapping("/me")
    public ResponseEntity<String> lancerJeu(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");

        try {
            jeuService.démarrerJeu(token);
            return ResponseEntity.ok("Jeu démarré !");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Erreur : " + e.getMessage());
        }
    }

    /*@PutMapping("/{Id}")
    public ResponseEntity<User> updateUser(@PathVariable ObjectId Id, @RequestBody User userDetails) {
        Optional<User> user = jeuService.getUserById(Id);
        if (user.isPresent()) {
            User updatedUser = user.get();
            updatedUser.setId(userDetails.getId());
            updatedUser.setNom(userDetails.getNom());
            updatedUser.setPrenom(userDetails.getPrenom());
            return ResponseEntity.ok(jeuService.saveUser(updatedUser));
        } else {
            return ResponseEntity.notFound().build();
        }
    }*/

    /*@DeleteMapping("/{Id}")
    public ResponseEntity<Void> deleteUser(@PathVariable ObjectId id) {
        jeuService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }*/
}