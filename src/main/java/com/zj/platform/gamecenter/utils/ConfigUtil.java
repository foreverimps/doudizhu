package com.zj.platform.gamecenter.utils;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigUtil {

	private static final Logger logger = LoggerFactory.getLogger(ConfigUtil.class);

	public static BigDecimal getWithdrawFeeRate() {
		String rate = PropertiesUtil.getInstance("config.properties").getText("withdraw.fee.rate");
		if (StringUtils.isNoneBlank(rate)) {
			return new BigDecimal(rate);
		}
		logger.info("获取默认提现费率失败");
		return null;
	}

	public static BigDecimal getWithdrawMin() {
		String min = PropertiesUtil.getInstance("config.properties").getText("withdraw.min");
		if (StringUtils.isNoneBlank(min)) {
			return new BigDecimal(min);
		}
		logger.info("获取默认提现最小金额失败");
		return null;
	}

	public static BigDecimal getWithdrawMax() {
		String max = PropertiesUtil.getInstance("config.properties").getText("withdraw.max");
		if (StringUtils.isNoneBlank(max)) {
			return new BigDecimal(max);
		}
		logger.info("获取默认提现最大金额失败");
		return null;
	}

	public static BigDecimal getWithdrawMinFee() {
		String min = PropertiesUtil.getInstance("config.properties").getText("withdraw.fee.min");
		if (StringUtils.isNoneBlank(min)) {
			return new BigDecimal(min);
		}
		logger.info("获取默认提现最小金额失败");
		return null;
	}

	public static BigDecimal getWithdrawMaxFee() {
		String max = PropertiesUtil.getInstance("config.properties").getText("withdraw.fee.max");
		if (StringUtils.isNoneBlank(max)) {
			return new BigDecimal(max);
		}
		logger.info("获取默认提现最大金额失败");
		return null;
	}

	public static String getTraceUrl() {
		String url = PropertiesUtil.getInstance("config.properties").getText("migu.trace.address");
		if (StringUtils.isNoneBlank(url)) {
			return url;
		}
		logger.info("获取交易系统的URL失败");
		return null;
	}
}
