
package be.helha.labos.Authentification;

import be.helha.labos.DB.User_DAO;
import be.helha.labos.collection.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

/**
 * Classe pour gérer l'authentification des utilisateurs.
 * Elle utilise un DAO pour interagir avec la base de données et un utilitaire JWT pour générer des tokens.
 */
public class Authen {
    /**
     * Instance de User_DAO pour interagir avec la base de données.
     */
    private final JwtUtils jwtUtils = new JwtUtils();

    /**
     * Méthode pour authentifier un utilisateur.
     *
     * @param pseudo      Le pseudo de l'utilisateur
     * @param motDePasse  Le mot de passe de l'utilisateur
     * @param Dbkey       La clé de la base de données
     * @return Un token JWT si l'authentification réussit
     */
    public String login(String pseudo, String motDePasse,String Dbkey) {
        final User_DAO user_DAO = new User_DAO(Dbkey);
        User user = user_DAO.getUserByPseudo(pseudo); // récupère le user custom

        if (user != null && User_DAO.PasswordUtils.verifyPassword(motDePasse, user.getPassword())) {
            UserDetailsImpl userDetails = new UserDetailsImpl(user);

            Authentication auth = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            if(!user_DAO.verifierConnexion(pseudo,motDePasse)){
                throw new RuntimeException("Utilisateur déjà connecté !");
            } 
            user_DAO.setUserActif(user.getId(), true);
            return jwtUtils.generateToken(auth);
        } else {
            throw new RuntimeException("Pseudo ou mot de passe invalide");
        }
    }
}
