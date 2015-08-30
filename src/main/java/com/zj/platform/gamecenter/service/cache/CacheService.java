package com.zj.platform.gamecenter.service.cache;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zj.platform.gamecenter.dto.LoginToken;
import com.zj.platform.gamecenter.dto.socket.FriendsDto;
import com.zj.platform.gamecenter.dto.socket.HelpDto;
import com.zj.platform.gamecenter.entity.Notice;
import com.zj.platform.gamecenter.entity.Product;
import com.zj.platform.gamecenter.entity.Ranking;
import com.zj.platform.gamecenter.entity.Task;
import com.zj.platform.gamecenter.entity.User;
import com.zj.platform.gamecenter.service.FriendsService;
import com.zj.platform.gamecenter.service.NoticeService;
import com.zj.platform.gamecenter.service.ProductService;
import com.zj.platform.gamecenter.service.RankingService;
import com.zj.platform.gamecenter.service.TaskService;
import com.zj.platform.gamecenter.utils.MD5Utils;

@Service
public class CacheService {

	@Autowired
	private SpyMemcachedClient client;

	@Autowired
	private ProductService productService;

	@Autowired
	private NoticeService noticeService;

	@Autowired
	private RankingService rankingService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private FriendsService friendsService;

	public String genToken(User user) {
		LoginToken loginToken = new LoginToken();
		loginToken.setUser(user);
		String token = MD5Utils.toMD5(RandomStringUtils.random(10) + user.getId(), "utf-8");
		String tokenValueKey = MemcacheKeyMgr.getTokenValueKey(user.getId());
		String oldToken = client.get(tokenValueKey);// 如果有老的token，那么就需要把token删除，以免一个人有两个token
		if (null != oldToken) {
			loginToken = client.get(oldToken);
			client.delete(oldToken);
		}
		client.set(tokenValueKey, MemcachedObjectType.TOKEN_VALUE.getExpiredTime(), token);
		client.set(token, MemcachedObjectType.TOKEN.getExpiredTime(), loginToken);
		return token;
	}

	public LoginToken getLoginToken(String token) {
		LoginToken loginToken = client.get(token);
		if (null == loginToken) {
			System.out.println("错误，登录失效");
		}
		return loginToken;
	}

	public List<Product> getProducts() {
		String key = MemcachedObjectType.PRODUCTS.getPrefix();
		List<Product> products = client.get(key);
		if (!CollectionUtils.isEmpty(products)) {
			products = productService.queryByType(1);
			client.set(key, MemcachedObjectType.PRODUCTS.getExpiredTime(), products);
		}
		return products;
	}

	public List<Product> getChests() {
		String key = MemcachedObjectType.PRODUCTS.getPrefix();
		List<Product> products = client.get(key);
		if (!CollectionUtils.isEmpty(products)) {
			products = productService.queryByType(2);
			client.set(key, MemcachedObjectType.PRODUCTS.getExpiredTime(), products);
		}
		return products;
	}

	public List<Ranking> getRankings(int type) {
		String key = MemcachedObjectType.TASKS.getPrefix() + type;
		List<Ranking> rankings = client.get(key);
		if (CollectionUtils.isEmpty(rankings)) {
			rankings = rankingService.queryByType(type);
			client.set(key, MemcachedObjectType.TASKS.getExpiredTime(), rankings);
		}
		return rankings;
	}

	public List<Task> getTasks(int type) {
		String key = MemcachedObjectType.TASKS.getPrefix() + type;
		List<Task> tasks = client.get(key);
		if (CollectionUtils.isEmpty(tasks)) {
			tasks = taskService.queryByType(type);
			client.set(key, MemcachedObjectType.TASKS.getExpiredTime(), tasks);
		}
		return tasks;
	}

	public List<Notice> getNotices() {
		String key = MemcachedObjectType.NOTICES.getPrefix();
		List<Notice> notices = client.get(key);
		if (CollectionUtils.isEmpty(notices)) {
			notices = noticeService.queryAllNotices();
			client.set(key, MemcachedObjectType.NOTICES.getExpiredTime(), notices);
		}
		return notices;
	}

	public List<HelpDto> getHelps() {
		// TODO 看是否有更新，如果没有更新那么返回0，如果是1，那么久获取。
		return null;
	}

	public List<FriendsDto> getFriendsByUserId(Long userId) {
		return friendsService.getAllFriendsByUserId(userId, false);
	}
}
