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


    private final int id;
    private final String pseudo;
    private final String password;
    private final boolean actif;
    private final String role;
    /**
     * Constructeur de la classe UserDetailsImpl.
     *
     */
    public UserDetailsImpl(User user) {
        this.id = user.getId();
        this.pseudo = user.getPseudo();
        this.password = user.getPassword();
        this.actif = user.isActif();
        this.role = "ROLE_" + user.getRole();
    }

    public int getId() {
        return id;
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
