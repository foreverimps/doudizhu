package com.zj.platform.gamecenter.entity;

import com.zj.platform.gamecenter.constant.EventTypeEnum.ThreeCardsEventTypeEnum;
import com.zj.platform.gamecenter.events.AyncUserEventPublisher;
import com.zj.platform.gamecenter.events.ThreeCardsEvent;
import com.zj.platform.gamecenter.service.RoomService;
import com.zj.platform.gamecenter.utils.ApplicationContextUtil;

public class Room {

	public static final int MAX_SEAT_COUNT = 7;

	private static AyncUserEventPublisher publisher = ApplicationContextUtil.getBean(AyncUserEventPublisher.class);

	private static RoomService roomService = ApplicationContextUtil.getBean(RoomService.class);

	private String id;

	private String code;

	private int type;

	private String password;// 密码

	private int bottomNote;// 底注

	private int maxNote;// 最大投注

	/** 已准备玩家 */
	private int readyCount;

	private int countDown;// 倒计时

	private int sitCount;

	public Room() {
		countDown = 0;
		readyCount = 0;
	}

	/**
	 * 创建一局游戏
	 */
	public void createBureauDto(Bureau bureau) {
		if (null != bureau && (!bureau.getIsEnd())) {
			return;
		}
		// 创建一局游戏条件
		boolean satisfied = false;
		if (readyCount >= 3) {
			satisfied = true;
		}
		if (satisfied) {
			// 创建一局诈金花并发牌
			bureau = new Bureau(this, bottomNote, maxNote);
			ThreeCardsEvent gameStartEvent = new ThreeCardsEvent(null, ThreeCardsEventTypeEnum.GAME_START_CARDS);
			gameStartEvent.setRoomCode(code);
			publisher.publishUserEvent(gameStartEvent);
			// 保存数据库 与发牌放入事件处理器中处理
			// 发牌 发牌结果考虑后期html方式，安全起见在看牌之前不发送至前台
		}
		// 重置准备玩家个数
		readyCount = 0;
		countDown = 0;
		roomService.updateRoomCountDown(code, countDown);
		roomService.updateRoomReadyCount(code, readyCount);
	}

	public int getReadyCount() {
		return readyCount;
	}

	public void setReadyCount(int readyCount) {
		this.readyCount = readyCount;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public int getSitCount() {
		return sitCount;
	}

	public void setSitCount(int sitCount) {
		this.sitCount = sitCount;
	}

}
