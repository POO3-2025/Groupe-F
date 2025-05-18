import be.helha.labos.collection.Item.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(OrderAnnotation.class)
@DisplayName("Classe de test pour les Items")
public class TestItems {

    @Test
    @Order(1)
    @DisplayName("Test du constructeur et des getters de Item")
    void testItemConstructeurEtGetters() {
        Item item = new Item();
        item.setName("TestItem");
        item.setId(new org.bson.types.ObjectId());

        assertNotNull(item.getId(), "L'ID de l'item ne doit pas être null.");
        assertEquals("TestItem", item.getName(), "Le nom de l'item doit être 'TestItem'.");
    }

    @Test
    @Order(2)
    @DisplayName("Test des setters de Item")
    void testItemSetters() {
        Item item = new Item();
        item.setName("NewItemName");
        item.setId(new org.bson.types.ObjectId());

        assertEquals("NewItemName", item.getName(), "Le nom de l'item doit être 'NewItemName'.");
        assertNotNull(item.getId(), "L'ID de l'item ne doit pas être null après modification.");
    }

    @Test
    @Order(3)
    @DisplayName("Test du constructeur et des getters de Weapon")
    void testWeaponConstructeurEtGetters() {
        Weapon weapon = new Sword(Sword.SwordMaterial.STEEL);

        assertEquals(5, weapon.getDamage(), "Les dégâts de l'arme doivent être 10.");
        assertEquals("Épée", weapon.getWeaponType().getName(), "Le type de l'arme doit être 'Épée'.");
    }

    @Test
    @Order(4)
    @DisplayName("Test des setters de Weapon")
    void testWeaponSetters() {
        Weapon weapon = new Sword(Sword.SwordMaterial.STEEL);
        weapon.setDamage(25);
        weapon.setWeaponType(WeaponType.SWORD);

        assertEquals(25, weapon.getDamage(), "Les dégâts de l'arme doivent être 25 après modification.");
        assertEquals("Épée", weapon.getWeaponType().getName(), "Le type de l'arme doit être 'Épée'.");
    }

    @Test
    @Order(5)
    @DisplayName("Test de la classe Sword")
    void testSword() {
        Sword sword = new Sword(Sword.SwordMaterial.GOLD);

        assertEquals(25, sword.getDamage(), "Les dégâts de l'épée doivent être 15.");
        assertEquals("Or", sword.getMaterial().getMaterial(), "Le matériau de l'épée doit être 'Or'.");
    }

    @Test
    @Order(6)
    @DisplayName("Test des setters de Sword")
    void testSwordSetters() {
        Sword sword = new Sword(Sword.SwordMaterial.STEEL);
        sword.setMaterial(Sword.SwordMaterial.FIRE);

        assertEquals(50, sword.getDamage(), "Les dégâts de l'épée doivent être 20 après modification.");
        assertEquals("Feu", sword.getMaterial().getMaterial(), "Le matériau de l'épée doit être 'Feu'.");
    }

    @Test
    @Order(7)
    @DisplayName("Test de la classe Bow")
    void testBow() {
        Bow bow = new Bow(Bow.BowMaterial.CROSSBOW);

        assertEquals(25, bow.getDamage(), "Les dégâts de l'arc doivent être 18.");
        assertEquals("Arbalète", bow.getMaterial().getMaterial(), "Le matériau de l'arc doit être 'Arbalète'.");
    }

    @Test
    @Order(8)
    @DisplayName("Test des setters de Bow")
    void testBowSetters() {
        Bow bow = new Bow(Bow.BowMaterial.WOOD);
        bow.setMaterial(Bow.BowMaterial.ICE);

        assertEquals(33, bow.getDamage(), "Les dégâts de l'arc doivent être 15 après modification.");
        assertEquals("Glace", bow.getMaterial().getMaterial(), "Le matériau de l'arc doit être 'Glace'.");
    }

    @Test
    @Order(9)
    @DisplayName("Test de la classe Mace")
    void testMace() {
        Mace mace = new Mace(Mace.MaceMaterial.DIAMOND);

        assertEquals(99, mace.getDamage(), "Les dégâts de la masse doivent être 18.");
        assertEquals("Diamant", mace.getMaterial().getMaterial(), "Le matériau de la masse doit être 'Diamant'.");
    }

    @Test
    @Order(10)
    @DisplayName("Test des setters de Mace")
    void testMaceSetters() {
        Mace mace = new Mace(Mace.MaceMaterial.WOOD);
        mace.setMaterial(Mace.MaceMaterial.STONE);

        assertEquals(49, mace.getDamage(), "Les dégâts de la masse doivent être 12 après modification.");
        assertEquals("Pierre", mace.getMaterial().getMaterial(), "Le matériau de la masse doit être 'Pierre'.");
    }

    @Test
    @Order(11)
    @DisplayName("Test de la classe Potion")
    void testPotion() {
        Potion potion = new Potion(100, 50);

        assertEquals(100, potion.getMaxContent(), "Le contenu maximum de la potion doit être 100.");
        assertEquals(50, potion.getActualContent(), "Le contenu actuel de la potion doit être 50.");
        assertEquals("Potion", potion.getType(), "Le type de la potion doit être 'Potion'.");
    }

    @Test
    @Order(12)
    @DisplayName("Test des setters de Potion")
    void testPotionSetters() {
        Potion potion = new Potion(100, 50);
        potion.setMaxContent(200);
        potion.setActualContent(150);

        assertEquals(200, potion.getMaxContent(), "Le contenu maximum de la potion doit être 200 après modification.");
        assertEquals(150, potion.getActualContent(), "Le contenu actuel de la potion doit être 150 après modification.");
    }

    @Test
    @Order(13)
    @DisplayName("Test de la méthode toString de Weapon")
    void testWeaponToString() {
        Sword sword = new Sword(Sword.SwordMaterial.FIRE);
        String expected = "Épée (Matériau: Feu, Dégâts: 50, Niveau requis: 15, Type autorisé: Knight)";
        assertEquals(expected, sword.toString(), "La méthode toString de l'épée doit retourner la chaîne attendue.");
    }

    @Test
    @Order(14)
    @DisplayName("Test de la méthode attack de Weapon")
    void testWeaponAttack() {
        Sword sword = new Sword(Sword.SwordMaterial.SILVER);
        assertDoesNotThrow(() -> sword.attack(), "La méthode attack ne doit pas lever d'exception.");
    }

    @Test
    @Order(15)
    @DisplayName("Test des exceptions pour les dégâts négatifs")
    void testWeaponNegativeDamage() {
        Weapon weapon = new Sword(Sword.SwordMaterial.STEEL);
        assertThrows(IllegalArgumentException.class, () -> weapon.setDamage(-5), "Les dégâts négatifs doivent lever une exception.");
    }
}