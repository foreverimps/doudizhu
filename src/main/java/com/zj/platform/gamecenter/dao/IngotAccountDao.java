package com.zj.platform.gamecenter.dao;

import org.apache.ibatis.annotations.Param;

import com.zj.platform.gamecenter.entity.IngotAccount;

public interface IngotAccountDao {

	void insert(IngotAccount ingotAccount);

	IngotAccount queryByUserId(@Param("userId") Long userId);

}
