package com.zj.platform.gamecenter.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import com.zj.platform.gamecenter.dto.socket.UserChanceDto;

@Service
public class RandomService {

	public static boolean getChance(int rate) {
		if (rate < 0 || rate > 100) {
			return false;
		} else {
			int num = Integer.parseInt(RandomStringUtils.randomNumeric(2));
			if (num <= rate) {
				return true;
			}
			return false;
		}
	}

	/**
	 * 根据用户id去计算获取大牌的概率 计算角度：注册时间，vip等级，用户金钱，充值记录，好友数量
	 * 注册时间越长几率越小，vip等级越高几率越小，用户金钱越小几率越大，充值记录越多几率越大，好友数量越多几率越大。几率相乘
	 */
	public boolean getUserChance(UserChanceDto dto) {
		int f = 3;
		if (dto.getFriendNum() < 11) {
			f = 3;
		} else if (dto.getFriendNum() < 100 && dto.getFriendNum() > 10) {
			f = 5;
		} else {
			f = 8;
		}
		int m = 3;
		if (dto.getMoneyAccount() < 10000) {
			m = 3;
		} else if (dto.getMoneyAccount() < 100000 && dto.getMoneyAccount() > 10000) {
			m = 5;
		} else {
			m = 8;
		}
		int r = 3;
		if (dto.getRechargeAmount() < 301) {
			r = 3;
		} else if (dto.getRechargeAmount() < 2000 && dto.getRechargeAmount() > 300) {
			r = 5;
		} else {
			r = 8;
		}
		int rd = 3;
		if (dto.getRegDays() < 61) {
			r = 8;
		} else if (dto.getRegDays() < 1000 && dto.getRegDays() > 60) {
			r = 5;
		} else {
			r = 3;
		}
		int v = 3;
		if (dto.getVipLevel() < 3) {
			r = 3;
		} else if (dto.getRegDays() < 6 && dto.getRegDays() > 3) {
			r = 5;
		} else {
			r = 8;
		}
		double max = 40;
		double num = f * m * r * rd * v;
		double gaiLv = num / max * 0.8 * 100;
		int lv = (int) Math.round(gaiLv);
		return getChance(lv);
	}

	/**
	 * main函数.
	 * 
	 * @param args
	 *            启动参数
	 * @throws Exception
	 *             Exception
	 */
	public static void main(String... args) throws Exception {
		int count = 0;
		for (int i = 0; i < 100; i++) {
			if (getChance(1)) {
				count++;
			}
		}
		System.out.println(count);

		double max = 5 * 8;
		double num = 3 + 3 + 3 + 3 + 3;
		double gaiLv = num / max * 0.8 * 100;
		System.out.println(Math.round(gaiLv));
	}
}
