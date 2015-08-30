package com.zj.platform.gamecenter.entity;

import java.util.Date;
import java.io.Serializable;

public class MoneyAccountLog implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

	private Long id;

	private Long moneyAccountId;

	private Long amount;

	private int optType;

	private String code;

	private Date createTime;

	private Date updateTime;

	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getMoneyAccountId(){
		return moneyAccountId;
	}

	public void setMoneyAccountId(Long moneyAccountId){
		this.moneyAccountId=moneyAccountId;
	}

	public Long getAmount(){
		return amount;
	}

	public void setAmount(Long amount){
		this.amount=amount;
	}

	public int getOptType(){
		return optType;
	}

	public void setOptType(int optType){
		this.optType=optType;
	}

	public String getCode(){
		return code;
	}

	public void setCode(String code){
		this.code=code;
	}

	public Date getCreateTime(){
		return createTime;
	}

	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}

	public Date getUpdateTime(){
		return updateTime;
	}

	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}

}