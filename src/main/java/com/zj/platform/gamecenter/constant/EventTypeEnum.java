package com.zj.platform.gamecenter.constant;

/**
 * 事件类型枚举 <br>
 * 用来标识事件是哪种动作引起. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2014年12月29日 下午8:04:45
 * <p>
 * Company:
 * <p>
 * 
 * @author H.L.Wan
 * @version 1.0.0
 */
public class EventTypeEnum {

	/**
	 * 系统事件枚举
	 */
	public static enum SystemEventTypeEnum {

		REGISTER(0, "注册"),

		LOGIN(10, "登录"),

		RECHARGE(30, "充值"),

		CHANGE(40, "兑换"),

		BUY(50, "购买"), ;

		private SystemEventTypeEnum(int code, String description) {
			this.code = code;
			this.description = description;
		}

		private final int code;

		private final String description;

		public int getCode() {
			return code;
		}

		public String getDescription() {
			return description;
		}

	}

	public static enum ThreeCardsEventTypeEnum {

		ROMM_IN(1000, "进入房间"),

		SIT(1010, "坐下"),

		STAND(1011, "离座"),

		READY(1020, "准备"),

		GAME_START_CARDS(1030, "一局开始，发牌"),

		NOTES_IN(1040, "跟注"),

		NOTES_NEXT(1041, "下一个"),

		NOTES_OUT(1050, "弃牌"),

		NOTES_ADD(1060, "加注"),

		CHECK_CARD(1070, "看牌"),

		COMPARE_CARDS(1080, "比牌"),

		COMPARE_WIN(1090, "比牌胜"),

		COMPARE_LOSE(1100, "比牌输"),

		ROUND_START(1110, "一轮开始"),

		ROUND_END(1120, "一轮结束"),

		GAME_WIN(1130, "胜一局"),

		GAME_LOSE(1140, "输一局"),

		GAME_END(1150, "一局结束"),

		COUNT_DOWN_BEFORE_BUREAU(1160, "开局前倒计时"),

		BUREAU_COUNT(1165, "在同一房间连续玩"),

		;

		private ThreeCardsEventTypeEnum(int code, String description) {
			this.code = code;
			this.description = description;
		}

		private final int code;

		private final String description;

		public int getCode() {
			return code;
		}

		public String getDescription() {
			return description;
		}
	}

}
