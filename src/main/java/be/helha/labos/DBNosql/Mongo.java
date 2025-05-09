package be.helha.labos.DBNosql;

import com.mongodb.client.MongoDatabase;

/**
 * Interface représentant une connexion à une base de données MongoDB.
 * Elle est utilisée pour définir les méthodes de connexion et d'interaction avec la base de données.
 */
public interface Mongo {
    MongoDatabase createDatabase();
}
