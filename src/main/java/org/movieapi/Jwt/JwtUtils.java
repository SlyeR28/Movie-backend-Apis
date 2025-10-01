package org.movieapi.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {


    private static  String secretKey ;

    public JwtUtils() {
        SecureRandom random = new SecureRandom();
        byte [] key = new byte[32];
        random.nextBytes(key);
        secretKey = Base64.getEncoder().encodeToString(key);
    }

    public String generateToken(Map<String, Object> claims , UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*24))
                .signWith(getSignedKey() , SignatureAlgorithm.HS256)
                .compact();

    }
    private Key getSignedKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public Boolean validToken(String token ,  UserDetails userDetails){
        return (extractUsername(token).equals(userDetails.getUsername()) &&  !isTokenExpired(token));
    }
    public String extractUsername(String token){
        return extractClaim(token , Claims::getSubject);
    }

    public  Date extractExpiration(String token){
        return extractClaim(token , Claims::getExpiration);
    }

    public Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }





    public <T> T extractClaim(String token , Function<Claims , T> claimsResolvers){
        final Claims  claims = Jwts.parser()
                .setSigningKey(getSignedKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolvers.apply(claims);

    }

}
