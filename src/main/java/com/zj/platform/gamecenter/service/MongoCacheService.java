package com.zj.platform.gamecenter.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.zj.platform.gamecenter.constant.UserSourceEnum;
import com.zj.platform.gamecenter.entity.Player;
import com.zj.platform.gamecenter.entity.Room;
import com.zj.platform.gamecenter.entity.User;

/**
 * 从mongoDB 中加载对象 <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2015年1月8日 下午11:37:27
 * <p>
 * Company:
 * <p>
 * 
 * @author
 * @version 1.0.0
 */
@Service
public class MongoCacheService {

	@Autowired
	protected MongoTemplate mongoTemplate;

	public List<Room> queryAllRoom() {
		return null;
	}

	public List<User> queryAllAiUser() {
		return null;
	}

	public List<User> queryAiUserOfRoom(String roomCode) {
		List<Player> players = mongoTemplate.find(new Query(Criteria.where("roomCode").is(roomCode)), Player.class);
		List<User> result = new ArrayList<User>();
		for (Player player : players) {
			User user = mongoTemplate.findOne(new Query(Criteria.where("id").is(player.getUserId())), User.class);
			if (UserSourceEnum.S_ROBOT.getCode() == user.getSource()) {// 只有是机器人才放入到集合里面
				result.add(user);
			}
		}
		return result;
	}

}
