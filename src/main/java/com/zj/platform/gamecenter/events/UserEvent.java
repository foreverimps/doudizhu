package com.zj.platform.gamecenter.events;

import com.zj.platform.gamecenter.entity.Seat;

/**
 * 用户事件基类 <br>
 * <p>
 * Copyright: Copyright (c) 2014年12月29日 下午8:39:23
 * <p>
 * Company:
 * <p>
 * 
 * @author H.L.Wan
 * @version 1.0.0
 */
public abstract class UserEvent {

	private final Long userId;

	private String roomCode;

	private Integer index;

	private Integer bureauCount;

	private Seat compareTo;

	private Integer note;

	private int round;

	private int curIndex;

	public String getRoomCode() {
		return roomCode;
	}

	public void setRoomCode(String roomCode) {
		this.roomCode = roomCode;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Integer getBureauCount() {
		return bureauCount;
	}

	public void setBureauCount(Integer bureauCount) {
		this.bureauCount = bureauCount;
	}

	public Seat getCompareTo() {
		return compareTo;
	}

	public void setCompareTo(Seat compareTo) {
		this.compareTo = compareTo;
	}

	public Integer getNote() {
		return note;
	}

	public void setNote(Integer note) {
		this.note = note;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public int getCurIndex() {
		return curIndex;
	}

	public void setCurIndex(int curIndex) {
		this.curIndex = curIndex;
	}

	public UserEvent(Long userId) {
		this.userId = userId;
	}

	public final Long getUserId() {
		return userId;
	}

	public abstract int getEventCode();

}
