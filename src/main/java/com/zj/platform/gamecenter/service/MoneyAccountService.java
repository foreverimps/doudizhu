package com.zj.platform.gamecenter.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zj.platform.gamecenter.constant.UserMoneyOptTypeEnum;
import com.zj.platform.gamecenter.dao.MoneyAccountDao;
import com.zj.platform.gamecenter.dao.MoneyAccountLogDao;
import com.zj.platform.gamecenter.entity.MoneyAccount;
import com.zj.platform.gamecenter.entity.MoneyAccountLog;

@Service
public class MoneyAccountService {

	@Autowired
	private MoneyAccountDao moneyAccountDao;

	@Autowired
	private MoneyAccountLogDao moneyAccountLogDao;

	public void insert(MoneyAccount moneyAccount) {
		moneyAccountDao.insert(moneyAccount);
		MoneyAccountLog moneyAccountLog = new MoneyAccountLog();
		BeanUtils.copyProperties(moneyAccount, moneyAccountLog);
		moneyAccountLog.setOptType(UserMoneyOptTypeEnum.REG.getCode());
		moneyAccountLog.setMoneyAccountId(moneyAccount.getId());
		moneyAccountLogDao.insert(moneyAccountLog);
	}

	public MoneyAccount queryByUserId(Long userId) {
		return moneyAccountDao.queryByUserId(userId);
	}

}
