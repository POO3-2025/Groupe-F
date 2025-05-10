package be.helha.labos.collection.Character;

import be.helha.labos.DBNosql.Connexion_DB_Nosql;
import be.helha.labos.collection.Inventaire;
import be.helha.labos.collection.InventaireFactory;
import be.helha.labos.collection.User;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class CharactereFactory {

    public static CharacterType createCharacter(String type, String name) {
        CharacterType character = null;

        switch (type.toLowerCase()) {
            case "archer":
                character = new Archer(name);
                break;
            case "mage":
                character = new Knight(name);
                break;
            case "guerrier":
                character = new Orc(name);
                break;
            // autres types
        }

        if (character != null) {
            InventaireFactory inventaire = new InventaireFactory();
            character.setInventaire(inventaire.createInventaire(new Inventaire()).getId());
        }

        return character;
    }

    private static void saveCharacterInDatabase(CharacterType character) {
        // Code pour enregistrer un personnage dans la base de données
        MongoCollection<Document> collection = getCharacterCollection();
        Document characterDoc = new Document("_id", character.getId())
                .append("name", character.getName())
                .append("health", character.getHealth())
                .append("damage", character.getDamage())
                .append("dodge", character.getDodgeChance())
                .append("precision", character.getPrecision())
                .append("level", character.getLevel())
                .append("money", character.getMoney())
                .append("inventaire", new Document("_id", character.getInventaire()));

        collection.insertOne(characterDoc);
    }

    private static MongoCollection<Document> getCharacterCollection() {
        MongoDatabase db = new Connexion_DB_Nosql("nosqlTest").createDatabase();
        return db.getCollection("characters");
    }
}
