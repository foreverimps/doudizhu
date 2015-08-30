package com.zj.platform.gamecenter.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zj.platform.gamecenter.constant.UserMoneyOptTypeEnum;
import com.zj.platform.gamecenter.dao.IngotAccountDao;
import com.zj.platform.gamecenter.dao.IngotAccountLogDao;
import com.zj.platform.gamecenter.entity.IngotAccount;
import com.zj.platform.gamecenter.entity.IngotAccountLog;

@Service
public class IngotAccountService {

	@Autowired
	private IngotAccountDao ingotAccountDao;

	@Autowired
	private IngotAccountLogDao ingotAccountLogDao;

	public void insert(IngotAccount ingotAccount) {
		ingotAccountDao.insert(ingotAccount);
		IngotAccountLog log = new IngotAccountLog();
		BeanUtils.copyProperties(ingotAccount, log);
		log.setOptType(UserMoneyOptTypeEnum.REG.getCode());
		log.setIngotAccountId(ingotAccount.getId());
		ingotAccountLogDao.insert(log);
	}

	public IngotAccount queryByUserId(Long userId) {
		return ingotAccountDao.queryByUserId(userId);
	}

}
