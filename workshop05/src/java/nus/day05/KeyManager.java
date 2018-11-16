package nus.day05;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class KeyManager {
    private Key key;
    
    @PostConstruct
    private void init() {
        key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }
    
    public Key getKey() {
        return(key);
    }
    
}
