package be.helha.labos.collection.Character;

import be.helha.labos.DBNosql.Connexion_DB_Nosql;
import be.helha.labos.collection.Inventaire;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CharacterType {


    @JsonProperty("_id")
    protected ObjectId id;
    protected String name;
    protected String title;
    protected int health;
    protected int damage;
    protected Inventaire inventaire;
    protected double dodge;
    protected double precision;

    private static Connexion_DB_Nosql connexionDbNosql;
    private static MongoDatabase mongoDatabase;
    private static MongoCollection<Document> collection;


    public CharacterType(){
    }

    public CharacterType(String name, int health, int damage, double dodge, double precision) {
        this.id = new ObjectId();
        this.name = name;
        this.health = health;
        this.damage = damage;
        this.dodge = dodge;
        this.precision = precision;
    }

    public boolean attackHits()
    {
        Random random = new Random();
        double roll = random.nextDouble(); // Génère un nombre aléatoire entre 0.0 et 1.0
        return roll < (precision * (1 - dodge));
    }

    // Getters et setters
    public ObjectId getId()
    {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String Title) {
        title = Title;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public double getDodgeChance() {
        return dodge;
    }

    public void setDodgeChance(double dodge) {
        this.dodge = dodge;
    }

    public double getPrecision() {
        return precision;
    }

    public void setPrecision(double precision) {
        this.precision = precision;
    }

    public Inventaire getInventaire() {
        return inventaire;
    }

    public String toString() {
        return "Character{" +
                "name='" + name + '\'' +
                ", health=" + health +
                ", title='" + title + '\'' +
                ", damage=" + damage +
                ", dodge=" + dodge +
                ", precision=" + precision +
                '}';
    }

    public void ajouterPersonnageAuUser(int idUser,ObjectId idPersonnage){
        try {
            // Initialisation de la connexion
            connexionDbNosql = Connexion_DB_Nosql.getInstance();
            mongoDatabase = connexionDbNosql.getDatabase();
            collection = mongoDatabase.getCollection("characters");

            // Ajout du document correspondant
            Document addQuery = new Document("_id", id);
            collection.insertOne(addQuery);

        } catch (Exception e) {
            System.out.println("Erreur lors de la suppression du personnage.");
            e.printStackTrace();
        }
    }

    public void removeCharacter(ObjectId characterId) {
        try {
            // Connexion à la DB
            connexionDbNosql = Connexion_DB_Nosql.getInstance();
            mongoDatabase = connexionDbNosql.getDatabase();

            // Récupération de la collection des personnages
            MongoCollection<Document> charactersCollection = mongoDatabase.getCollection("characters");

            // Récupération du document personnage AVANT suppression
            Document characterDoc = charactersCollection.find(new Document("_id", characterId)).first();

            if (characterDoc != null) {
                // Récupérer l'ID de l'inventaire à partir du champ "inventaire._id"
                Document inventaireDoc = characterDoc.get("inventaire", Document.class);
                ObjectId inventoryId= inventaireDoc.getObjectId("_id");

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

}