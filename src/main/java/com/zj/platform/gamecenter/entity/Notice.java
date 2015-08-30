package com.zj.platform.gamecenter.entity;

import java.util.Date;
import java.io.Serializable;

public class Notice implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

	private Long id;

	private String title;

	private String descs;

	private int status;

	private Date createTime;

	private Date updateTime;

	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id=id;
	}

	public String getTitle(){
		return title;
	}

	public void setTitle(String title){
		this.title=title;
	}

	public String getDescs(){
		return descs;
	}

	public void setDescs(String descs){
		this.descs=descs;
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