package com.zj.platform.gamecenter.dto.socket;

public class ServerDto {

	private String ip;// 服务端IP

	private String port;// 服务端端口

	public ServerDto() {

	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

}
