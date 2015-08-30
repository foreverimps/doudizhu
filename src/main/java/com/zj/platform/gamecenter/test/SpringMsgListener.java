package com.zj.platform.gamecenter.test;

import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.jms.listener.SessionAwareMessageListener;

public class SpringMsgListener implements SessionAwareMessageListener<TextMessage> {

	private Destination destination;

	public void onMessage(TextMessage message, Session session) {
		try {
			System.out.println("消息监听：" + message.getText());
			MessageProducer producer = session.createProducer(destination);
			Message textMessage = session.createTextMessage("ConsumerSessionAwareMessageListener。。。");
			producer.send(textMessage);
		}
		catch (Exception ex) {
		}
	}

	public Destination getDestination() {
		return destination;
	}

	public void setDestination(Destination destination) {
		this.destination = destination;
	}
}
