package be.helha.labos.Authentification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "be.helha.labos.Authentification")
@EntityScan(basePackages = "be.helha.labos.collection")
/**
 * Classe principale pour démarrer l'application de serveur d'authentification.
 * Elle utilise Spring Boot pour configurer et exécuter l'application.
 */
public class ServerAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerAuthApplication.class, args);
    }
}
