package be.helha.labos.DB;

import be.helha.labos.collection.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class User_DAO {



    // Récupère l'instance de connexion
    Connexion_DB connexion = Connexion_DB.getInstance("mysql");

    // Vérifie si la connexion est bien ouverte
    Connection conn = connexion.getConnection();

    /*public void ajouterUser () {
        User user = new User("Doe","John");
        user.save;
    }*/

    public User_DAO() {
        creerTableUser(); // S'assure que la table existe au moment de l'initialisation
    }

    private void creerTableUser() {
        String createTableQuery = """
            CREATE TABLE IF NOT EXISTS User (
                ID INT AUTO_INCREMENT PRIMARY KEY,
                Pseudo VARCHAR(60),
                Password VARCHAR(60),
                IDPersonnage INT NOT NULL
            );
        """;
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createTableQuery);
            System.out.println("Table 'User' créée ou déjà existante.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de la table 'User'.");
            e.printStackTrace();
        }
    }

    public boolean ajouterUser(User user) {
        String sql = "INSERT INTO User (Pseudo, Password, IDPersonnage) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getPseudo());
            pstmt.setString(2, user.getPassword());
            pstmt.setInt(3, user.getIdPersonnage());

            int rows = pstmt.executeUpdate();
            return rows > 0; // Retourne vrai si au moins une ligne a été insérée
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'insertion de l'utilisateur.");
            e.printStackTrace();
            return false;
        }
    }

}
