package be.helha.labos;

import be.helha.labos.DBNosql.Connexion_DB_Nosql;
import be.helha.labos.DB.*;

import be.helha.labos.Lanterna.Inscription;
import be.helha.labos.collection.Character.*;
import be.helha.labos.collection.Item.*;
import be.helha.labos.collection.User;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.io.ObjectInputFilter;
import java.sql.Connection;

import static be.helha.labos.DBNosql.MongoDB.readAllCollections;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;


public class Lancer {
    public static void main(String[] args) {
        Inscription inscription = new Inscription();
        inscription.Lancer();
    }
}
