package be.helha.labos.Authentification;

import be.helha.labos.collection.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
     * @param Id l'ID de l'utilisateur
     * @return l'utilisateur correspondant à l'ID
     */
    @GetMapping("/{Id}")
    public ResponseEntity<User> getUserById(@PathVariable int Id) {
        User user = jeuService.getUserById(Id);
        if(user == null) {
            return ResponseEntity.notFound().build();
        }
        else
            return ResponseEntity.ok(user);
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