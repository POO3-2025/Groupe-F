package be.helha.labos.collection.Character;

import be.helha.labos.collection.User;
import be.helha.labos.DBNosql.Connexion_DB_Nosql;
import be.helha.labos.collection.Inventaire;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Random;

import static com.mongodb.client.model.Filters.eq;

/**
 * Classe mère représentant un personnage dans le jeu.
 * Elle contient des informations sur le personnage, telles que son nom, sa santé, ses dégâts, etc.
 */
public class CharacterType {

/**
 * Attributs de la classe CharacterType
 */
    @JsonProperty("_id")
    protected ObjectId id;
    protected String name;
    protected String title;
    protected int health;
    protected int idUser;
    protected int damage;
    protected double money;
    protected int level;
    protected Inventaire inventaire;
    protected double dodge;
    protected double precision;
    private ObjectId inventoryId;


    private static Connexion_DB_Nosql connexionDbNosql;
    private static MongoDatabase mongoDatabase;
    private static MongoCollection<Document> collection;

    /**
     * Constructeur vide
     */
    public CharacterType(){
    }

    /**
     * Constructeur de la classe CharacterType
     * @param name
     * @param health
     * @param damage
     * @param dodge
     * @param precision
     * @param user
     */
    public CharacterType(String name, int health, int damage, double dodge, double precision,User user) {
        this.id = new ObjectId();
        this.name = name;
        this.health = health;
        this.damage = damage;
        this.dodge = dodge;
        this.idUser= user.getId();
        this.precision = precision;
        this.level = 1;
        this.money = 100.00;
    }

    /**
     * Méthode d'attaque qui touche ou pas la cible
     * @return
     */
    public boolean attackHits()
    {
        Random random = new Random();
        double roll = random.nextDouble(); // Génère un nombre aléatoire entre 0.0 et 1.0
        return roll < (precision * (1 - dodge));
    }

    /**
     * Méthode d'attaque qui touche la cible a main nu (sauf pour l'orc)
     * @return
     */
    public int attackHitsMainNu(CharacterType perso)
    {
        return perso.getDamage();
    }


    /**
     * Méthode de récupération de l'inventaire
     * @return
     */
    public ObjectId getId()
    {
        return id;
    }

    public void setInventoryId(ObjectId id) {
        this.inventoryId = id;
    }

    public ObjectId getInventoryId() {
        return this.inventoryId;
    }

    /**
     * Méthode de setter de l'id
     * @param id
     */
    public void setId(ObjectId id) {
        this.id = id;
    }

    /**
     * Méthode de récupération du nom
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Méthode de récupération du titre
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * Méthode de setter du titre
     * @param Title
     */
    public void setTitle(String Title) {
        title = Title;
    }

    /**
     * Méthode de setter du nom
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Méthode de récupération de la vie
     * @return
     */
    public int getHealth() {
        return health;
    }

    /**
     * Méthode de setter de la vie
     * @param health
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * Méthode de récupération des dégats
     * @return
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Méthode de setter des dégats
     * @param damage
     */
    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**
     * Méthode de récupération de la vie
     * @return
     */
    public double getDodgeChance() {
        return dodge;
    }

    /**
     * Méthode de setter de la vie
     * @param dodge
     */
    public void setDodgeChance(double dodge) {
        this.dodge = dodge;
    }

    /**
     * Méthode de récupération de l'id de l'utilisateur
     * @return
     */
    public int getIdUser() {
        return idUser;
    }

    /**
     * Méthode de setter de l'id de l'utilisateur
     * @param IdUser
     */
    public void setIdUser(int IdUser) {
        idUser = IdUser;
    }

    /**
     * Méthode de récupération de l'argent
     * @return
     */
    public double getMoney() {
        return money;
    }

    /**
     * Méthode de setter de l'argent
     * @param money
     */
    public void setMoney(double money) {
        this.money = money;
    }

    /**
     * Méthode de récupération du niveau
     * @return
     */
    public int getLevel() {
        return level;
    }

    /**
     * Méthode de setter du niveau
     * @param level
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Méthode de récupération de la précision
     * @return
     */
    public double getPrecision() {
        return precision;
    }

    /**
     * Méthode de setter de la précision
     * @param precision
     */
    public void setPrecision(double precision) {
        this.precision = precision;
    }

    /**
     * Méthode de récupération de l'inventaire
     * @return
     */
    public Inventaire getInventaire() {
        return inventaire;
    }

    public void setInventaire(Inventaire inventaire) {
        this.inventaire = inventaire;
    }

    /**
     * Méthode toString pour afficher les informations du personnage
     */
    public String toString() {
        return "Character{" +
                "name='" + name + '\'' +
                ", health=" + health +
                ", title='" + title + '\'' +
                ", damage=" + damage +
                ", money=" + money +
                ", user=" + idUser +
                ", dodge=" + dodge +
                ", precision=" + precision +
                '}';
    }

    /**
     * Méthode changer l'argent du personnage dans la DB
     */
    public void updateMoneyInDB() {
        connexionDbNosql = new Connexion_DB_Nosql("nosqlTest");
        mongoDatabase = connexionDbNosql.createDatabase();
        collection = mongoDatabase.getCollection("characters");

        collection.updateOne(new Document("_id", this.id), new Document("$set", new Document("money", this.money)));
    }

    /**
     * Méthode retirer un personnage de la DB noSQL
     * @param characterId
     */
    public void removeCharacter(ObjectId characterId) {
        try {
            // Connexion à la DB
            connexionDbNosql = new Connexion_DB_Nosql("nosqlTest");
            mongoDatabase = connexionDbNosql.createDatabase();

            // Récupération de la collection des personnages
            MongoCollection<Document> charactersCollection = mongoDatabase.getCollection("characters");

            // Récupération du document personnage AVANT suppression
            Document characterDoc = charactersCollection.find(new Document("_id", characterId)).first();

            if (characterDoc != null) {
                // Récupérer l'ID de l'inventaire à partir du champ "inventaire._id"
                ObjectId inventoryId= characterDoc.getObjectId("inventaire");

                // Suppression du personnage
                charactersCollection.deleteOne(new Document("_id", characterId));
                System.out.println("Personnage supprimé avec succès.");

                MongoCollection<Document> inventoryCollection = mongoDatabase.getCollection("inventory");
                long invDeleted = inventoryCollection.deleteOne(new Document("_id", inventoryId)).getDeletedCount();

                if (invDeleted > 0) {
                    System.out.println("Inventaire du personnage supprimé.");
                } else {
                    System.out.println("Aucun inventaire associé trouvé.");
                }
            } else {
                System.out.println("Aucun personnage trouvé avec cet identifiant.");
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la suppression du personnage ou de son inventaire.");
            e.printStackTrace();
        }
    }

    public Inventaire getInventaireFromDB() {
        Connexion_DB_Nosql connexion = new Connexion_DB_Nosql("nosqlTest");
        MongoDatabase db = connexion.createDatabase();
        MongoCollection<Inventaire> inventaireCollection = db.getCollection("inventory", Inventaire.class);
        return inventaireCollection.find(eq("_id", inventaire)).first();
    }

}