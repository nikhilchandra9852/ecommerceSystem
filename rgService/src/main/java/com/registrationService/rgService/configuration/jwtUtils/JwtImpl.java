package com.registrationService.rgService.configuration.jwtUtils;


import com.registrationService.rgService.service.UserService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;


// managing  the jwt token
@Component
public class JwtImpl {

    private static final Logger logger = LoggerFactory.getLogger(JwtImpl.class); // Logger for logging errors


    @Value("${jwtSecret}")
    private String jwtSecret;

    @Value("${jwtExpirationMs}")
    private String jwtExpireSec;

    /**
     * Generate a JWT token based on the provided authentication.
     *
     * @param authentication The authentication object containing user details.
     * @param roles
     * @return The generated JWT token as a string.
     */
    public String generateJwtToken(Authentication authentication, List<String> roles){
        // get the userDetails
        UserService userDetailsService = (UserService) authentication.getPrincipal();


        // build the jwt token
        return Jwts.builder()
                .setSubject(userDetailsService.getEmail())
                .setIssuedAt(new Date())
                .claim("roles",roles)
                .setExpiration( new Date(new Date().getTime() + Long.parseLong(jwtExpireSec)))
                .signWith(key() , SignatureAlgorithm.HS256)
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    // get Usernmame from jwt token
    public String getEmailfromJwtToken(String token){

        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Validate the given JWT token.
     *
     * @param authToken The JWT token to validate.
     * @return True if the token is valid, false otherwise.
     */
    public boolean validateJwtToken(String authToken) {
        try {
            // Parse the token and verify its signature
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true; // Token is valid
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage()); // Log invalid token error
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage()); // Log expired token error
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage()); // Log unsupported token error
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage()); // Log empty claims error
        }

        return false; // Token is invalid
    }


}
