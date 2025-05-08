package be.helha.labos.Authentification;

import be.helha.labos.DB.User_DAO;
import be.helha.labos.collection.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


public class Authen {

    private final JwtUtils jwtUtils = new JwtUtils();
    private final User_DAO user_DAO = new User_DAO("mysql");

    public String login(String pseudo, String motDePasse) {
        User user = user_DAO.GetUserByPseudo(pseudo); // récupère ton user custom

        if (user != null && User_DAO.PasswordUtils.verifyPassword(motDePasse, user.getPassword())) {
            UserDetailsImpl userDetails = new UserDetailsImpl(user);

            Authentication auth = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            return jwtUtils.generateToken(auth);
        } else {
            throw new RuntimeException("Pseudo ou mot de passe invalide");
        }
    }
}