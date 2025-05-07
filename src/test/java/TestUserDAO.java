import be.helha.labos.DB.Connexion_DB;
import be.helha.labos.DB.User_DAO;
import be.helha.labos.collection.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(OrderAnnotation.class)
public class TestUserDAO {


    @BeforeEach
    public void setUp(TestInfo testInfo) {
        System.out.println("Exécution du test : " + testInfo.getDisplayName());
    }

    @Test
    @DisplayName("Test d'ajout d'un USER")
    @Order(1)
    public void testAjouterUser() {

        // Création d'un objet User
        User user1 = new User("TestPseudo", "TestPassword", "TestRole");
        User_DAO userDAO = new User_DAO();
        userDAO.ajouterUser(user1);
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
        user1.setRôle("TestRole2"); // La provenance doit être setter à false avant de modifier l'état de la paire pour 'T' ou 'B'

        // Vérification des nouvelles valeurs
        assertEquals("TestPseudo2", user1.getPseudo());
        assertEquals("TestPassword2", user1.getPassword());
        assertEquals("TestRole2",user1.getRôle());
    }
}
