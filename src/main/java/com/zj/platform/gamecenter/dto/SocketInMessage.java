package com.zj.platform.gamecenter.dto;

import com.zj.platform.gamecenter.utils.MD5Utils;

public class SocketInMessage<T> {

	private Integer id;

	private T content;

	private String token;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public T getContent() {
		return content;
	}

	public void setContent(T content) {
		this.content = content;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * main函数.
	 * 
	 * @param args
	 *            启动参数
	 * @throws Exception
	 *             Exception
	 */
	public static void main(String... args) throws Exception {
		String password = "a123123";
		System.out.println(MD5Utils.toMD5(password, "UTF-8"));
	}

}
