package com.zj.platform.gamecenter.mongoDao;

import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.zj.platform.gamecenter.entity.Bureau;
import com.zj.platform.gamecenter.entity.Card;

@Repository
public class BureauDao extends BaseMongoDao {

	/**
	 * 获取房间中的局对象
	 * 
	 * @param roomCode
	 * @return
	 */
	public Bureau getLastBureauDtoInRoom(String roomCode) {
		return mongoTemplate.findOne(new Query(Criteria.where("roomCode").is(roomCode)), Bureau.class);
	}

	/**
	 * 更新局的倒计时
	 * 
	 * @param id
	 * @param count
	 */
	public void updateBureauCountDown(String id, int count) {
		mongoTemplate.updateFirst(new Query(Criteria.where("id").is(id)), new Update().addToSet("countDown", count), Bureau.class);
	}

	/**
	 * 更新局的当前玩家下标
	 * 
	 * @param id
	 * @param curIndex
	 */
	public void updateBureauCurIndex(String id, int curIndex) {
		mongoTemplate.updateFirst(new Query(Criteria.where("id").is(id)), new Update().addToSet("curIndex", curIndex), Bureau.class);
	}

	/**
	 * 更新局的当前轮
	 * 
	 * @param id
	 * @param round
	 */
	public void updateBureauRound(String id, int round) {
		mongoTemplate.updateFirst(new Query(Criteria.where("id").is(id)), new Update().addToSet("round", round), Bureau.class);
	}

	/**
	 * 保存一局游戏 只适用于创建时调用，否则可能导致属性值覆盖 保存之前将之前的局从MongoDB中删除
	 * 
	 * @param bureau
	 */
	public void saveBureauDto(Bureau bureau) {
		mongoTemplate.remove(new Query(Criteria.where("roomCode").is(bureau.getRoomCode())), Bureau.class);
		mongoTemplate.save(bureau);
	}

	/**
	 * 更新局中的总注
	 * 
	 * @param id
	 * @param totalNote
	 */
	public void updateBureauTotalNote(String id, Long totalNote) {
		mongoTemplate.updateFirst(new Query(Criteria.where("id").is(id)), new Update().addToSet("totalNote", totalNote), Bureau.class);
	}

	/**
	 * 更新局的单注
	 * 
	 * @param id
	 * @param singleNote
	 */
	public void updateBureauSingleNote(String id, int singleNote) {
		mongoTemplate.updateFirst(new Query(Criteria.where("id").is(id)), new Update().addToSet("singleNote", singleNote), Bureau.class);
	}

	/**
	 * 减少局中的有效人数,即将MongoDB中count+1的值更新为count,如果Count=1，则将局更新为结束
	 * 
	 * @param id
	 * @param count
	 * @param index
	 *            用于检验结果， 是否为本次更新结果
	 * @Return true 更新成功 false 失败
	 */
	public boolean updateBureauActiveCount(String id, int count, int index) {
		Update update = new Update().addToSet("activeCount", count).addToSet("lastestOutIndex", index);
		Query query = new Query(Criteria.where("id").is(id).and("activeCount").is(count + 1).and("isEnd").is(false));
		if (count == 1) {
			update.addToSet("isEnd", true);
		}
		mongoTemplate.updateFirst(query, update, Bureau.class);
		// 较验
		Bureau bureau = mongoTemplate.findOne(new Query(Criteria.where("id").is(id)), Bureau.class);
		if (bureau.getActiveCount() == count && bureau.getLastestOutIndex() == index) {
			return true;
		}
		return false;
	}

	/**
	 * 更改房间中的一轮开始的下标
	 * 
	 * @param bureauDtoId
	 * @param index
	 */
	public void changeBureauDtoRoundBeginIndex(String bureauDtoId, int index) {
		mongoTemplate.updateFirst(new Query(Criteria.where("id").is(bureauDtoId)), new Update().addToSet("roundBeginIndex", index), Bureau.class);
	}

	/**
	 * 设置局中的剩余的牌点
	 * 
	 * @param id
	 * @param onePairCard
	 */
	public void updateBureauCards(String id, List<Card> onePairCard) {
		mongoTemplate.updateFirst(new Query(Criteria.where("id").is(id)), new Update().addToSet("onePairCard", onePairCard), Bureau.class);
	}

	/**
	 * 根据roomCode查询局
	 * 
	 * @param bureau
	 */
	public Bureau queryBureau(String roomCode) {
		return getLastBureauDtoInRoom(roomCode);
	}

}
