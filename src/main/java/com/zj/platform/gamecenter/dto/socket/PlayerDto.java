package com.zj.platform.gamecenter.dto.socket;

import java.util.List;

import com.zj.platform.gamecenter.entity.Card;

public class PlayerDto {

	private Long userId;// 用户ID

	private String account;

	private List<Card> cards;// 牌

	private int status;// 状态

	private int place;// 座位号

	private int note;// 下的注数

	private int optTime;// 操作时间

	private Long moneyAccountAmount = 0L;

	private Long ingotAccountAmount = 0L;

	private int hasLook = 0;// 是否看牌,默认没看

	private int isCurrWork = 0;// 是否为当前的操作者

	private int type;// 操作类型

	private int bottomNote;// 底注

	public PlayerDto() {

	}

	public PlayerDto(Long userId, List<Card> cards, int status, int place, String account) {
		super();
		this.userId = userId;
		this.cards = cards;
		this.status = status;
		this.place = place;
		this.account = account;
	}

	public PlayerDto(Long userId, int status, int place, String account) {
		super();
		this.userId = userId;
		this.status = status;
		this.place = place;
		this.account = account;
	}

	public int getPlace() {
		return place;
	}

	public void setPlace(int place) {
		this.place = place;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

	public int getNote() {
		return note;
	}

	public void setNote(int note) {
		this.note = note;
	}

	public int getHasLook() {
		return hasLook;
	}

	public void setHasLook(int hasLook) {
		this.hasLook = hasLook;
	}

	public int getIsCurrWork() {
		return isCurrWork;
	}

	public void setIsCurrWork(int isCurrWork) {
		this.isCurrWork = isCurrWork;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getBottomNote() {
		return bottomNote;
	}

	public void setBottomNote(int bottomNote) {
		this.bottomNote = bottomNote;
	}

	public int getOptTime() {
		return optTime;
	}

	public void setOptTime(int optTime) {
		this.optTime = optTime;
	}

}
