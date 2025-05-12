package be.helha.labos.Authentification;

/**
 * Classe représentant la réponse d'authentification.
 * Elle contient un token et un message.
 */
public class AuthResponse {
    /**
     * Constructeur de la classe AuthResponse.
     */
    private String token;
    private String message;

    public AuthResponse(String token, String message) {
        this.token = token;
        this.message = message;
    }

    // Getters et setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}