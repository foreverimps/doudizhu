package com.zj.platform.gamecenter.dto.socket;

import com.zj.platform.gamecenter.entity.Bureau;
import com.zj.platform.gamecenter.entity.Room;
import com.zj.platform.gamecenter.entity.Seat;
import com.zj.platform.gamecenter.service.MongoCacheService;
import com.zj.platform.gamecenter.service.PlayerService;
import com.zj.platform.gamecenter.utils.ApplicationContextUtil;

public class RoomDto {

	private static MongoCacheService cacheService = ApplicationContextUtil.getBean(MongoCacheService.class);

	private static PlayerService playerService = ApplicationContextUtil.getBean(PlayerService.class);

	private String code;// 房间编号

	private int type;// 房间类型

	private String password;// 密码

	private int bottomNote;// 底注

	private int maxNote;// 最大投注 封顶多少

	private int countDown;// 倒计时

	private BureauDto bureau;// 房间中当前局

	private SeatDto[] seats;// 房间中的座位

	public RoomDto(Room room, Bureau bureau, Seat[] seats) {
		code = room.getCode();
		type = room.getType();
		password = room.getPassword();
		bottomNote = room.getBottomNote();
		maxNote = room.getMaxNote();
		countDown = room.getCountDown();
		if (null != bureau && (!bureau.getIsEnd())) {
			this.bureau = new BureauDto(bureau);
		}
		this.seats = new SeatDto[seats.length];
		for (int i = 0; i < seats.length; i++) {
			this.seats[i] = new SeatDto(seats[i], playerService.getPlayerByUserId(seats[i].getUserId()));
		}
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getBottomNote() {
		return bottomNote;
	}

	public void setBottomNote(int bottomNote) {
		this.bottomNote = bottomNote;
	}

	public int getMaxNote() {
		return maxNote;
	}

	public void setMaxNote(int maxNote) {
		this.maxNote = maxNote;
	}

	public int getCountDown() {
		return countDown;
	}

	public void setCountDown(int countDown) {
		this.countDown = countDown;
	}

	public BureauDto getBureau() {
		return bureau;
	}

	public void setBureau(BureauDto bureau) {
		this.bureau = bureau;
	}

	public SeatDto[] getSeats() {
		return seats;
	}

	public void setSeats(SeatDto[] seats) {
		this.seats = seats;
	}

}
