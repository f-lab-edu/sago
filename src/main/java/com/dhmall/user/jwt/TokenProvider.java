package com.dhmall.user.jwt;

import com.dhmall.exception.TokenException;
import com.dhmall.exception.UserException;
import com.dhmall.user.dto.LoginDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class TokenProvider implements InitializingBean {

    private static final Logger logger = LogManager.getLogger(TokenProvider.class.getName());

    private static final String AUTHORITIES_KEY = "auth";
    private static final int TOKEN_EXPIRATION_TIME = 1000*60*30;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisTemplate<String, String> redisTemplate;
    private final String secret;
    private final Long tokenValidityInMilliSeconds;

    private Key key;

    public TokenProvider(AuthenticationManagerBuilder authenticationManagerBuilder,
                         RedisTemplate<String, String> redisTemplate,
                         @Value("${jwt.secret}") String secret, @Value("${jwt.token-validity-in-seconds}") Long tokenValidityInMilliSeconds) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.redisTemplate = redisTemplate;
        this.secret = secret;
        this.tokenValidityInMilliSeconds = tokenValidityInMilliSeconds * 1000;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(LoginDto user) {

        ValueOperations<String, String> tokenMap = redisTemplate.opsForValue();
        if(tokenMap.get(user.getUserId().trim()) != null) throw new UserException("이미 로그인 상태입니다.");

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("user"));

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserId(), user.getPassword(), grantedAuthorities);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(new Date(TOKEN_EXPIRATION_TIME + System.currentTimeMillis()))
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(new Date(tokenValidityInMilliSeconds + System.currentTimeMillis()))
                .compact();

        tokenMap.set(user.getUserId().trim(), refreshToken.trim());

        return accessToken;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        logger.info("claims: " + claims);

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("user"));

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public String getAuthorities() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            throw new TokenException("잘못된 형식의 JWT 입니다.");
        } catch (ExpiredJwtException e) {
            return false;
        } catch (UnsupportedJwtException e) {
            throw new TokenException("지원하지않는 JWT 입니다.");
        } catch (IllegalArgumentException e) {
            throw new TokenException("잘못된 JWT 입니다.");
        }
    }

    public String refreshToken(String userId) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String storedRefreshToken = valueOperations.get(userId);

        // 토큰 만료 여부
        if(!validateToken(storedRefreshToken) || !storedRefreshToken.equals(storedRefreshToken)) throw new TokenException("만료된 JWT 입니다.");

        String newAccessToken = Jwts.builder()
                .setSubject(getAuthentication().getName())
                .claim(AUTHORITIES_KEY, getAuthorities())
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(new Date(TOKEN_EXPIRATION_TIME + System.currentTimeMillis()))
                .compact();

        return newAccessToken;
    }
}
