package com.zj.platform.gamecenter.test;

public class Task extends Thread {

	public Task() {
	}

	@Override
	public void run() {
		SocketClient client = new SocketClient();
		SocketIoHandler handler = new SocketIoHandler();
		client.setHandler(handler);
		client.setHost("192.168.0.143");
		client.setPort(8888);

		// if (client.connect()) {
		String message = "{\"company\":\"LS\",\"content\":{\"gameId\":\"TSCQ\"},\"game\":\"TSCQ\",\"id\":1000,\"key\":\"c0489f23fa60d6c8b21c07e42c31ab03\"}";
		// System.out.println("Client Send1: " + message);
		client.send(message);
		// System.out.println("Client Send2: " + message);
		// }
	}

}
