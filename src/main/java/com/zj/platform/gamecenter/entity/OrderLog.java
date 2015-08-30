package com.zj.platform.gamecenter.entity;

import java.util.Date;
import java.io.Serializable;

public class OrderLog implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

	private Long id;

	private Long orderId;

	private Long userId;

	private String code;

	private Long amount;

	private int status;

	private Date createTime;

	private Date updateTime;

	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getOrderId(){
		return orderId;
	}

	public void setOrderId(Long orderId){
		this.orderId=orderId;
	}

	public Long getUserId(){
		return userId;
	}

	public void setUserId(Long userId){
		this.userId=userId;
	}

	public String getCode(){
		return code;
	}

	public void setCode(String code){
		this.code=code;
	}

	public Long getAmount(){
		return amount;
	}

	public void setAmount(Long amount){
		this.amount=amount;
	}

	public int getStatus(){
		return status;
	}

	public void setStatus(int status){
		this.status=status;
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