package be.helha.labos.Lanterna;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
/**
 * Classepour lancer l'application.
 * Elle initialise une instance de la classe Inscription et appelle la méthode Lancer.
 */
public class Lancer {
    public static void main(String[] args) {
        Inscription inscription = new Inscription();
        inscription.Lancer();
    }
}
