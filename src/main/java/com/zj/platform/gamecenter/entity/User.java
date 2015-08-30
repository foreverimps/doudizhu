package com.zj.platform.gamecenter.entity;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	private Long id;

	private String account;

	private String nickName;// 昵称

	private String password;

	private int source;

	private int status;

	private int type;// VIP等级

	private String icon;

	private Date createTime;

	private Date updateTime;

	private Long moneyAccountAmount;

	private Long ingotAccountAmount;

	private Long attractive;// 魅力值

	/** 扩展字段 */
	private MoneyAccount moneyAccount;

	private IngotAccount ingotAccount;

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public MoneyAccount getMoneyAccount() {
		return moneyAccount;
	}

	public void setMoneyAccount(MoneyAccount moneyAccount) {
		moneyAccountAmount = moneyAccount.getAmount();
		this.moneyAccount = moneyAccount;
	}

	public IngotAccount getIngotAccount() {
		return ingotAccount;
	}

	public void setIngotAccount(IngotAccount ingotAccount) {
		ingotAccountAmount = ingotAccount.getAmount();
		this.ingotAccount = ingotAccount;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
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

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Long getAttractive() {
		return attractive;
	}

	public void setAttractive(Long attractive) {
		this.attractive = attractive;
	}

	public String getNickName() {
	    return nickName;
    }

	public void setNickName(String nickName) {
	    this.nickName = nickName;
    }

}