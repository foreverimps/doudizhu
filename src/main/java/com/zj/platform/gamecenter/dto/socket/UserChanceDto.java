package com.zj.platform.gamecenter.dto.socket;

public class UserChanceDto {

	private Long regDays;// 注册天数

	private int vipLevel;// vip等级

	private Long moneyAccount;// 金钱数量

	private Long rechargeAmount;// 充值数量

	private int friendNum;// 好友数量

	public Long getRegDays() {
		return regDays;
	}

	public void setRegDays(Long regDays) {
		this.regDays = regDays;
	}

	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}

	public Long getMoneyAccount() {
		return moneyAccount;
	}

	public void setMoneyAccount(Long moneyAccount) {
		this.moneyAccount = moneyAccount;
	}

	public Long getRechargeAmount() {
		return rechargeAmount;
	}

	public void setRechargeAmount(Long rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}

	public int getFriendNum() {
		return friendNum;
	}

	public void setFriendNum(int friendNum) {
		this.friendNum = friendNum;
	}

}
