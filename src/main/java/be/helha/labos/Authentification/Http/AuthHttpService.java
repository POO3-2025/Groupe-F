package be.helha.labos.Authentification.Http;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AuthHttpService {

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     *  Méthode qui appele une requête dans AuthController
     * @param jwtToken le token du user après authentificatio
     * @return
     */
    public UserHttp getUserFromToken(String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<UserHttp> response = restTemplate.exchange(
                    "http://localhost:8080/whoami",
                    HttpMethod.GET,
                    requestEntity,
                    UserHttp.class
            );

            return response.getBody();
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération de l'utilisateur : " + e.getMessage());
            return null;
        }
    }
}
