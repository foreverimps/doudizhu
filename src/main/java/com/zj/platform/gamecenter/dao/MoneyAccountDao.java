package com.zj.platform.gamecenter.dao;

import org.apache.ibatis.annotations.Param;

import com.zj.platform.gamecenter.entity.MoneyAccount;

public interface MoneyAccountDao {

	MoneyAccount queryByUserId(@Param("userId") Long userId);

	void insert(MoneyAccount moneyAccount);

	// public void update(MoneyAccount moneyAccount);

}
