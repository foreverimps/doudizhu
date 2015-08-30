package com.zj.platform.gamecenter.dao;

import java.util.List;

import com.zj.platform.gamecenter.entity.Friends;

public interface FriendsDao {

	/**
	 * 根据主键查询好友关系
	 * 
	 * @param id
	 * @return
	 */
	public Friends getById(Long id);

	/**
	 * 根据用户Id与好友Id查询有效对象
	 * 
	 * @param userId
	 * @param friendsId
	 * @return
	 */
	public Friends getByUserIdAndFriendId(Long userId, Long friendsId);

	/**
	 * 查询表中用户自己的记录，即userId=toUserId
	 * 
	 * @param userId
	 * @return
	 */
	public Friends getSelfFriend(Long userId);

	/**
	 * 添加好友关系
	 * 
	 * @param friends
	 * @return 成功返回1 其它值为失败
	 */
	public int insert(Friends friends);

	/**
	 * 删除好友，逻辑删除 ，修改Flag标志
	 * 
	 * @param id
	 * @return 成功返回1 其它值为失败
	 */
	public int relieveFriend(Long id);

	/**
	 * 查询好友列表，如果isOnline为空则查询userId所有好友
	 * 
	 * @param userId
	 * @param isOnline
	 *            为true时只查询在线好友，不包括隐身
	 * @return
	 */
	public List<Friends> getAllFriendsByUserId(Long userId, boolean isOnline);

	/**
	 * 查询所有在线（包括隐身）状态的好友Id
	 * 
	 * @param userId
	 * @return
	 */
	public List<Long> getAllOnLineFriendsIdByUserId(Long userId);

	/**
	 * 修改好友备注姓名
	 * 
	 * @param id
	 * @param friendName
	 * @return 成功返回1 其它值为失败
	 */
	public int modifyFriendName(Long id, String friendName);

	/**
	 * 修改好友图标信息
	 * 
	 * @param friendId
	 * @param icon
	 */
	public void changeIcons(Long friendId, String icon);

	/**
	 * 修改好友的用户类型
	 * 
	 * @param friendId
	 * @param userType
	 */
	public void changeUserType(Long friendId, int userType);

	/**
	 * 修改好友的在线状态
	 * 
	 * @param friendId
	 * @param onlineStatus
	 */
	public void changeOnlineStatus(Long friendId, int onlineStatus);

}
