package dev.joseluisgs.tiendaapispringboot.utils.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Servicio de JWT
 */
@Service
@Slf4j
public class JwtServiceImpl implements JwtService {
    @Value("${jwt.secret}")
    private String jwtSigningKey;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;


    /**
     * Extrae el nombre de usuario del token
     *
     * @param token token
     * @return nombre de usuario
     */
    @Override
    public String extractUserName(String token) {
        log.info("Extracting username from token " + token);
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Genera un token
     *
     * @param userDetails Detalles del usuario
     * @return token
     */
    @Override
    public String generateToken(UserDetails userDetails) {
        log.info("Generating token for user " + userDetails.getUsername());
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Genera un token con datos extra
     *
     * @param token       token
     * @param userDetails Detalles del usuario
     * @return token
     */
    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        log.info("Validating token " + token + " for user " + userDetails.getUsername());
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Genera un token con datos extra
     *
     * @param token           token
     * @param claimsResolvers Detalles del usuario
     * @return token
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        log.info("Extracting claim from token " + token);
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    /**
     * Genera un token con datos extra
     *
     * @param extraClaims Datos extra
     * @param userDetails Detalles del usuario
     * @return token
     */
    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .setHeaderParam("typ", "JWT")
                .setClaims(extraClaims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (1000 * jwtExpiration)))
                .compact();
    }

    /**
     * Comprueba si el token ha expirado
     *
     * @param token token
     * @return true si ha expirado
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extrae la fecha de expiración del token
     *
     * @param token token
     * @return fecha de expiración
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extrae todos los claims del token
     *
     * @param token token
     * @return claims
     */
    private Claims extractAllClaims(String token) {
        log.info("Extracting all claims from token " + token);
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()).build().parseClaimsJws(token)
                .getBody();
    }

    /**
     * Obtiene la clave de firma
     *
     * @return clave de firma
     */
    private Key getSigningKey() {
        byte[] keyBytes = Base64.getEncoder().encode(jwtSigningKey.getBytes());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}