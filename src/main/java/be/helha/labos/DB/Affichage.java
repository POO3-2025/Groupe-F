package be.helha.labos.DB;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Objects;

public class Affichage {
    public static void main(String[] args) {
        Gson gson = new Gson();
        Configuration configuration;

        String configFileName = "config.json"; // Assurez-vous qu'il est dans src/main/resources

        try (Reader reader = new InputStreamReader(
                Objects.requireNonNull(Affichage.class.getClassLoader().getResourceAsStream(configFileName)),
                StandardCharsets.UTF_8)) {

            // Chargement de la configuration
            configuration = gson.fromJson(reader, Configuration.class);

            // Construction de l'URL de connexion
            String url = "jdbc:" + configuration.getDBType() + "://" +
                    configuration.getDBCredentials().getHost() +
                    ":" + configuration.getDBCredentials().getPort() +
                    "/" + configuration.getDBCredentials().getDatabase();

            // Connexion à la base de données
            try (Connection conn = DriverManager.getConnection(url,
                    configuration.getDBCredentials().getUser(),
                    configuration.getDBCredentials().getPassword())) {
                System.out.println("Connexion réussie !");
            } catch (Exception e) {
                System.err.println("Échec de la connexion !");
                e.printStackTrace();
            }

            // Affichage des détails de configuration
            System.out.println("Type de connexion : " + configuration.getConnectionType());
            System.out.println("Type de base de données : " + configuration.getDBType());
            System.out.println("Nom de la base de données : " + configuration.getDBCredentials().getDatabase());

        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de la configuration.");
            e.printStackTrace();
        }
    }
}
