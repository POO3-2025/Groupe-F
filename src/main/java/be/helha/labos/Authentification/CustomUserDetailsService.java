package be.helha.labos.Authentification;

import be.helha.labos.DB.Connexion_DB;
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

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Connexion_DB factory = new Connexion_DB("mysql");

        try (Connection conn = factory.createConnection()) { // Utilisation du singleton
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE Pseudo = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String password = rs.getString("password");
                String role = rs.getString("role");

                return User.builder()
                        .username(username)
                        .password(password)
                        .authorities(Collections.singletonList(new SimpleGrantedAuthority("Role :" + role)))
                        .build();
            } else {
                throw new UsernameNotFoundException("Utilisateur non trouvé : " + username);
            }

        } catch (Exception e) {
            throw new UsernameNotFoundException("Erreur lors de la recherche de l'utilisateur : " + e.getMessage());
        }
    }
}