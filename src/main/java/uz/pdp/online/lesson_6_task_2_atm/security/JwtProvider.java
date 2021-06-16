package uz.pdp.online.lesson_6_task_2_atm.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import uz.pdp.online.lesson_6_task_2_atm.entity.Role;

import java.util.Date;
import java.util.Set;

@Component
public class JwtProvider {

    private static final long expireDate = 1000 * 60 * 60 * 24;
    private static final String secretKey = "birnimalar";

    public String generateToken(String username, Set<Role> roles) {
        Date expireDate = new Date(System.currentTimeMillis() + JwtProvider.expireDate);
        String token = Jwts
                .builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .claim("roles", roles)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
        return token;
    }

    public String getEmailFromToken(String token) {
        try {
            String email = Jwts
                    .parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            return email;
        } catch (Exception e) {
            return null;
        }
    }

}
