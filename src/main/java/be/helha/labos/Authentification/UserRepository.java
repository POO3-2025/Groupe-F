package be.helha.labos.Authentification;

import be.helha.labos.collection.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Interface de référentiel pour gérer les opérations liées aux utilisateurs dans la base de données.
 * Elle étend JpaRepository pour fournir des méthodes CRUD par défaut.
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByPseudo(String pseudo);
}