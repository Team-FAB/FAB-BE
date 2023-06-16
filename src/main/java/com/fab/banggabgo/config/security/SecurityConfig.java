package com.fab.banggabgo.config.security;

import com.fab.banggabgo.common.exception.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtTokenProvider jwtTokenProvider;
  private final RedisTemplate<String, String> redisTemplate;

  @Bean
  public SecurityFilterChain defaultFilter(HttpSecurity http) throws Exception {
    http.csrf().disable();
    http.httpBasic().disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        .and()
        .authorizeHttpRequests()
        .antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**").permitAll()
        .antMatchers(HttpMethod.GET ,"/api/exception").permitAll()
        .antMatchers("/api/users/**").permitAll()
        .antMatchers(HttpMethod.GET ,"/api/articles/**").permitAll()
        .antMatchers("/login/oauth2/**").permitAll()
        .anyRequest().authenticated()

        .and()
        .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())

        .and()
        .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, redisTemplate),
            UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}
