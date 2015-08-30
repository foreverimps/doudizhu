package com.zj.platform.gamecenter.dto.socket;

public class MessageDto {

	private String content;// 消息内容,如果是语音，那么content的值为url

	private Long userId;// 接受者的用户ID

	private int type;// 0:语音 1：图片 2：文字

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
