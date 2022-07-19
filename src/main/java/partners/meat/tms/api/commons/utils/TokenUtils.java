package partners.meat.tms.api.commons.utils;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import partners.meat.tms.api.v1.model.UsersEntity;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenUtils {

    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${jwt.refreshKey}")
    private String refreshKey;
    @Value("${jwt.dataKey}")
    private String dataKey;
    @Value("${jwt.token-validation-in-seconds}")
    private long expireDate;


    public String generateJwtToken(UsersEntity usersEntity) {
        return Jwts.builder()
                .setSubject(usersEntity.getUserId())
                .setHeader(createHeader())
                .setClaims(createClaims(usersEntity))
                .setExpiration(createExpireDate(expireDate))
                .signWith(SignatureAlgorithm.HS256, createSigningKey(secretKey))
                .compact();
    }

    public String  saveRefreshToken(UsersEntity usersEntity) {
        return Jwts.builder()
                .setSubject(usersEntity.getUserId())
                .setHeader(createHeader())
                .setClaims(createClaims(usersEntity))
                .setExpiration(createExpireDate(expireDate))
                .signWith(SignatureAlgorithm.HS256, createSigningKey(refreshKey))
                .compact();
    }

    public boolean isValidToken(String token) {
        log.info("isValidToken = {}", token);

        try {
            Claims accessClaims = getClaimsFormToken(token);
            log.info("Access expireTime = {}", accessClaims.getExpiration());
            log.info("Access userId = {}", accessClaims.get("userId"));

            return true;
        } catch (ExpiredJwtException exception) {
            log.error("Token Expired UserID = {} ", exception.getClaims().getSubject());
            return false;

        } catch (JwtException exception) {
            log.error("Token Tampered!");
            return false;

        } catch (NullPointerException exception) {
            log.error("Token is null!");
            return false;
        }
    }
    public boolean isValidRefreshToken(String token) {
        try {
            Claims accessClaims = getClaimsToken(token);
            log.info("Access expireTime = {}", accessClaims.getExpiration());
            log.info("Access userId = {}", accessClaims.get("userId"));
            return true;
        } catch (ExpiredJwtException exception) {
            log.error("Token Expired UserID = {} ", exception.getClaims().getSubject());
            return false;
        } catch (JwtException exception) {
            log.error("Token Tampered!");
            return false;
        } catch (NullPointerException exception) {
            log.error("Token is null!");
            return false;
        }
    }

    private Date createExpireDate(long expireDate) {
        long curTime = System.currentTimeMillis();
        return new Date(curTime + expireDate);
    }

    private Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();

        header.put("typ","ACCESS_TOKEN");
        header.put("alg", "HS256");
        header.put("regDate", System.currentTimeMillis());

        return header;
    }

    private Map<String, Object> createClaims(UsersEntity userEntity) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(dataKey, userEntity.getUserId());
        return claims;
    }

    private Key createSigningKey(String key) {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(key);
        return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    private Claims getClaimsFormToken(String token) {
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                .parseClaimsJws(token)
                .getBody();
    }

    private Claims getClaimsToken(String token) {
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(refreshKey))
                .parseClaimsJws(token)
                .getBody();
    }
}
