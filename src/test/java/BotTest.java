import be.helha.labos.Bot.Bot;
import be.helha.labos.collection.Character.CharacterType;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BotTest {

    private Bot bot;

    @BeforeEach
    public void setUp(TestInfo testInfo) {
        System.out.println("Exécution du test : " + testInfo.getDisplayName());
        bot = new Bot();
    }

    private CharacterType botTest(String title, int level) {
        CharacterType character = new CharacterType();
        character.setTitle(title);
        character.setLevel(level);
        return character;
    }

    @Test
    @Order(1)
    @DisplayName("Orc niveau 1 inflige 40 dégâts")
    public void testOrcLevel1() {
        CharacterType orc = botTest("Orc", 1);
        assertEquals(40, bot.jouerContreBot(orc));
    }

    @Test
    @Order(2)
    @DisplayName("Orc niveau 2 inflige 60 dégâts")
    public void testOrcLevel2() {
        CharacterType orc = botTest("Orc", 2);
        assertEquals(60, bot.jouerContreBot(orc));
    }

    @Test
    @Order(3)
    @DisplayName("Knight niveau 3 inflige 60 dégâts")
    public void testKnightLevel3() {
        CharacterType knight = botTest("Knight", 3);
        assertEquals(60, bot.jouerContreBot(knight));
    }

    @Test
    @Order(4)
    @DisplayName("Archer niveau 1 inflige 10 dégâts")
    public void testUnknownTitleLevel1() {
        CharacterType inconnu = botTest("Archer", 1);
        assertEquals(10, bot.jouerContreBot(inconnu));
    }

    @Test
    @Order(5)
    @DisplayName("Archer niveau 4 inflige 60 dégâts (bonus niveau 50 + base 10)")
    public void testUnknownTitleLevel4() {
        CharacterType inconnu = botTest("Archer", 4);
        assertEquals(55, bot.jouerContreBot(inconnu));
    }

    @Test
    @Order(6)
    @DisplayName("Pas d'erreur lors de l'appel avec un bot sans titre")
    public void testNullTitleNoCrash() {
        CharacterType botSansTitre = botTest(null, 2);
        assertDoesNotThrow(() -> bot.jouerContreBot(botSansTitre));
    }
}
