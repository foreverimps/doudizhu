package com.zj.platform.gamecenter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.zj.platform.gamecenter.handler.WebGameSocketHandler;

@Configuration
@EnableWebMvc
@EnableWebSocket
public class WebSocketController extends WebMvcConfigurerAdapter implements WebSocketConfigurer {

	@Autowired
	private WebGameSocketHandler webGameSocketHandler;

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(webGameSocketHandler, "/web_game");
		registry.addHandler(webGameSocketHandler, "/sockjs/web_game").withSockJS();
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
}
