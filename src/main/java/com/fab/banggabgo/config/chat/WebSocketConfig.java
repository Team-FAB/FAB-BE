package com.fab.banggabgo.config.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  @Value("${spring.rabbitmq.port.stomp}")
  int stomp_port;
  @Value("${spring.rabbitmq.username}")
  String username;
  @Value("${spring.rabbitmq.password}")
  String pass;
  @Value("${spring.rabbitmq.host}")
  String host;

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry
        .setApplicationDestinationPrefixes("/pub")
        .enableStompBrokerRelay("/topic")
        .setRelayHost(host)
        .setRelayPort(stomp_port)
        .setVirtualHost("/")
        .setSystemLogin(username)
        .setSystemPasscode(pass)
        .setClientLogin(username)
        .setClientPasscode(pass)
    ;
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry
        .addEndpoint("/ws")
        .setAllowedOriginPatterns("*");
    registry
        .addEndpoint("/ws")
        .setAllowedOriginPatterns("*")
        .withSockJS();
  }
}
