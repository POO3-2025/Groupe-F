package be.helha.labos.Authentification;

import be.helha.labos.DB.Connexion_DB;
import be.helha.labos.DB.User_DAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;

/**
 * Classe de service pour gérer les détails de l'utilisateur.
 * Elle implémente l'interface UserDetailsService de Spring Security.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * Constructeur.
     */
    @Override
    /**
     * Méthode pour charger les détails de l'utilisateur par son nom d'utilisateur.
     *
     * @param username le nom d'utilisateur
     * @return les détails de l'utilisateur
     * @throws UsernameNotFoundException si l'utilisateur n'est pas trouvé
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Connexion_DB factory = new Connexion_DB("mysql");

        try (Connection conn = factory.createConnection()) { // Utilisation du singleton
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE Pseudo = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            User_DAO userDao = new User_DAO("mysql");

            if (rs.next()) {
                int id = rs.getInt("id");
                String pseudo = rs.getString("pseudo");
                String password = rs.getString("password");
                String role = rs.getString("role");

                be.helha.labos.collection.User user = new be.helha.labos.collection.User();
                user.setId(id);
                user.setPseudo(pseudo);
                user.setPassword(password);
                user.setRole(role);
                user.setActif(true);

                return new UserDetailsImpl(user);
            } else {
                throw new UsernameNotFoundException("Utilisateur non trouvé : " + username);
            }

        } catch (Exception e) {
            throw new UsernameNotFoundException("Erreur lors de la recherche de l'utilisateur : " + e.getMessage());
        }
    }
}