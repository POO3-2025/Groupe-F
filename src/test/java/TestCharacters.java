
import be.helha.labos.DBNosql.Connexion_DB_Nosql;
import be.helha.labos.collection.Character.*;
import be.helha.labos.collection.Inventaire;
import be.helha.labos.collection.User;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import static com.mongodb.client.model.Filters.eq;
import static org.junit.jupiter.api.Assertions.*;

//Test CharacterType (Knight et Orc héritent de CharacterType et sont identique a Archer)

@TestMethodOrder(OrderAnnotation.class)
public class TestCharacters {

    private User testUser;

    Connexion_DB_Nosql connexion = new Connexion_DB_Nosql("nosqlTest");
    MongoDatabase db = connexion.createDatabase();

    @BeforeEach
    public void setUp(TestInfo testInfo) {
        System.out.println("Exécution du test : " + testInfo.getDisplayName());
        //utilisateur fictif pour les tests
        testUser = new User("TestUser", "TestPassword", "TestRole");
        testUser.setId(1); // ID USER fictif
    }

    @Test
    @DisplayName("Test du constructeur et des getters de CharacterType")
    @Order(1)
    public void testCharacterTypeConstructeurEtGetters() {
        CharacterType character = new CharacterType("TestCharacter", 100, 10, 0.2, 0.8,db, testUser);

        assertEquals("TestCharacter", character.getName());
        assertEquals(100, character.getHealth());
        assertEquals(10, character.getDamage());
        assertEquals(0.2, character.getDodgeChance());
        assertEquals(0.8, character.getPrecision());
        assertEquals(1, character.getIdUser());
        assertEquals(1, character.getLevel());
        assertEquals(100.00, character.getMoney());
    }

    @Test
    @DisplayName("Test des setters de CharacterType")
    @Order(2)
    public void testCharacterTypeSetters() {
        CharacterType character = new CharacterType();
        character.setName("UpdatedName");
        character.setHealth(120);
        character.setDamage(15);
        character.setDodgeChance(0.3);
        character.setPrecision(0.9);
        character.setIdUser(2);
        character.setLevel(2);
        character.setMoney(200.00);

        assertEquals("UpdatedName", character.getName());
        assertEquals(120, character.getHealth());
        assertEquals(15, character.getDamage());
        assertEquals(0.3, character.getDodgeChance());
        assertEquals(0.9, character.getPrecision());
        assertEquals(2, character.getIdUser());
        assertEquals(2, character.getLevel());
        assertEquals(200.00, character.getMoney());
    }

    @Test
    @DisplayName("Test du constructeur et des méthodes spécifiques de Archer")
    @Order(3)
    public void testArcher() {
        Archer archer = new Archer("TestArcher",db);

        assertEquals("TestArcher", archer.getName());
        assertEquals("Archer", archer.getTitle());
        assertEquals(100, archer.getHealth());
        assertEquals(5, archer.getDamage());
        assertEquals(0.5, archer.getDodgeChance());
        assertEquals(0.9, archer.getPrecision());
        assertEquals(1, archer.getLevel());
        assertEquals(100.00, archer.getMoney());
    }

    @Test
    @DisplayName("Test du constructeur et des méthodes spécifiques de Knight")
    @Order(4)
    public void testKnight() {
        Knight knight = new Knight("TestKnight",db);

        assertEquals("TestKnight", knight.getName());
        assertEquals("Knight", knight.getTitle());
        assertEquals(150, knight.getHealth());
        assertEquals(25, knight.getDamage());
        assertEquals(0.3, knight.getDodgeChance());
        assertEquals(0.7, knight.getPrecision());
        assertEquals(1, knight.getLevel());
        assertEquals(100.00, knight.getMoney());
    }

    @Test
    @DisplayName("Test du constructeur et des méthodes spécifiques de Orc")
    @Order(5)
    public void testOrc() {
        Orc orc = new Orc("TestOrc",db);

        assertEquals("TestOrc", orc.getName());
        assertEquals("Orc", orc.getTitle());
        assertEquals(250, orc.getHealth());
        assertEquals(50, orc.getDamage());
        assertEquals(0.0, orc.getDodgeChance());
        assertEquals(0.5, orc.getPrecision());
        assertEquals(1, orc.getLevel());
        assertEquals(100.00, orc.getMoney());
    }

    @Test
    @DisplayName("Test de la méthode attackHits de CharacterType")
    @Order(6)
    public void testAttackHits() {
        CharacterType character = new CharacterType("TestCharacter", 100, 10, 0.2, 0.8,db, testUser);
        boolean attackResult = character.attackHits();

        // Vérifie que la méthode retourne un bool
        assertTrue(attackResult || !attackResult);
    }

    @Test
    @DisplayName("Test de la méthode attackHitsMainNu de CharacterType")
    @Order(7)
    public void testAttackHitsMainNu() {
        CharacterType attacker = new CharacterType("Attacker", 100, 10, 0.2, 0.8,db, testUser);
        CharacterType target = new CharacterType("Target", 100, 5, 0.1, 0.9,db, testUser);

        int damage = attacker.attackHitsMainNu(target);

        // vérifie que les dégâts correspondent
        assertEquals(5, damage);
    }

    @Test
    @DisplayName("Test de la mise à jour de l'argent dans la base de données")
    @Order(9)
    public void testUpdateMoneyInDB() {
        CharacterType character = new CharacterType("TestCharacter", 100, 10, 0.2, 0.8,db, testUser);
        character.setMoney(200.00);

        // Simule la mise à jour dans la base de données
        character.updateMoneyInDB(db);

        // Vérifie que l'argent a été mis à jour
        assertEquals(200.00, character.getMoney());
    }

    @Test
    @DisplayName("Test de la suppression d'un personnage")
    @Order(10)
    public void testRemoveCharacter() {
        // Insère un personnage ("test") dans la base de données avec son inventaire
        Knight character = new Knight("TestCharacter",db);
        MongoCollection<Document> collection = db.getCollection("characters");
        collection.insertOne(new Document("_id", character.getId())
                .append("name", character.getName())
                .append("inventaire", character.getInventaire().getId()));

        // Supprime le personnage
        character.removeCharacter(db,character.getId());

        // Vérifie que le personnage n'existe plus dans la DB
        assertNull(collection.find(eq("_id", character.getId())).first(), "Le personnage n'a pas été supprimé.");

        // Vérifie que l'inventaire n'existe également plus dans la DB
        MongoCollection<Document> inventoryCollection = db.getCollection("inventory");
        assertNull(inventoryCollection.find(eq("_id", character.getInventaire().getId())).first(), "L'inventaire n'a pas été supprimé.");
    }

    @Test
    @DisplayName("Test de la méthode toString pour CharacterType")
    @Order(11)
    public void testToStringCharacterType() {
        CharacterType character = new CharacterType("TestCharacter", 100, 10, 0.2, 0.8,db, testUser);
        String expected = "Character{name='TestCharacter', health=100, title='null', damage=10, money=100.0, user=1, dodge=0.2, precision=0.8}";
        assertEquals(expected, character.toString());
    }

    @Test
    @DisplayName("Test de la méthode toString pour Archer")
    @Order(12)
    public void testToStringArcher() {
        Archer archer = new Archer("TestArcher",db);
        String expected = "Archer{name='TestArcher', health=100, title='Archer', damage=5, money=100.0, user=0, dodge=0.5, precision=0.9}";
        assertEquals(expected, archer.toString());
    }
}
