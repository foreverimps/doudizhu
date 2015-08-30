package com.zj.platform.gamecenter.entity;

import java.util.Date;
import java.io.Serializable;

public class RankingLog implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

	private Long id;

	private Long userId;

	private String account;

	private Long amount;

	private int type;

	private Date createTime;

	private Date updateTime;

	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getUserId(){
		return userId;
	}

	public void setUserId(Long userId){
		this.userId=userId;
	}

	public String getAccount(){
		return account;
	}

	public void setAccount(String account){
		this.account=account;
	}

	public Long getAmount(){
		return amount;
	}

	public void setAmount(Long amount){
		this.amount=amount;
	}

	public int getType(){
		return type;
	}

	public void setType(int type){
		this.type=type;
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