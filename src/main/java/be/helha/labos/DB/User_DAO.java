package be.helha.labos.DB;

import be.helha.labos.collection.User;

import org.springframework.security.crypto.bcrypt.BCrypt;

import java.sql.*;

public class User_DAO {

    // Récupère l'instance de connexion
    Connexion_DB connexion = Connexion_DB.getInstance("mysql");

    // Vérifie si la connexion est bien ouverte
    Connection conn = connexion.getConnection();

    public class PasswordUtils {
        public static String hashPassword(String plainPassword) {
            return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        }

        public static boolean verifyPassword(String plainPassword, String hashedPassword) {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        }
    }

    public User_DAO() {
        creerTableUser(); // S'assure que la table existe au moment de l'initialisation
    }

    private void creerTableUser() {
        String createTableQuery = """
            CREATE TABLE IF NOT EXISTS User (
                ID INT AUTO_INCREMENT PRIMARY KEY,
                Pseudo VARCHAR(60),
                Password VARCHAR(60),
                ROLE VARCHAR(60),
                Actif BOOL 
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
        String sql = "INSERT INTO User (Pseudo, Password, Role, Actif) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            pstmt.setString(1, user.getPseudo());
            // 🔐 Hashage du mot de passe
            String hashedPassword = PasswordUtils.hashPassword(user.getPassword());
            pstmt.setString(2, hashedPassword);
            pstmt.setString(3, user.getRôle());
            pstmt.setBoolean(4, user.isActif());

            if(GetUserByPseudo(user.getPseudo()) == null) {
                pstmt.executeUpdate();
                // Récupération de l'ID généré
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    user.setId(id); // Met à jour l'objet avec l'id attribué
                }
                return true;
            }
            else {
                throw new RuntimeException("Un utilisateur avec ce pseudo existe déjà");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'insertion de l'utilisateur.");
            e.printStackTrace();
            return false;
        }
    }

    public User GetUserByPseudo(String pseudo) {
        User user = null;
        String query = "SELECT DISTINCT * FROM User WHERE Pseudo = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, pseudo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Création d'une instance de User à partir des données de la base
                user = new User(
                        rs.getString("Pseudo"),              // Pseudo
                        rs.getString("Password"),            // Mot de passe
                        rs.getString("ROLE")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public User GetUserById(int id) {
        User user = null;
        String query = "SELECT DISTINCT * FROM User WHERE ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Création d'une instance de User à partir des données de la base
                user = new User(
                        rs.getInt("ID"),
                        rs.getString("Pseudo"),              // Pseudo
                        rs.getString("Password"),            // Mot de passe
                        rs.getString("ROLE")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }


    public boolean verifierConnexion(String pseudo, String password) {
        String sql = "SELECT Password FROM User WHERE Pseudo = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pseudo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String hashed = rs.getString("Password");
                return PasswordUtils.verifyPassword(password, hashed);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void supprimerTableUser(){
        String sql = "TRUNCATE TABLE  User";
        try(PreparedStatement pstmt = conn.prepareStatement(sql)){

            System.out.println("Table 'User' supprimée.");
            pstmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
