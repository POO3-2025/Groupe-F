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
    protected int maxHealth;
    protected int experience;
    protected int experienceToNextLevel;
    protected int idUser;
    protected int damage;
    protected double money;
    protected int level;
    protected Inventaire inventaire;
    protected double dodge;
    protected double precision;


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
    public CharacterType(String name, int health, int damage, double dodge, double precision,MongoDatabase database,User user) {
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

    public int attackHitsArme(CharacterType personnage, MongoDatabase mongoDatabase, String itemId) {
        MongoCollection<Document> inventoryCollection = mongoDatabase.getCollection("inventory");
        Document query = new Document("characterId", this.getId());
        Document inventoryDoc = inventoryCollection.find(query).first();

        if (inventoryDoc == null || itemId == null) {
            return attackHitsMainNu(personnage);
        }

        @SuppressWarnings("unchecked")
        java.util.List<Document> slots = (java.util.List<Document>) inventoryDoc.get("slots");
        if (slots == null || slots.isEmpty()) {
            return attackHitsMainNu(personnage);
        }

        // Chercher l'arme spécifique par son ID
        Document armeEquipee = null;
        for (Document slot : slots) {
            Document item = slot.get("item", Document.class);
            if (item != null && item.get("_id").toString().equals(itemId)) {
                armeEquipee = item;
                break;
            }
        }

        // Si on trouve l'arme, utiliser ses dégâts
        if (armeEquipee != null) {
            int degatsArme = armeEquipee.getInteger("attack");
            return degatsArme + new Random().nextInt(3);
        }

        return attackHitsMainNu(personnage);
    }


    /**
     * Méthode de récupération de l'inventaire
     * @return
     */
    public ObjectId getId()
    {
        return id;
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
     * Méthode getter pour récuperer l'expérience actuelle du perso
     * @return
     */
    public int getExperience() {
        return experience;
    }

    /**
     * Méthode setter pour l'expérience actuelle du perso
     * @param experience
     */
    public void setExperience(int experience) {
        this.experience = experience;
    }

    /**
     * Méthode getter pour l'expérience nécéssaire afin de passer d'un niveau
     * @return
     */
    public int getExperienceToNextLevel() {
        return experienceToNextLevel;
    }

    /**
     * Méthode setter pour l'expérience nécéssaire afin de passer d'un niveau
     * @param experienceToNextLevel
     */
    public void setExperienceToNextLevel(int experienceToNextLevel) {
        this.experienceToNextLevel = experienceToNextLevel;
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
     * Méthode de getter de la vie maximum
     * @return
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * méthode de setter de la vie maximum
     * @param maxHealth
     */
    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
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
                ", health=" + maxHealth +
                ", title='" + title + '\'' +
                ", damage=" + damage +
                ", experience=" + experience +
                ", money=" + money +
                ", dodge=" + dodge +
                ", precision=" + precision +
                '}';
    }

    /**
     * Méthode changer l'argent du personnage dans la DB
     */
    public void updateMoneyInDB(MongoDatabase mongoDatabase) {
        collection = mongoDatabase.getCollection("characters");

        collection.updateOne(new Document("_id", this.id), new Document("$set", new Document("money", this.money)));
    }

    /**
     * Méthode pour que le perso récupère toute sa vie après un combat
     * @param mongoDatabase
     */
    public void recupererVie(MongoDatabase mongoDatabase) {
        MongoCollection<Document> collection = mongoDatabase.getCollection("characters");

        // Mise à jour de l'attribut en mémoire
        this.health = this.maxHealth;  // Suppose que tu as bien un attribut `maxHealth`

        // Mise à jour dans la DB
        Document filter = new Document("_id", this.getId());
        Document update = new Document("$set", new Document("health", this.maxHealth));

        collection.updateOne(filter, update);
    }

    /**
     *  Ajoute l'éxpérience gagné lors d'un combat et l'enregistre dans la collecyion du perso en DB NOSQL
     * @param amount // l'expérience en int
     * @param mongoDatabase // la Db pour la connexion
     */
    public void gainExperience(int amount, MongoDatabase mongoDatabase) {
        this.experience += amount;

        if (this.experience >= this.experienceToNextLevel) {
            levelUp(mongoDatabase);
        }

        // Mise à jour de l'XP dans la DB
        MongoCollection<Document> collection = mongoDatabase.getCollection("characters");
        Document filter = new Document("_id", this.getId());
        Document update = new Document("$set", new Document("experience", this.experience));
        collection.updateOne(filter, update);
    }

    /**
     * Méthode permettant de passer d'un niveau si l'expérience suffit
     * @param mongoDatabase // la Db pour la connexion
     */
    private void levelUp(MongoDatabase mongoDatabase) {
        this.experience -= this.experienceToNextLevel;
        this.level++;
        this.experienceToNextLevel += 50; // Chaque niveau demande 50 XP en plus

        // Augmenter stats du personnage
        this.maxHealth += 20;
        this.damage += 5;
        this.health = this.maxHealth; // Vie pleine à la montée de niveau

        // Mise à jour dans la DB
        MongoCollection<Document> collection = mongoDatabase.getCollection("characters");
        Document filter = new Document("_id", this.getId());
        Document update = new Document("$set",
                new Document("level", this.level)
                        .append("experience", this.experience)
                        .append("experienceToNextLevel", this.experienceToNextLevel)
                        .append("health", this.health)
                        .append("maxHealth", this.maxHealth)
                        .append("damage", this.damage)
        );
        collection.updateOne(filter, update);
    }

    /**
     * Méthode retirer un personnage de la DB noSQL
     * @param characterId // L'Object ID du personnage
     */
    public void removeCharacter(MongoDatabase mongoDatabase, ObjectId characterId) {
        try {
            MongoCollection<Document> charactersCollection = mongoDatabase.getCollection("characters");

            // Récupération du document personnage AVANT suppression
            Document characterDoc = charactersCollection.find(new Document("_id", characterId)).first();

            if (characterDoc != null) {
                // Récupérer le sous-document "inventaire"
                Document inventaireDoc = (Document) characterDoc.get("inventaire");
                ObjectId inventoryId = inventaireDoc.getObjectId("_id");

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


    public Inventaire getInventaireFromDB(MongoDatabase mongoDatabase) {
        MongoCollection<Inventaire> inventaireCollection = mongoDatabase.getCollection("inventory", Inventaire.class);
        return inventaireCollection.find(eq("_id", inventaire)).first();
    }

}