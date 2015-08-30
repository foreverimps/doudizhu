package com.zj.platform.gamecenter.dto;

public class SocketOutMessage {

	private Integer id;// 业务ID

	private Integer code;// 返回码

	private Object content;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

}
