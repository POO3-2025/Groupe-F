import be.helha.labos.DB.User_DAO;
import be.helha.labos.DBNosql.*;
import be.helha.labos.collection.Character.Archer;
import be.helha.labos.collection.Character.CharacterType;
import be.helha.labos.collection.Character.Knight;
import be.helha.labos.collection.User;
import com.mongodb.client.MongoDatabase;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@TestMethodOrder(OrderAnnotation.class)
public class TestDAO_NOSQL {

    Connexion_DB_Nosql conn = new Connexion_DB_Nosql("nosqlTest");
    MongoDatabase dbTest = conn.createDatabase();
    String dbkeySQL = "mysqlTEST";
    DAO_NOSQL dao = new DAO_NOSQL("nosqlTest");
    User_DAO userDao = new User_DAO(dbkeySQL);

    @BeforeEach
    public void setUp(TestInfo testInfo) {
        System.out.println("Exécution du test : " + testInfo.getDisplayName());
        userDao.supprimerTableUser();
    }

    @Test
    @DisplayName("Test d'ajout d'un perso à un user")
    @Order(1)
    public void testAjouterPersoAUser() {
        User user1 = new User("TestPseudo", "TestPassword", "TestRole");
        userDao.ajouterUser(user1);
        Archer archer = new Archer("archer",dbTest);

        assertDoesNotThrow(() ->dao.ajouterPersonnagePourUser(dbkeySQL,user1.getPseudo(),archer) , "La méthode attack ne doit pas lever d'exception.");
    }

    @Test
    @DisplayName("Test de suppression du perso")
    @Order(2)
    public void TestSuppressionPersoAUser() {
        User user1 = new User("TestPseudo", "TestPassword", "TestRole");
        userDao.ajouterUser(user1);
        Archer archer = new Archer("archerTestASupprimer",dbTest);

        assertDoesNotThrow(() ->dao.ajouterPersonnagePourUser(dbkeySQL,user1.getPseudo(),archer) , "La méthode attack ne doit pas lever d'exception.");
        assertDoesNotThrow(()->dao.DeleteCharactersById(archer.getId()),"La méthode ne doit pas renvoyer d'erreur");

        List<CharacterType> characters = dao.readAllCharactersByUserId(user1.getId());
        Assertions.assertTrue(characters.stream().noneMatch(c -> c.getName().equals("archerTestASupprimer")));

    }

    @Test
    @DisplayName("Test de lecture des personnages d'un user par son ID")
    @Order(3)
    public void TestLireCharactersByUserById() {
        User user1 = new User("TestPseudoPersoParUser", "TestPassword", "TestRole");
        userDao.ajouterUser(user1);
        Archer archer = new Archer("archerTest",dbTest);
        Knight knight = new Knight("knightTest",dbTest);

        assertDoesNotThrow(() ->dao.ajouterPersonnagePourUser(dbkeySQL,user1.getPseudo(),archer) , "La méthode attack ne doit pas lever d'exception.");
        assertDoesNotThrow(() ->dao.ajouterPersonnagePourUser(dbkeySQL,user1.getPseudo(),knight) , "La méthode attack ne doit pas lever d'exception.");
        assertDoesNotThrow(()->dao.readAllCharactersByUserId(user1.getId()));
    }

    @Test
    @DisplayName("Test ajout perso avec pseudo inexistant")
    @Order(4)
    public void testAjouterPersoUserInexistant() {
        Archer archer = new Archer("ArcherInexistant", dbTest);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            dao.ajouterPersonnagePourUser(dbkeySQL, "Inexistant", archer);
        });
    }
}
