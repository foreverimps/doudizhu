package com.zj.platform.gamecenter.dto.socket;

public class GameResultDto {

	private Long userId;

	private Long toUserId;

	private int type;

	private Long winerUserId;

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

	public Long getWinerUserId() {
		return winerUserId;
	}

	public void setWinerUserId(Long winerUserId) {
		this.winerUserId = winerUserId;
	}

}
