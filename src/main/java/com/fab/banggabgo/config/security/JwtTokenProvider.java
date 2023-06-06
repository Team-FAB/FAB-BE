package com.fab.banggabgo.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

  @Value("${jwt.auth.atk}")
  private String ACCESS_TOKEN_HEADER;

  @Value("${jwt.auth.rtk}")
  private String REFRESH_TOKEN_HEADER;
  @Value("${jwt.secretKey}")
  private String secretKey;

  private final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 60; //60m
  private final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24;//24h
  private final UserDetailsService userDetailsService;


  @PostConstruct
  protected void init() {
    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
  }

  public String createAccessToken(String email, List<String> roles) {

    Claims claims = Jwts.claims().setSubject(email);
    claims.put("roles", roles);

    Date now = new Date();
    String token = Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION))
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact();
    return token;
  }

  public String createRefreshToken() {//rtk
    Date now = new Date();
    String token = Jwts.builder()
        .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION))
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact();
    return token;
  }

  public Authentication getAuthentication(String token) { // token 정보 검사

    UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUser(token));

    String info = Jwts.parser()
        .setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();

    return new UsernamePasswordAuthenticationToken(userDetails, "",
        userDetails.getAuthorities());
  }

  public String getUser(String token) {
    String info = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody()
        .getSubject();
    return info;
  }

  public String resolveAtk(HttpServletRequest request) {
    return request.getHeader(ACCESS_TOKEN_HEADER);
  }

  public String resolveRtk(HttpServletRequest request) {
    return request.getHeader(REFRESH_TOKEN_HEADER);
  }

  public boolean validateToken(String token) {
    try { // check validate  with secretKey
      Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
      return !claims.getBody().getExpiration().before(new Date());
    } catch (Exception e) {
      return false;
    }
  }

  public Long getExpiration(String token) {
    Date expiration = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody()
        .getExpiration();
    long now = new Date().getTime();
    return expiration.getTime() - now;
  }
}
