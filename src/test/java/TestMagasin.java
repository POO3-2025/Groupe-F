import be.helha.labos.DB.User_DAO;
import be.helha.labos.DBNosql.Connexion_DB_Nosql;
import be.helha.labos.Magasin.Magasin;
import be.helha.labos.collection.Character.Archer;
import be.helha.labos.collection.Character.CharacterType;
import be.helha.labos.collection.Character.Knight;
import be.helha.labos.collection.User;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestMagasin {

    private static MongoDatabase database;
    private static Magasin magasin;
    private static MongoCollection<Document> itemsCollection;
    private static CharacterType testCharacter;
    MongoCollection<CharacterType> Charactercollection = database.getCollection("characters", CharacterType.class);

    @BeforeAll
    static void setUp() throws RuntimeException {
        try {
            // On utilise directement la configuration "nosql_test" du fichier config.json
            Connexion_DB_Nosql mongoFactory = new Connexion_DB_Nosql("nosqlTest");
            database = mongoFactory.createDatabase();

            if (database == null) {
                throw new RuntimeException("La base de données est null");
            }

            magasin = new Magasin(database);
            itemsCollection = database.getCollection("Magasin");
        } catch (Exception e) {
            e.printStackTrace(); // Pour avoir plus de détails sur l'erreur
            throw new RuntimeException("Erreur de connexion à la base de test : " + e.getMessage());
        }
    }

    @BeforeEach
    void setUpEach() {
        // Nettoyage des collections avant chaque test
        itemsCollection.drop();
        database.getCollection("characters").drop();
        database.getCollection("inventory").drop();

        // Création d'un personnage de test
        testCharacter = new Knight("TestKnight",database);
        testCharacter.setMoney(1000.0);
        Charactercollection.insertOne(testCharacter);
    }

    @Test
    @DisplayName("T1 - Affichage des objets disponibles dans le magasin")
    void testAfficherObjetsDisponibles() {
        // Génération d'objets dans le magasin
        Magasin.genererObjets(itemsCollection);

        // Test de l'affichage des objets
        List<Document> objets = magasin.afficherObjetsDisponibles();
        assertNotNull(objets);
        assertFalse(objets.isEmpty());
        assertEquals(10, objets.size());
    }

    @Test
    @DisplayName("T2 - Achat d'un objet par un personnage")
    void testAcheterObjet() {
        // Création d'un objet test
        Document item = new Document()
                .append("_id", new ObjectId())
                .append("nom", "Épée test")
                .append("prix", 100.0)
                .append("type", "Sword")
                .append("allowed","Knight")
                .append("disponible", true);
        itemsCollection.insertOne(item);

        // Test de l'achat
        boolean resultat = magasin.acheterObjet(item, testCharacter);
        assertTrue(resultat);

        // Vérification du montant
        assertEquals(900.0, testCharacter.getMoney());

    }


    @Test
    @DisplayName("T3 - Vente d'un objet par un personnage")
    void testVendreObjet() {
        // Création d'un objet test
        Document item = new Document()
                .append("_id", new ObjectId())
                .append("nom", "Épée test")
                .append("prix", 100.0)
                .append("allowed","Knight")
                .append("type", "Sword");
        itemsCollection.insertOne(item);

        // Test de l'achat
        boolean achats = magasin.acheterObjet(item, testCharacter);
        assertTrue(achats);

        // Test de la vente
        boolean resultat = magasin.vendreObjet(item, testCharacter);
        assertTrue(resultat);
        assertEquals(980.0, testCharacter.getMoney()); // 1000 + (100 * 0.8)
    }

    @AfterEach
    void tearDownEach() {
        itemsCollection.drop();
        database.getCollection("characters").drop();
        database.getCollection("inventory").drop();
    }

    @AfterAll
    static void tearDown() {
        database = null;
        magasin = null;
        itemsCollection = null;
    }
}