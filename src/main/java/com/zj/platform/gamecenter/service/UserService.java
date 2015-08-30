package com.zj.platform.gamecenter.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zj.platform.gamecenter.constant.StatusEnum;
import com.zj.platform.gamecenter.constant.UserSourceEnum;
import com.zj.platform.gamecenter.constant.UserTypeEnum;
import com.zj.platform.gamecenter.dao.UserDao;
import com.zj.platform.gamecenter.dto.socket.AccountDto;
import com.zj.platform.gamecenter.entity.IngotAccount;
import com.zj.platform.gamecenter.entity.MoneyAccount;
import com.zj.platform.gamecenter.entity.User;

@Service
public class UserService {

	@Autowired
	private MoneyAccountService moneyAccountService;

	@Autowired
	private IngotAccountService ingotAccountService;

	@Autowired
	private UserDao userDao;

	public User login(String account, String password) {
		User user = userDao.login(account, password);
		if (null != user) {
			MoneyAccount moneyAccount = moneyAccountService.queryByUserId(user.getId());
			IngotAccount ingotAccount = ingotAccountService.queryByUserId(user.getId());
			user.setMoneyAccount(moneyAccount);
			user.setIngotAccount(ingotAccount);
		}
		return user;
	}

	@Transactional
	public User register(AccountDto accountDto) {
		User user = new User();
		user.setAccount(accountDto.getAccount());
		user.setPassword(accountDto.getPassword());
		user.setSource(0);
		user.setStatus(StatusEnum.VALID.getCode());
		user.setType(UserTypeEnum.NORMAL.getCode());
		user.setCreateTime(new Date());
		user.setUpdateTime(new Date());
		userDao.register(user);

		MoneyAccount moneyAccount = new MoneyAccount();
		moneyAccount.setUserId(user.getId());
		moneyAccount.setAmount(0L);
		moneyAccount.setUpdateTime(new Date());

		IngotAccount ingotAccount = new IngotAccount();
		ingotAccount.setUserId(user.getId());
		ingotAccount.setAmount(0L);
		ingotAccount.setUpdateTime(new Date());

		moneyAccountService.insert(moneyAccount);
		ingotAccountService.insert(ingotAccount);

		user.setMoneyAccount(moneyAccount);
		user.setIngotAccount(ingotAccount);
		return user;
	}

	public List<User> queryAiUser() {
		return userDao.queryAiUser(UserSourceEnum.S_ROBOT.getCode());
	}

	public List<User> queryByMoneyAccount(Long start, Long end) {
		return userDao.queryByMoneyAccount(start, end);
	}

	public User getById(Long id) {
		return userDao.getById(id);
	}
}
