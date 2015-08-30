package com.zj.platform.gamecenter.test;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.spring.ActiveMQConnectionFactory;

public class TestSender {

	/**
	 * main函数.
	 * 
	 * @param args
	 *            启动参数
	 * @throws Exception
	 *             Exception
	 */
	public static void main(String... args) throws Exception {
		ConnectionFactory factory = new ActiveMQConnectionFactory();

		Connection connection = factory.createConnection();

		Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);

		Destination destination = session.createQueue("my-test");

		MessageProducer producer = session.createProducer(destination);

		for (int i = 0; i < 3; i++) {
			TextMessage msg = session.createTextMessage("this is a test " + i);
			Thread.sleep(1000);
			producer.send(msg);
		}
		session.commit();
		session.close();
		connection.close();
	}
}
