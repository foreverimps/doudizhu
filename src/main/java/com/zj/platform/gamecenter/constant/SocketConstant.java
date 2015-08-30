package com.zj.platform.gamecenter.constant;

/**
 * socket操作类型
 */
public enum SocketConstant {

	HAND_SHAKE(1000, "握手"),

	LOGIN(1010, "登陆"),

	REG(1020, "注册"),

	USER_INFO(1030, "用户信息"),

	SERVER_SEND_MSG(1040, "服务器发送消息"),

	SEND_MSG(1050, "发送消息"),

	FEEDBACK(1060, "意见反馈"),

	TASK(1070, "任务"),

	OVER_TASK(1080, "完成任务"),

	GET_PRODUCTS(1090, "获取商品"),

	BUY_PRODUCT(1100, "购买商品"),

	HELP(1110, "帮助"),

	RANKING(1120, "排行榜"),

	GAME_LIST(1130, "游戏列表"),

	CREATE_GAME(1140, "创建游戏"),

	QUICK_START(1145, "进入房间/快速开始"),

	PLAY_GAME(1150, "玩游戏"),

	FRIENDS(1160, "朋友列表"),

	FRIENDS_ADD_ASK(1170, "请求添加好友"),

	FRIENDS_ADD_AGREE(1180, "同意添加好友"),

	FRIENDS_RENAME(1190, "修改好友备注"),

	FRIENDS_RELEIVE(1200, "解除好友");

	private int code;

	private String description;

	private SocketConstant(int code, String description) {
		this.code = code;
		this.description = description;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public static String getDescription(int code) {
		for (SocketConstant socketConstant : SocketConstant.values()) {
			if (code == socketConstant.getCode()) {
				return socketConstant.getDescription();
			}
		}
		return null;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
