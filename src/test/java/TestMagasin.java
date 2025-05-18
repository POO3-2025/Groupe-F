import be.helha.labos.DBNosql.Connexion_DB_Nosql;
import be.helha.labos.Magasin.Magasin;
import be.helha.labos.collection.Character.CharacterType;
import be.helha.labos.collection.Character.Knight;
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

        // Insertion du personnage dans la base
        Document characterDoc = new Document()
                .append("_id", new ObjectId())
                .append("name", testCharacter.getName())
                .append("money", testCharacter.getMoney());
        database.getCollection("characters").insertOne(characterDoc);
        testCharacter.setId(characterDoc.getObjectId("_id"));
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
    /*
    @Test
    @DisplayName("T2 - Achat d'un objet par un personnage")

    void testAcheterObjet() {
        // Création d'un objet test
        Document item = new Document()
                .append("_id", new ObjectId())
                .append("nom", "Épée test")
                .append("prix", 100.0)
                .append("type", "Sword")
                .append("disponible", true);
        itemsCollection.insertOne(item);

        // Création d'un inventaire vide
        Document inventoryDoc = new Document()
                .append("_id", new ObjectId())
                .append("character_id", testCharacter.getId())
                .append("slots", List.of(new Document("slot", 0).append("item", null)));
        ObjectId inventoryId = database.getCollection("inventory").insertOne(inventoryDoc).getInsertedId().asObjectId().getValue();

        // Mise à jour du personnage avec l'ID de l'inventaire (dans la base de données et en mémoire)
        database.getCollection("characters").updateOne(
                new Document("_id", testCharacter.getId()),
                new Document("$set", new Document("inventoryId", inventoryId))
        );
        testCharacter.setInventoryId(inventoryId); // <-- C’est ça qui manquait !

        // Test de l'achat
        boolean resultat = magasin.acheterObjet(item, testCharacter);
        assertTrue(resultat);

        // Vérification du montant
        assertEquals(900.0, testCharacter.getMoney());

        // Vérification de l'inventaire
        Document updatedInventory = database.getCollection("inventory").find(
                new Document("_id", inventoryId)
        ).first();
        assertNotNull(updatedInventory);

        List<Document> slots = updatedInventory.getList("slots", Document.class);
        assertNotNull(slots);
        assertFalse(slots.isEmpty());
        Document firstSlot = slots.get(0);
        assertNotNull(firstSlot.get("item"));
    }
    */

    @Test
    @DisplayName("T3 - Vente d'un objet par un personnage")
    void testVendreObjet() {
        // Création d'un objet test
        Document item = new Document()
                .append("_id", new ObjectId())
                .append("nom", "Épée test")
                .append("prix", 100.0)
                .append("type", "Sword");

        // Création d'un inventaire avec l'objet
        Document inventoryDoc = new Document()
                .append("_id", new ObjectId())
                .append("slots", List.of(new Document("item", item)));
        database.getCollection("inventory").insertOne(inventoryDoc);

        // Mise à jour du personnage avec l'inventaire
        database.getCollection("characters").updateOne(
                new Document("_id", testCharacter.getId()),
                new Document("$set", new Document("inventaire", inventoryDoc))
        );

        // Test de la vente
        boolean resultat = magasin.vendreObjet(item, testCharacter);
        assertTrue(resultat);
        assertEquals(1080.0, testCharacter.getMoney()); // 1000 + (100 * 0.8)
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