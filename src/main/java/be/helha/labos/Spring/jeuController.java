package be.helha.labos.Spring;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import be.helha.labos.collection.*;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<User> getUserById(@PathVariable ObjectId Id) {
        Optional<User> user = jeuService.getUserById(Id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return jeuService.saveUser(user);
    }

    @PutMapping("/{Id}")
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
    }

    @DeleteMapping("/{Id}")
    public ResponseEntity<Void> deleteUser(@PathVariable ObjectId id) {
        jeuService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}