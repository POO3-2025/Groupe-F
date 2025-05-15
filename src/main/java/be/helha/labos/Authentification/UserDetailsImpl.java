package be.helha.labos.Authentification;

import be.helha.labos.collection.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Classe qui implémente l'interface UserDetails de Spring Security.
 * Elle représente les détails d'un utilisateur pour l'authentification.
 */
public class UserDetailsImpl implements UserDetails {

    /**
     * Constructeur de la classe UserDetailsImpl.
     *
     * @param user l'utilisateur à partir duquel créer les détails
     */
    private final String pseudo;
    private final String password;
    private final boolean actif;
    private final String role;

    public UserDetailsImpl(User user) {
        this.pseudo = user.getPseudo();
        this.password = user.getPassword();
        this.actif = user.isActif();
        this.role = user.getRole();
    }

    /**
     * Méthode pour obtenir le pseudo de l'utilisateur.
     *
     * @return le pseudo de l'utilisateur
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role)); // Exemple : "ROLE_USER"
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return pseudo;
    }

    @Override
    public boolean isAccountNonExpired() {
        return actif;
    }

    @Override
    public boolean isAccountNonLocked() {
        return actif;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return actif;
    }

    @Override
    public boolean isEnabled() {
        return actif;
    }
}
