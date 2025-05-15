import be.helha.labos.Authentification.Authen;
import be.helha.labos.DB.User_DAO;
import be.helha.labos.collection.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import java.util.List;

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
    @DisplayName("Test de hash et vérification du mot de passe")
    @Order(2)
    public void testPasswordHashingEtVerification() {
        String password = "monSuperMotDePasse";
        String hashed = User_DAO.PasswordUtils.hashPassword(password);

        assertNotEquals(password, hashed);
        assertTrue(User_DAO.PasswordUtils.verifyPassword(password, hashed));
    }

    @Test
    @DisplayName("Vérification de l'authentification du user")
    @Order(3)
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
    @DisplayName("Connexion échoue avec mot de passe incorrect")
    @Order(4)
    public void testConnexionAvecMauvaisMotDePasse() {
        User user = new User("TestPseudo", "correct", "TestRole");
        userDao.ajouterUser(user);

        boolean result = userDao.verifierConnexion("TestPseudo", "incorrect");
        assertFalse(result);
    }

    @Test
    @DisplayName("Récupérer le user par le pseudo")
    @Order(5)
    public void testGetUserByPseudo() {
        // Création d'un objet User
        User user1 = new User("TestDeCeUser", "PasswordSecret", "TestRole");
        userDao.ajouterUser(user1);
        assertTrue(userDao.verifierConnexion(user1.getPseudo(), user1.getPassword()));
        assertEquals(user1.getPseudo(),userDao.getUserByPseudo(user1.getPseudo()).getPseudo());
    }

    @Test
    @DisplayName("Récupérer un utilisateur inexistant par pseudo")
    @Order(6)
    public void testGetUserByPseudoInexistant() {
        assertNull(userDao.getUserByPseudo("inconnu"));
    }

    @Test
    @DisplayName("Récupérer le user par son id")
    @Order(7)
    public void testGetUserById() {
        User user1 = new User("TestDeCeUser", "PasswordSecret", "TestRole");
        userDao.ajouterUser(user1);
        assertTrue(userDao.verifierConnexion(user1.getPseudo(), user1.getPassword()));
        assertEquals(1, userDao.getUserById(user1.getId()).getId());
    }

    @Test
    @DisplayName("Test d'ajout d'un utilisateur avec un pseudo déjà existant")
    @Order(8)
    public void testAjouterUserAvecPseudoExistant() {
        User user1 = new User("DuplicateUser", "PasswordSecret", "Role");
        userDao.ajouterUser(user1);

        User duplicate = new User("DuplicateUser", "AnotherPassword", "AnotherRole");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userDao.ajouterUser(duplicate);
        });

        assertEquals("Un utilisateur avec ce pseudo existe déjà", exception.getMessage());
    }

    @Test
    @DisplayName("Test de récupération de tous les utilisateurs")
    @Order(9)
    public void testGetAllUser() {
        User user1 = new User("UserTest1", "PasswordSecret1", "role1");
        User user2 = new User("UserTest2", "PasswordSecret2", "role2");
        userDao.ajouterUser(user1);
        userDao.ajouterUser(user2);

        List<User> users = userDao.getAllUser();

        assertEquals(2, users.size());
        assertTrue(users.stream().anyMatch(u -> u.getPseudo().equals("UserTest1")));
        assertTrue(users.stream().anyMatch(u -> u.getPseudo().equals("UserTest2")));
    }

    @Test
    @DisplayName("Test de suppression de tous les utilisateurs")
    @Order(10)
    public void testSupprimerTableUser() {
        User user1 = new User("UserTest", "PasswordSecret", "role");
        userDao.ajouterUser(user1);
        userDao.supprimerTableUser();

        assertTrue(userDao.getAllUser().isEmpty());
    }

}
