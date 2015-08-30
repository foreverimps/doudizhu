package com.zj.platform.gamecenter.service.cache;

/**
 * 缓存key的管理
 * <p>
 * Copyright: Copyright (c) 2014-3-14 上午11:09:33
 * <p>
 * Company: 苏州米谷网络科技有限公司
 * <p>
 * 
 * @author name
 * @version 1.0.0
 */
public class MemcacheKeyMgr {

	/**
	 * TOKEN值的key
	 */
	public static String getTokenValueKey(Long userId) {
		return MemcachedObjectType.TOKEN_VALUE.getPrefix() + "userId:" + userId;
	}

	/**
	 * LoginToken的key
	 */
	public static String getTokenKey(String token) {
		return MemcachedObjectType.TOKEN.getPrefix() + "token:" + token;
	}

	/**
	 * 短信验证码key
	 */
	public static String getSmsCodeKey(Long userId, String phone, String sessionName) {
		return MemcachedObjectType.SMS_CODE.getPrefix() + "userId:" + userId + "phone:" + phone + "sessionName:" + sessionName;
	}

	/**
	 * 验证码key
	 * 
	 * @param email
	 */
	public static String getCheckCodeKey(Long userId, String operate) {
		return MemcachedObjectType.CHECK_CODE.getPrefix() + "userId:" + userId + "operate:" + operate;
	}

	/**
	 * 获取user的key
	 */
	public static String getUserKey(Long id) {
		return MemcachedObjectType.USER.getPrefix() + "userId:" + id;
	}

	/**
	 * 商品key
	 */
	public static String getProductsKey() {
		return MemcachedObjectType.PRODUCTS.getPrefix();
	}

	/**
	 * 宝箱key
	 * 
	 * @param token
	 * @return
	 */
	public static String getChestsKey(String token) {
		return MemcachedObjectType.CHESTS.getPrefix();
	}

}
