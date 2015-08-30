package com.zj.platform.gamecenter.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 好友实体类 <br>
 * 用户在注册成功后需要向该表中添加一条自己指向自己的好友记录
 * <p>
 * Copyright: Copyright (c) 2014年12月23日 下午1:06:58
 * <p>
 * Company:
 * <p>
 * 
 * @author H.L.Wan
 * @version 1.0.0
 */
public class Friends implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	private Long id;

	private Long userId;

	private Long toUserId;

	/** 好友备注 */
	private String friendName;

	private int status;

	private Date updateTime;

	private Date createTime;

	// -------------------冗余字段-----------------------//

	private Integer onlineStatus;

	private String account;

	private int userType;

	private String icon;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(Integer onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

	public String getFriendName() {
		return friendName;
	}

	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getToUserId() {
		return toUserId;
	}

	public void setToUserId(Long toUserId) {
		this.toUserId = toUserId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}