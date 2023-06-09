package com.fab.banggabgo.config.security;

import com.fab.banggabgo.config.security.OAuth2.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtTokenProvider jwtTokenProvider;
  private final RedisTemplate<String,String> redisTemplate;


  @Bean
  public SecurityFilterChain defaultFilter(HttpSecurity http) throws Exception {
    http.csrf().disable();
    http.httpBasic().disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        .and()
        .authorizeHttpRequests()
        .antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**").permitAll()
        .antMatchers("/api/users/**").permitAll()
        .antMatchers("/api/article/**").permitAll()
        .anyRequest().authenticated()

        .and()
        .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider,redisTemplate),
            UsernamePasswordAuthenticationFilter.class)
        .formLogin()
        .disable()
        .oauth2Login()
        .authorizationEndpoint()
        .baseUri("/login/oauth2")
        .and()
        .successHandler(new OAuth2SuccessHandler(jwtTokenProvider))
        .failureUrl("/api/v2/test")

        .and()
        .exceptionHandling()
        .and()
        .addFilterBefore(new SecurityExceptionFilter(), OAuth2AuthorizationRequestRedirectFilter.class)
    ;
    return http.build();
  }
}
