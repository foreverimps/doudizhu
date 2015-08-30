package com.zj.platform.gamecenter.dto.socket;

public class PlayGameDto {

	private String roomCode;// 房间code

	private int place;// 座位，必须，为当前玩家的座位号，0-6

	private Long userId;// 用户ID

	private int toPlace;// 对方座位号，比牌时需要

	private Long toUserId;// to用户id

	private int type;// 准备、比牌 、看牌 、弃牌 5：跟注、加注、占座、离座、换位置、离开房间

	private int amount;// 数量

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getToUserId() {
		return toUserId;
	}

	public void setToUserId(Long toUserId) {
		this.toUserId = toUserId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getRoomCode() {
		return roomCode;
	}

	public void setRoomCode(String roomCode) {
		this.roomCode = roomCode;
	}

	public int getToPlace() {
		return toPlace;
	}

	public void setToPlace(int toPlace) {
		this.toPlace = toPlace;
	}

	public int getPlace() {
		return place;
	}

	public void setPlace(int place) {
		this.place = place;
	}

}
