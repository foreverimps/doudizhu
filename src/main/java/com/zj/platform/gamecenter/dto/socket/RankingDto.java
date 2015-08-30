package com.zj.platform.gamecenter.dto.socket;

public class RankingDto {

	private String account;// 账号

	private int vocation;// 职业 1：穷人2：富豪

	private String vocationName;// 1：穷人2：富豪

	private Long amount;// 数量

	private int type;// 类型

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public int getVocation() {
		return vocation;
	}

	public void setVocation(int vocation) {
		this.vocation = vocation;
	}

	public String getVocationName() {
		return vocationName;
	}

	public void setVocationName(String vocationName) {
		this.vocationName = vocationName;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public int getType() {
	    return type;
    }

	public void setType(int type) {
	    this.type = type;
    }

}
