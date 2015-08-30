package com.zj.platform.gamecenter.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zj.platform.gamecenter.constant.OnLineStatusEnum;
import com.zj.platform.gamecenter.constant.SocketConstant;
import com.zj.platform.gamecenter.constant.StatusEnum;
import com.zj.platform.gamecenter.dao.FriendsDao;
import com.zj.platform.gamecenter.dao.UserDao;
import com.zj.platform.gamecenter.dto.socket.FriendsDto;
import com.zj.platform.gamecenter.entity.Friends;
import com.zj.platform.gamecenter.entity.User;
import com.zj.platform.gamecenter.service.socket.SocketService;

/**
 * 好友Service类 <br>
 * TODO 需要添加异常处理<br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2014年12月25日 上午11:10:16
 * <p>
 * Company:
 * <p>
 * 
 * @author H.L.WAN
 * @version 1.0.0
 */
@Service
public class FriendsService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private FriendsDao friendsDao;

	@Autowired
	private SocketService socketService;

	/**
	 * 获取用户的所有好友列表
	 * 
	 * @param userId
	 * @param onlyOnline
	 *            是否只获取在线好友
	 * @return
	 */
	public List<FriendsDto> getAllFriendsByUserId(Long userId, boolean onlyOnline) {
		List<FriendsDto> dtos = new ArrayList<FriendsDto>();
		List<Friends> friends = friendsDao.getAllFriendsByUserId(userId, onlyOnline);
		for (Friends friend : friends) {
			FriendsDto friendDto = new FriendsDto(friend);
			dtos.add(friendDto);
		}
		return dtos;
	}

	/**
	 * 根据用户Id与好友Id查询Friends
	 * 
	 * @param userId
	 * @param friendId
	 * @return
	 */
	public Friends getByUserIdAndFriendId(Long userId, Long friendId) {
		return friendsDao.getByUserIdAndFriendId(userId, friendId);
	}

	/**
	 * 添加好友
	 * 
	 * @param userId
	 * @param friendId
	 * @return
	 */
	@Transactional
	public boolean addFriend(User user, User friend) {
		// User对象
		if (null == user) {
			return false;
		}
		Long userId = user.getId();
		Long friendId = friend.getId();
		Friends friends = getByUserIdAndFriendId(userId, friendId);
		if (friends != null) {
			return false;
		}
		friends = new Friends();
		// 设置好友信息
		Date cur = new Date();
		friends.setCreateTime(cur);
		friends.setUpdateTime(cur);
		friends.setIcon(friend.getIcon());
		friends.setFriendName(friend.getAccount());
		friends.setToUserId(friendId);
		friends.setAccount(friend.getAccount());
		// TODO 根据friendId当时登录状态设置 通知需要修改
		friends.setOnlineStatus(OnLineStatusEnum.ONLINE.getCode());
		friends.setStatus(StatusEnum.VALID.getCode());
		friends.setUserId(userId);
		friends.setUserType(friend.getType());
		friendsDao.insert(friends);
		// 通知请求者
		socketService.addSocketTask(userId, SocketConstant.FRIENDS_ADD_AGREE.getCode(), friends);
		Friends anFriends = new Friends();
		anFriends.setCreateTime(cur);
		anFriends.setUpdateTime(cur);
		anFriends.setIcon(user.getIcon());
		anFriends.setFriendName(user.getAccount());
		anFriends.setToUserId(userId);
		anFriends.setAccount(user.getAccount());
		// TODO 根据userId当时登录状态设置 通知需要修改
		anFriends.setOnlineStatus(OnLineStatusEnum.ONLINE.getCode());
		anFriends.setStatus(StatusEnum.VALID.getCode());
		anFriends.setUserId(friendId);
		anFriends.setUserType(user.getType());
		friendsDao.insert(anFriends);
		// 通知被请求者
		socketService.addSocketTask(friendId, SocketConstant.FRIENDS_ADD_AGREE.getCode(), friends);
		return true;
	}

	/**
	 * 解除好友
	 * 
	 * @param userId
	 * @param friendId
	 * @return
	 */
	@Transactional
	public boolean relieveFriend(Long id, Long userId) {
		// 检查该好友关系是否存在
		Friends friends = friendsDao.getById(id);
		if (null == friends) {
			return false;
		}
		if (!friends.getUserId().equals(id)) {
			return false;
		}
		friendsDao.relieveFriend(id);
		Friends anFriends = friendsDao.getByUserIdAndFriendId(friends.getToUserId(), friends.getUserId());
		// 双向解除
		if (null != anFriends) {
			friendsDao.relieveFriend(anFriends.getId());
		}
		socketService.addSocketTask(userId, SocketConstant.FRIENDS_RELEIVE.getCode(), friends);
		friends.setUserId(friends.getToUserId());
		friends.setToUserId(userId);
		return true;
	}

	public boolean renameFriendName(Long id, Long userId, String friendName) {
		Friends friends = friendsDao.getById(id);
		if (null == friends) {
			return false;
		}
		if (!friends.getUserId().equals(userId)) {
			return false;
		}
		friendsDao.modifyFriendName(id, friendName);
		return true;
	}

	/**
	 * 获取用户所有在线好友Id（包括隐身）
	 * 
	 * @param userId
	 * @return
	 */
	public List<Long> getOnlineFriendsId(Long userId) {
		return friendsDao.getAllOnLineFriendsIdByUserId(userId);
	}

	/**
	 * 更改登录状态, 登录之后需要调用，将状态改为在线或者隐身 连接断开时需要同样将状态更改为离线
	 * 
	 * @param friendId
	 * @param onlineStatus
	 */
	public void changeOnlineStatus(Long friendId, int onlineStatus) {
		friendsDao.changeOnlineStatus(friendId, onlineStatus);
	}

	/**
	 * 更新头像图标
	 * 
	 * @param friendId
	 * @param icon
	 */
	public void changeIcon(Long friendId, String icon) {
		friendsDao.changeIcons(friendId, icon);
	}

	/**
	 * 更新用户类型
	 * 
	 * @param friendId
	 * @param userType
	 */
	public void changeUserType(Long friendId, int userType) {
		friendsDao.changeUserType(friendId, userType);
	}

	/**
	 * 通知某一用户的所有在线好友 包括隐身
	 * 
	 * @param userId
	 */
	public void notifyFriends(Long userId, int commandId, Object content) {
		List<Long> friendsIds = getOnlineFriendsId(userId);
		for (Long id : friendsIds) {
			notifyUser(id, commandId, content);
		}
	}

	public void notifyUser(Long userId, int commandId, Object content) {
	}

}
