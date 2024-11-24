package com.example.JoinGoREST.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {

		registry.addEndpoint("/joinGoSB").setAllowedOrigins("http://10.0.2.2:8080", "http://localhost:8080").withSockJS();
		//.setAllowedOriginPatterns("*")
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/ all","/specific");// Prefix for messages from server to client
		config.setApplicationDestinationPrefixes("/joinGoapp");// Prefix for messages from client to server
	}

	//.setHeartbeatValue(new long[]{10000, 10000})
}
