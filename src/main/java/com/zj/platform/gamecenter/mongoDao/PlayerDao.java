package com.zj.platform.gamecenter.mongoDao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.zj.platform.gamecenter.constant.PlayerStatusEnum;
import com.zj.platform.gamecenter.entity.Card;
import com.zj.platform.gamecenter.entity.Player;

@Repository
public class PlayerDao extends BaseMongoDao {

	/**
	 * 保存房间，只适用于创建时调用，否则可能导致属性值覆盖
	 * 
	 * @param room
	 */
	public void savePlayer(Player player) {
		removePlayer(player.getUserId());
		mongoTemplate.save(player);
	}

	/**
	 * 用于在session断开或者退出登录时将玩家对象Player
	 * 
	 * @param userId
	 */
	public void removePlayer(long userId) {
		mongoTemplate.remove(new Query(Criteria.where("userId").is(userId)), Player.class);
	}

	/**
	 * 获取房间中的所有用户Id
	 * 
	 * @param roomCode
	 * @return
	 */
	public List<Player> getPlayingByRoomCode(String roomCode) {
		return mongoTemplate.find(new Query(Criteria.where("roomCode").is(roomCode).and("status").is(PlayerStatusEnum.GAMEING.getCode())),
		                          Player.class);
	}

	/**
	 * 获取房间中的所有用户Id
	 * 
	 * @param roomCode
	 * @return
	 */
	public List<Long> getUserIdsByRoomCode(String roomCode) {
		List<Player> players = mongoTemplate.find(new Query(Criteria.where("roomCode").is(roomCode)), Player.class);
		if (players.isEmpty()) {
			return new ArrayList<Long>();
		}
		List<Long> result = new ArrayList<Long>();
		for (Player player : players) {
			result.add(player.getUserId());
		}
		return result;
	}

	/**
	 * 更新玩家的状态
	 * 
	 * @param userId
	 * @param status
	 */
	public void updatePlayDtoStatus(Long userId, int status) {
		mongoTemplate.updateFirst(new Query(Criteria.where("userId").is(userId)), new Update().addToSet("status", status), Player.class);
	}

	/**
	 * 修改玩家上注总和
	 * 
	 * @param userId
	 * @param sumNote
	 */
	public void updatePlayerSumNote(Long userId, Long sumNote) {
		mongoTemplate.updateFirst(new Query(Criteria.where("userId").is(userId)), new Update().addToSet("sumNote", sumNote), Player.class);
	}

	/**
	 * 更改用户RoomCode,表示用户房间
	 * 
	 * @param userId
	 * @param roomCode
	 */
	public void updatePlayerRoomCode(Long userId, String roomCode) {
		mongoTemplate.updateFirst(new Query(Criteria.where("userId").is(userId)), new Update().addToSet("roomCode", roomCode), Player.class);
	}

	/**
	 * 根据userid获取玩家对象
	 * 
	 * @param userId
	 * @return
	 */
	public Player getPlayerByUserId(Long userId) {
		if (null == userId) {
			return null;
		}
		return mongoTemplate.findOne(new Query(Criteria.where("userId").is(userId)), Player.class);
	}

	/**
	 * 修改玩家在某一房间中连续玩的局数
	 * 
	 * @param userId
	 * @param index
	 */
	public void updatePlayerBureauIndex(Long userId, int index) {
		mongoTemplate.updateFirst(new Query(Criteria.where("userId").is(userId)), new Update().addToSet("bureauIndex", index), Player.class);
	}

	/**
	 * 更新玩家的金币数量 TODO 元宝与金币不放到玩家对象中，需要用时从User里面取
	 * 
	 * @param player
	 */
	public void updatePlayerMoneyAmount(Player player) {
		if (null == player) {
			return;
		}
		mongoTemplate.updateFirst(new Query(Criteria.where("userId").is(player.getUserId())),
		                          new Update().addToSet("moneyAccountAmount", player.getMoneyAccountAmount()).addToSet("sumNote", 0L), Player.class);
	}

	/**
	 * 更新玩家元宝数量 TODO 元宝与金币不放到玩家对象中，需要用时从User里面取
	 * 
	 * @param player
	 */
	public void updatePlayerIngotAccountAmount(Player player) {
		if (null == player) {
			return;
		}
		mongoTemplate.updateFirst(new Query(Criteria.where("userId").is(player.getUserId())),
		                          new Update().addToSet("ingotAccountAmount", player.getIngotAccountAmount()), Player.class);
	}

	public void updatePlayerCard(Long userId, List<Card> card) {
		mongoTemplate.updateFirst(new Query(Criteria.where("userId").is(userId)), new Update().addToSet("cards", card), Player.class);
	}
}
