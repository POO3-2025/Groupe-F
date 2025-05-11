import be.helha.labos.DBNosql.*;
import com.mongodb.client.MongoDatabase;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

@TestMethodOrder(OrderAnnotation.class)
public class TestDAO_NOSQL {

    @BeforeEach
    public void setUp(TestInfo testInfo) {
        System.out.println("Exécution du test : " + testInfo.getDisplayName());
    }

    private static Connexion_DB_Nosql connexionDbNosql = new Connexion_DB_Nosql("nosqlTest");;
    private static MongoDatabase mongoDatabase = connexionDbNosql.createDatabase();

    @Test
    @DisplayName("Test de lecture des collections")
    @Order(1)
    public void testLireCollection() {
        DAO_NOSQL dao = new DAO_NOSQL();
        dao.readAllCollections(mongoDatabase);
    }

}
