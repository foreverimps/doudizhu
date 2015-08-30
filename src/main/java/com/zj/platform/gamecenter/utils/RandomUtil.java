package com.zj.platform.gamecenter.utils;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomUtil {

	public boolean getChance(int rate) {
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

	public boolean getChanceByUserId(Long userId) {
		return false;
	}

}
