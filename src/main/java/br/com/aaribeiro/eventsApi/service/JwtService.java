package br.com.aaribeiro.eventsApi.service;

import br.com.aaribeiro.eventsApi.document.UserDocument;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JwtService {

    @Value("${security.jwt.expire}")
    private String expiracao;

    @Value("${security.jwt.key-signature}")
    private String keySignature;

    public String generateToken(UserDocument userDocument){
        long expString = Long.parseLong(expiracao);
        LocalDateTime dateTimeExpire = LocalDateTime.now().plusMinutes(expString);
        Instant instant = dateTimeExpire.atZone(ZoneId.systemDefault()).toInstant();
        Date dateExpire = Date.from(instant);

        return Jwts
                .builder()
                .setSubject(userDocument.getEmail())
                .setExpiration(dateExpire)
                .signWith(SignatureAlgorithm.HS512, keySignature)
                .compact();
    }

    private Claims getClaims(String token) throws ExpiredJwtException {
        return Jwts
                .parser()
                .setSigningKey(keySignature)
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean tokenIsValid(String token){
        try {
            Claims claims = getClaims(token);
            Date dateExpire = claims.getExpiration();

            LocalDateTime localDateTime = dateExpire.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            return !LocalDateTime.now().isAfter(localDateTime);
        } catch (Exception e){
            return false;
        }
    }

    public String getUserEmailFromToken(String token) throws ExpiredJwtException {
        return (String) getClaims(token).getSubject();
    }
}
