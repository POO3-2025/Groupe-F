import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(OrderAnnotation.class)
public class TestDBSQL {
    @BeforeEach
    void displayName(TestInfo testInfo) {
        System.out.println(testInfo.getDisplayName());
    }

    @Test
    @DisplayName("Test d'une connexion réussie avec la base MySQL")
    @Order(1)
    public void testConnexionOk() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/gameprod?useSSL=false&serverTimezone=UTC";
            String user = "root";
            String password = "";

            try (Connection con = DriverManager.getConnection(url, user, password)) {
                assertNotNull(con, "La connexion à la base de données doit être établie.");
                System.out.println("Connexion réussie à la base de données.");
            }
        } catch (ClassNotFoundException e) {
            fail("Erreur : Driver MySQL introuvable.", e);
        } catch (SQLException e) {
            fail("Erreur : Échec de la connexion à la base de données.", e);
        }
    }
    @Test
    @DisplayName("Test d'une connexion ratée avec une DB inexistante")
    @Order(2)
    public void testConnexionExistePas() {
        assertThrows(SQLException.class, () -> {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/gameInvalide?useSSL=false&serverTimezone=UTC";
            String user = "root";
            String password = "";
            try (Connection con = DriverManager.getConnection(url, user, password)) {
                // Pas besoin d'actions ici car on s'attend à ce que la connexion échoue
            }
        }, "Une exception SQLException est attendue pour une base de données inexistante.");
    }

    @Test
    @DisplayName("Test d'une connexion ratée avec une DB éxistante mais un MDP faux")
    @Order(3)
    public void testConnexionMDP_Faux() {
        assertThrows(SQLException.class, () -> {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/gameInvalide?useSSL=false&serverTimezone=UTC";
            String user = "root";
            String password = "invalide";
            try (Connection con = DriverManager.getConnection(url, user, password)) {
                // Pas besoin d'actions ici car on s'attend à ce que la connexion échoue
            }
        }, "Une exception SQLException est attendue pour une base de données inexistante.");
    }
}

