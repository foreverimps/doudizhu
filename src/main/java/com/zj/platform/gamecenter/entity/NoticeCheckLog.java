package com.zj.platform.gamecenter.entity;

import java.util.Date;
import java.io.Serializable;

public class NoticeCheckLog implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

	private Long id;

	private Long userId;

	private Long noticeId;

	private Date createTime;

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

	public Long getNoticeId(){
		return noticeId;
	}

	public void setNoticeId(Long noticeId){
		this.noticeId=noticeId;
	}

	public Date getCreateTime(){
		return createTime;
	}

	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}

}