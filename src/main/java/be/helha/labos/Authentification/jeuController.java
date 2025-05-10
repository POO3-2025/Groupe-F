package be.helha.labos.Authentification;

import be.helha.labos.collection.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jeu")
public class jeuController {

    private final jeuService jeuService;

    @Autowired
    public jeuController(jeuService jeuService) {
        this.jeuService = jeuService;
    }


    @GetMapping
    public List<User> getAlluser() {
        return jeuService.getAllUsers();
    }

    @GetMapping("/{Id}")
    public ResponseEntity<User> getUserById(@PathVariable int Id) {
        User user = jeuService.getUserById(Id);
        if(user == null) {
            return ResponseEntity.notFound().build();
        }
        else
            return ResponseEntity.ok(user);
    }

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