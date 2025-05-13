import be.helha.labos.DB.User_DAO;
import be.helha.labos.DBNosql.*;
import be.helha.labos.collection.Character.Archer;
import be.helha.labos.collection.User;
import com.mongodb.client.MongoDatabase;
import org.assertj.core.api.NotThrownAssert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

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
        Archer archer = new Archer("archerTest",dbTest);

        assertDoesNotThrow(() ->dao.ajouterPersonnagePourUser(dbkeySQL,user1.getPseudo(),archer) , "La méthode attack ne doit pas lever d'exception.");
        assertDoesNotThrow(()->dao.DeleteCharacters(archer.getId()),"La méthode ne doit pas renvoyer d'erreur");
    }
}
