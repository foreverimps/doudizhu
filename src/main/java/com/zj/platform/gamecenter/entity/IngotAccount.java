package com.zj.platform.gamecenter.entity;

import java.util.Date;
import java.io.Serializable;

public class IngotAccount implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

	private Long id;

	private Long userId;

	private Long amount;

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

	public Long getAmount(){
		return amount;
	}

	public void setAmount(Long amount){
		this.amount=amount;
	}

	public Date getUpdateTime(){
		return updateTime;
	}

	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}

}