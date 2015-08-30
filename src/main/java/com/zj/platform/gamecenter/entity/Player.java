package com.zj.platform.gamecenter.entity;

import java.util.List;

import com.zj.platform.gamecenter.constant.PlayerStatusEnum;

/**
 * 用户进入房间后，根据当前用户与房间信息生成玩家对象<br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2015年1月1日 上午11:03:37
 * <p>
 * Company:
 * <p>
 * 
 * @author
 * @version 1.0.0
 */
public class Player {

	private Long userId;// 用户ID

	private String roomCode;

	private String nickName;

	private String account;

	private int bureauIndex;// 第几局

	private List<Card> cards;// 牌

	private int status;// 状态

	private Long moneyAccountAmount = 0L;

	private Long ingotAccountAmount = 0L;

	private Long sumNote;

	private int gender;

	private String icon;

	private int type;

	public Player(User user) {
		userId = user.getId();
		icon = user.getIcon();
		type = user.getType();
		gender = 0;
		// 初始化
		status = PlayerStatusEnum.WAIT.getCode();
		// TODO 其它信息
	}

	public Player() {
	}

	public Long getSumNote() {
		return sumNote;
	}

	public void setSumNote(Long sumNote) {
		this.sumNote = sumNote;
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

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public List<Card> getCards() {
		return cards;
	}

	public void setCards(List<Card> cards) {
		this.cards = cards;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Long getMoneyAccountAmount() {
		return moneyAccountAmount;
	}

	public void setMoneyAccountAmount(Long moneyAccountAmount) {
		this.moneyAccountAmount = moneyAccountAmount;
	}

	public Long getIngotAccountAmount() {
		return ingotAccountAmount;
	}

	public void setIngotAccountAmount(Long ingotAccountAmount) {
		this.ingotAccountAmount = ingotAccountAmount;
	}

	@Override
	public int hashCode() {
		return userId.hashCode();
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}
		if (!(obj instanceof Player)) {
			return false;
		}
		Player dto = (Player) obj;
		return dto.userId.equals(userId);
	}

	public int getBureauIndex() {
		return bureauIndex;
	}

	public void setBureauIndex(int bureauIndex) {
		this.bureauIndex = bureauIndex;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

}
