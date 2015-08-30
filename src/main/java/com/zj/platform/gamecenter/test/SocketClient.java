package com.zj.platform.gamecenter.test;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.logging.MdcInjectionFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class SocketClient {

	private IoHandler handler = null;

	private IoSession session;

	private String host;

	private int port;

	public IoHandler getHandler() {
		return handler;
	}

	public void setHandler(IoHandler handler) {
		this.handler = handler;
	}

	public IoSession getSession() {
		return session;
	}

	public void setSession(IoSession session) {
		this.session = session;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean connect() {
		if (session != null && session.isConnected()) {
			return true;
		}
		SocketAddress address = new InetSocketAddress(host, port);
		NioSocketConnector connector = new NioSocketConnector();
		try {

			connector.getFilterChain().addLast("mdc", new MdcInjectionFilter());

			connector.setHandler(handler);
			ConnectFuture future1 = connector.connect(address);
			future1.awaitUninterruptibly();
			if (!future1.isConnected()) {
				return false;
			}
			session = future1.getSession();
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	class Keep extends Thread {

		private IoSession session;

		public Keep(IoSession session) {
			this.session = session;
		}

		@Override
		public void run() {
			while (true) {
				System.out.println("keep start");
				try {
					sleep(3000);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (connect()) {
					session.write("");
					System.out.println("keep send null string");
				}
			}
		}
	}

	public boolean send(Object message) {
		if (session == null || session.isConnected()) {
			// Keep keep = new Keep(session);
			// keep.start();
		}
		session.write(message);
		return true;
	}

	public void close() {
		if (session != null) {
			if (session.isConnected()) {
				// Wait until the chat ends.
				session.getCloseFuture().awaitUninterruptibly();
			}
			session.close();
		}
	}

	public static void main(String[] args) {

		// SocketClient client = new SocketClient();
		// SocketIoHandler handler = new SocketIoHandler();
		// client.setHandler(handler);
		// client.setHost("58.210.12.90");
		// client.setPort(8707);
		//
		// if (client.connect()) {
		// String message =
		// "{\"company\":\"LSaa\",\"content\":{\"gameId\":\"TSCQaa\"},\"game\":\"TSCQaa\",\"id\":1000,\"key\":\"c0489f23fa60d6c8b21c07e42c31ab03\"}";
		// System.out.println("Client Send1: " + message);
		// client.send(message);
		// System.out.println("Client Send2: " + message);
		// }

		for (int i = 0; i < 100; i++) {
			Task t = new Task();
			t.start();
		}

		// List<String> content = new ArrayList<String>();
		// content.add("1");
		// content.add("2");
		// JSONObject json = new JSONObject();
		// json.put("id", "1109");
		// json.put("company", "YBSM");
		// json.put("game", "AHZG");
		// json.put("server", "1");
		// json.put("content", content);
		// System.out.println(json.toJSONString());
	}
}
