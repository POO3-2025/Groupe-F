package be.helha.labos.Spring;

import be.helha.labos.collection.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jeu")

/**
 * Methode controleur pour gérer les opérations liées aux utilisateurs dans le jeu.
 */
public class jeuController {
    /**
     * Methode pour gérer les opérations liées aux utilisateurs dans le jeu.
     */
    private final jeuService jeuService;

    @Autowired
    /**
     * Constructeur pour initialiser le service de jeu.
     */
    public jeuController(jeuService jeuService) {
        this.jeuService = jeuService;
    }


    @GetMapping
    /**
     * Methode Get all users.
     *
     * @return liste des utilisateurs
     */
    public List<User> getAlluser() {
        return jeuService.getAllUsers();
    }

    @GetMapping("/{Id}")
    /**
     * Methode qui retourne un utilisateur par son ID.
     * return @param Id
     */
    public ResponseEntity<User> getUserById(@PathVariable int Id) {
        User user = jeuService.getUserById(Id);
        if(user == null) {
            return ResponseEntity.notFound().build();
        }
        else
            return ResponseEntity.ok(user);
    }

    @PostMapping
    /**
     * Methode qui crée un nouvel utilisateur.
     *
     * @param user
     * @return l'utilisateur créé
     */
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