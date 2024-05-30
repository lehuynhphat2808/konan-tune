package com.example.demo.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;


@Service
public class JwtService {
    private static final String SECRET_KEY="z4DkqXUQOb7qaNxSR4oGXzcLMv6vrDHGsdj4Y64zzu4rHfD7LagQJWLvzLulWT6ogdrI8VWWnYqmSeWvmOZoALROZrNsuBeFbPdUEJKjRXz9tNHstc8qwii273gO3EBWrwtWcLu083aoqFjz57fKr9USuXWXMglhBA7jJs9QtYaUTtn1L6zHLgkw4gy0iJoOVEyBXoV2b8topdMITfbvzp4hbMsPGEngCE9quy0mq2Ncr3AB5iahzJ9r314ptbhTVAElO1oDlgm23fRmNQnpMQOCcVX0uZOASzloBtEjYkdjvZRh0NCSalwl0Eg7p3PHAKxYs6hEfiniKYGQwxnahBJrtBaQciaYHT4hkBQlIeO14DMFGHsgUm3vjCSlywJ2xqW1pgStgKuzFe2cFYdSfkNKDcOFIw1E1MhsEy2nhoAGCjDmbTiKqY8jqGE6mjbXPvCnL4nbY0JhyI1F9xzhTE9myJkiukAhugIs1QlmjZicl7HiDZEFEwod9b4OwgTJmFHnCwi5Aok0ssxF5jDVby8eul2LnOSnDMPtWaq97dAsZsMgJ6kMKKhu3Mc2BgzOidUbhqs6LRwuuO1ZF0HGTnIyAC5LU8jo18JmhdKx2fW18ENZPHfWlgxiCfTNo80aaiiUTrDn0fhkthudL1cTPC8YSRgEvfstKEnROpjyD1HQKWHg8LCFFoWZHMYbzVWo28zfqZkAiYOJThdzo62u7T9p8CgZ5AREeAizWdSzWj0g5mFIdbgZ0HBfSyxWvfQBOfMcVnk1Sl8DoaVgmWFWtKKkCJHkvwVlVBKjqLdYRoFrBElqq1iZ370ZKwSKiZJxsytyQDTRS8zQDQlNZ2Nc4MWG9HvhUsVaBxzEAZffw1JWU6ZF1iPp70xkNVI3M02eIjJAeuw7ChL4wBTZt56VwSYzdqfoShWCMiLHzh2yzC8mCIxYk4B2DIdznDUxlFg2j1szUaJd6AmlGu2ALrIm9dA6DLb0DUIKJpmYExAtTffS2HXrSkbnLTCPuZzxBfv8";
    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(),userDetails);
    }
    public String generateToken(String email) {
        return generateToken(email, new HashMap<>());
    }
    public String generateToken(String email, Map<String, Object> extraClaims) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(email) // Sử dụng email hoặc username làm dữ liệu chính trong token
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public String generateToken(
            Map<String, Objects> extraClaims,
            UserDetails userDetails
    ){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

    }
    public boolean isTokenValid(String token,UserDetails userDetails){
        final String username= extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    public boolean isTokenValid(String token, String email) {
        final String tokenEmail = extractUsername(token);
        return (tokenEmail.equals(email) && !isTokenExpired(token));
    }
    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }
    public Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
