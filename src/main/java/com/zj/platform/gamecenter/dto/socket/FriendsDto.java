package com.zj.platform.gamecenter.dto.socket;

import com.zj.platform.gamecenter.constant.OnLineStatusEnum;
import com.zj.platform.gamecenter.constant.UserTypeEnum;
import com.zj.platform.gamecenter.entity.Friends;

public class FriendsDto {

	private Long id;

	private Long userId;// 用户ID

	private String account;// 账号

	private String friendName;// 备注,替代account

	private String icon;// 头像

	private int userType;// 用户类型

	private String userTypeName;

	private Boolean isOnline;// 是否在线

	public FriendsDto(Friends friend) {
		id = friend.getId();
		userId = friend.getToUserId();
		account = friend.getAccount();
		friendName = friend.getFriendName();
		icon = friend.getIcon();
		userType = friend.getUserType();
		userTypeName = UserTypeEnum.getDescription(userType);
		if (OnLineStatusEnum.ONLINE.getCode() == friend.getOnlineStatus()) {
			isOnline = true;
		} else {
			isOnline = false;
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserTypeName() {
		return userTypeName;
	}

	public void setUserTypeName(String userTypeName) {
		this.userTypeName = userTypeName;
	}

	public String getFriendName() {
		return friendName;
	}

	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public Boolean getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(Boolean isOnline) {
		this.isOnline = isOnline;
	}

}
