package com.zj.platform.gamecenter.dto.socket;

import com.zj.platform.gamecenter.entity.Player;
import com.zj.platform.gamecenter.entity.Seat;

public class SeatDto {

	private int index;// 下标

	private Boolean isOut;// 是否已出局

	private Boolean isCheck;// 是否已看牌

	private Boolean isLose;// 比牌是否为输

	private int status;// 等待、准备、游戏中

	private SeatPlayerDto player;// 座位上的玩家

	public SeatDto(Seat seat, Player player) {
		if (null != player) {
			this.player = new SeatPlayerDto(player);
			status = player.getStatus();
		}
		index = seat.getIndex();
		isOut = seat.getIsOut();
		isCheck = seat.getIsCheck();
		isLose = seat.getIsLose();
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Boolean getIsOut() {
		return isOut;
	}

	public void setIsOut(Boolean isOut) {
		this.isOut = isOut;
	}

	public Boolean getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(Boolean isCheck) {
		this.isCheck = isCheck;
	}

	public Boolean getIsLose() {
		return isLose;
	}

	public void setIsLose(Boolean isLose) {
		this.isLose = isLose;
	}

	public SeatPlayerDto getPlayer() {
		return player;
	}

	public void setPlayer(SeatPlayerDto player) {
		this.player = player;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
