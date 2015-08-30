package com.zj.platform.gamecenter.dto.socket;

/**
 * 前台动作类 <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2014年12月30日 下午11:35:26
 * <p>
 * Company:
 * <p>
 * 
 * @author H.L.Wan
 * @version 1.0.0
 */
public class ActionDto {

	private Long userId;

	private int commandId;

	private Object content;

	private Integer nextOptIndex;

	private Long nextOptUserId;

	private Integer round;

	private String roomCode;

	public String getRoomCode() {
		return roomCode;
	}

	public void setRoomCode(String roomCode) {
		this.roomCode = roomCode;
	}

	public Integer getRound() {
		return round;
	}

	public void setRound(Integer round) {
		this.round = round;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public int getCommandId() {
		return commandId;
	}

	public void setCommandId(int commandId) {
		this.commandId = commandId;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	public Integer getNextOptIndex() {
		return nextOptIndex;
	}

	public void setNextOptIndex(Integer nextOptIndex) {
		this.nextOptIndex = nextOptIndex;
	}

	public Long getNextOptUserId() {
		return nextOptUserId;
	}

	public void setNextOptUserId(Long nextOptUserId) {
		this.nextOptUserId = nextOptUserId;
	}

}
