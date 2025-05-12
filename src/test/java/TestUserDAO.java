import be.helha.labos.Authentification.Authen;
import be.helha.labos.DB.User_DAO;
import be.helha.labos.collection.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(OrderAnnotation.class)
public class TestUserDAO {

    User_DAO userDao = new User_DAO("mysqlTEST");

    @BeforeEach
    public void setUp(TestInfo testInfo) {
        System.out.println("Exécution du test : " + testInfo.getDisplayName());
        userDao.supprimerTableUser();
    }

    @Test
    @DisplayName("Test d'ajout d'un USER")
    @Order(1)
    public void testAjouterUser() {

        // Création d'un objet User
        User user1 = new User("TestPseudo", "TestPassword", "TestRole");
        userDao.ajouterUser(user1);
        assertTrue(userDao.verifierConnexion(user1.getPseudo(), user1.getPassword()));
        assertEquals(1, user1.getId());
    }

    @Test
    @DisplayName("Vérification de l'authentification du user")
    @Order(2)
    public void testVerifierAuthentification() {
        // Création d'un objet User
        User user1 = new User("TestPseudo", "PasswordSecret", "TestRole");
        userDao.ajouterUser(user1);
        assertTrue(userDao.verifierConnexion(user1.getPseudo(), user1.getPassword()));

        Authen authen = new Authen();
        String token = authen.login(user1.getPseudo(),"PasswordSecret","mysqlTEST");
        assertNotNull(token);
    }

    @Test
    @DisplayName("Récupérer le user par le pseudo")
    @Order(3)
    public void testGetUserByPseudo() {
        // Création d'un objet User
        User user1 = new User("TestDeCeUser", "PasswordSecret", "TestRole");
        userDao.ajouterUser(user1);
        assertTrue(userDao.verifierConnexion(user1.getPseudo(), user1.getPassword()));
        assertEquals(user1.getPseudo(),userDao.getUserByPseudo(user1.getPseudo()).getPseudo());
    }
    @Test
    @DisplayName("Récupérer le user par son id")
    @Order(4)
    public void testGetUserById() {
        User user1 = new User("TestDeCeUser", "PasswordSecret", "TestRole");
        userDao.ajouterUser(user1);
        assertTrue(userDao.verifierConnexion(user1.getPseudo(), user1.getPassword()));
        assertEquals(1, userDao.getUserById(user1.getId()).getId());
    }

}
