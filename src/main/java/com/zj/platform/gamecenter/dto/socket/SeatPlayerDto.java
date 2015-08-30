package com.zj.platform.gamecenter.dto.socket;

import org.apache.commons.lang3.StringUtils;

import com.zj.platform.gamecenter.entity.Player;

public class SeatPlayerDto {

	private Long userId;// 用户ID

	private String nickName;// 用户昵称，为空时取值为账号

	private Long moneyAccountAmount;// 金币的值（该值在游戏过程中保持不变，游戏结束或者离开房间时做计算）

	private Long sumNote;// 一局过程中玩家上的总注

	private Long ingotAccountAmount;// 元宝数量

	private String icon;// 头像

	private int type;// 账号等级类别

	private int gender;// 性别 0：女 1:男

	public SeatPlayerDto(Player player) {
		userId = player.getUserId();
		nickName = player.getNickName();
		if (StringUtils.isEmpty(nickName)) {
			nickName = player.getAccount();
		}
		moneyAccountAmount = player.getMoneyAccountAmount();
		sumNote = player.getSumNote();
		ingotAccountAmount = player.getIngotAccountAmount();
		// TODO
		icon = player.getIcon();
		type = player.getType();
		gender = 0;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Long getMoneyAccountAmount() {
		return moneyAccountAmount;
	}

	public void setMoneyAccountAmount(Long moneyAccountAmount) {
		this.moneyAccountAmount = moneyAccountAmount;
	}

	public Long getSumNote() {
		return sumNote;
	}

	public void setSumNote(Long sumNote) {
		this.sumNote = sumNote;
	}

	public Long getIngotAccountAmount() {
		return ingotAccountAmount;
	}

	public void setIngotAccountAmount(Long ingotAccountAmount) {
		this.ingotAccountAmount = ingotAccountAmount;
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

}
