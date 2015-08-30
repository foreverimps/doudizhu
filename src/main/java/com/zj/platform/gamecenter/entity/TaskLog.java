package com.zj.platform.gamecenter.entity;

import java.util.Date;
import java.io.Serializable;

public class TaskLog implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

	private Long id;

	private String code;

	private Long taskId;

	private Long userId;

	private Date createTime;

	private Date updateTime;

	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id=id;
	}

	public String getCode(){
		return code;
	}

	public void setCode(String code){
		this.code=code;
	}

	public Long getTaskId(){
		return taskId;
	}

	public void setTaskId(Long taskId){
		this.taskId=taskId;
	}

	public Long getUserId(){
		return userId;
	}

	public void setUserId(Long userId){
		this.userId=userId;
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