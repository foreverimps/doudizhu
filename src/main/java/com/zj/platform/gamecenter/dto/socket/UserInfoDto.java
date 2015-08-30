package com.zj.platform.gamecenter.dto.socket;

import java.util.List;

import com.zj.platform.gamecenter.entity.Product;

public class UserInfoDto {

	private Long id;// userId

	private String account;

	private String nickName;// 昵称

	private int icon;

	private int type;// VIP等级

	private Long moneyAccountAmount;// 游戏币数量

	private Long ingotAccountAmount;// 元宝数量

	private Long meiLi;// 魅力

	private String qianMing;

	private int sex;

	private String shengfu;

	private Long OneWinMoney;// 单把赢最多

	private int[] bigCards;//

	private String contact;

	private String address;

	private String rangking;// 排名

	private List<Product> productList;

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

}
