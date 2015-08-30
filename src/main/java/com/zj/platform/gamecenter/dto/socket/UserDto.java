package com.zj.platform.gamecenter.dto.socket;

public class UserDto {

	private Long id;// userId

	private String account;

	private String nickName;// 昵称

	private int status;

	private int type;// VIP等级

	private Long moneyAccountAmount;// 游戏币数量

	private Long ingotAccountAmount;// 元宝数量

	private Long attractive;// 魅力值

	private String token;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Long getAttractive() {
		return attractive;
	}

	public void setAttractive(Long attractive) {
		this.attractive = attractive;
	}

}
