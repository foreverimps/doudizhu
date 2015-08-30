package com.zj.platform.gamecenter.entity;

public class SocketTask {

	private String id;

	private String roomCode;

	private Long userId;

	private int type;

	private int commonId;

	private String content;// jsonString

	private int status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRoomCode() {
		return roomCode;
	}

	public void setRoomCode(String roomCode) {
		this.roomCode = roomCode;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getCommonId() {
		return commonId;
	}

	public void setCommonId(int commonId) {
		this.commonId = commonId;
	}

}
