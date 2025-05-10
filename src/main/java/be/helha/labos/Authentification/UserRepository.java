package be.helha.labos.Authentification;

import org.springframework.data.jpa.repository.JpaRepository;
import be.helha.labos.collection.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByPseudo(String pseudo);
}