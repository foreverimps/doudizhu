package com.zj.platform.gamecenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zj.platform.gamecenter.entity.User;

public interface UserDao {

	User login(@Param("account") String account, @Param("password") String password);

	void register(User user);

	void updateInfo(User user);

	List<User> queryAiUser(@Param("source") int source);

	User getById(Long id);

	List<User> queryByMoneyAccount(@Param("start") Long start, @Param("end") Long end);

}
