import be.helha.labos.collection.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(OrderAnnotation.class)
public class TestUser {

    @BeforeEach
    public void setUp(TestInfo testInfo) {
        System.out.println("Exécution du test : " + testInfo.getDisplayName());
    }

    @Test
    @DisplayName("Test du contructeur et des getters")
    @Order(1)
    public void testConstructeurEtGetters() {
        // Création d'un objet User
        User user1 = new User("TestPseudo", "TestPassword", "TestRole");

        // Vérification des getters
        assertEquals("TestPseudo", user1.getPseudo());
        assertEquals("TestPassword", user1.getPassword());
        assertEquals("TestRole",user1.getRole());
    }

    @Test
    @DisplayName("Test des setters")
    @Order(2)
    public void testSetters() {
        // Création d'un objet User
        User user1 = new User("TestPseudo", "TestPassword", "TestRole");

        // Modification via les setters
        user1.setPseudo("TestPseudo2");
        user1.setPassword("TestPassword2");
        user1.setRole("TestRole2"); // La provenance doit être setter à false avant de modifier l'état de la paire pour 'T' ou 'B'

        // Vérification des nouvelles valeurs
        assertEquals("TestPseudo2", user1.getPseudo());
        assertEquals("TestPassword2", user1.getPassword());
        assertEquals("TestRole2",user1.getRole());
    }

    @Test
    @DisplayName("Vérification du password du user")
    @Order(3)
    public void testVerifierPassword() {
        // Création d'un objet User
        User user1 = new User("TestPseudo", "PasswordSecret", "TestRole");
        assertEquals("PasswordSecret", user1.getPassword());
    }

    @Test
    @DisplayName("Test de l'état actif par défaut")
    @Order(4)
    public void testNonActifParDefaut() {
        User user = new User("pseudo", "password", "admin");
        assertTrue(!(user.isActif()), "L'utilisateur doit être inactif par défaut");
    }

    @Test
    @DisplayName("Test du toString()")
    @Order(5)
    public void testToString() {
        User user = new User(1, "pseudo", "motdepasse", "admin",false);
        String attendu = "\n{id=1" +
                "\n pseudo='pseudo" +
                "\n rôle='admin" +
                "\n actif=false}";
        assertEquals(attendu, user.toString());
    }
}
