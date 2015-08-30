package com.zj.platform.gamecenter.utils;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidateUtil {

	private static final Logger logger = LoggerFactory.getLogger(ValidateUtil.class);

	private static Set<String> bankTypes = new HashSet<String>();

	private static final String LOW_REX = "\\d{6,}";// 一级 只包含数字

	private static final String MID_REX = "[a-zA-Z]+";// 二级 只包含字母

	private static final String HIG_REX = "[\\da-zA-Z]*\\d+[a-zA-Z]+[\\da-zA-Z]*";// 三级包含数据和字母

	static {
		bankTypes.add("alipay"); // 支付宝支付
		bankTypes.add("DEBITCARD_BOC"); // 中国银行储蓄卡
		bankTypes.add("DEBITCARD_ICBC"); // 中国工商银行储蓄卡
		bankTypes.add("DEBITCARD_CMB"); // 中国招商银行储蓄卡
		bankTypes.add("DEBITCARD_ABC"); // 中国农业银行储蓄卡
		bankTypes.add("DEBITCARD_COMM"); // 交通银行储蓄卡
		bankTypes.add("DEBITCARD_CCB"); // 中国建设银行储蓄卡
		bankTypes.add("DEBITCARD_PSBC"); // 中国邮政储蓄银行储蓄卡
		bankTypes.add("DEBITCARD"); // 更多储蓄卡
	}

	public static String validPassStength(String password) {
		if (password.matches(LOW_REX)) {
			return "last";
		}
		if (password.matches(MID_REX)) {
			return "mid";
		}
		return "high";
	}

	/**
	 * 检查是否为有效的银行卡号
	 * 
	 * @param bankCardNo
	 * @return
	 */
	public static boolean validateBankCardNo(String bankCardNo) {
		return isNumeric(bankCardNo);
	}

	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	/**
	 * 检查充值的金额。
	 * 
	 * @return 错误信息。
	 */
	public static boolean checkRechargeAmount(BigDecimal amount) {
		String errorMsg = "";

		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			errorMsg = "金额 <= 0";
			logger.warn(errorMsg + "amount:" + amount);
			return false;
		}

		if (amount.intValue() >= 10000000) {
			errorMsg = "充值金额>= 10000000";
			logger.warn(errorMsg + "amount:" + amount);
			return false;
		}

		if (amount.scale() > 2) {
			errorMsg = "充值金额最多有2位小数。";
			logger.warn(errorMsg + "amount:" + amount);
			return false;
		}

		return true;
	}

	public static boolean checkRechargeBankType(String bankType, boolean isPhone) {
		if (!isPhone) {
			return true;
		}
		String errorMsg = "";
		// check bankType
		if (StringUtils.isBlank(bankType)) {
			errorMsg = "无效的银行类型。";
			logger.warn(errorMsg + "bankType is blank!");
			return false;
		}
		if (!bankTypes.contains(bankType)) {
			errorMsg = "无效的银行类型。";
			logger.warn(errorMsg + "bankType:" + bankType);
			return false;
		}
		return true;
	}

	public static boolean isPhone(String str) {
		Pattern p = Pattern.compile("^0?[1][358][0-9]{9}$");
		Matcher m = p.matcher(str);
		return m.matches();
	}

}
