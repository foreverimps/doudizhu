package com.zj.platform.gamecenter.test;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.spring.ActiveMQConnectionFactory;

public class TestReciver {

	public static void main(String[] args) throws Exception {
		ConnectionFactory factory = new ActiveMQConnectionFactory();

		Connection connection = factory.createConnection();
		connection.start();

		Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);

		Destination destination = session.createQueue("my-test");

		MessageConsumer consumer = session.createConsumer(destination);
		int i = 0;
		while (i < 3) {
			i++;
			TextMessage msg = (TextMessage) consumer.receive();
			session.commit();
			System.out.println(msg.getText());
		}

		session.close();
		connection.close();
	}

}
