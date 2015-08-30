package com.zj.platform.gamecenter.mongoDao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.zj.platform.gamecenter.entity.Player;
import com.zj.platform.gamecenter.entity.Room;
import com.zj.platform.gamecenter.entity.Seat;

@Repository
public class SeatDao extends BaseMongoDao {

	@Autowired
	private RoomDao roomDao;

	/**
	 * 保存座位 只适用于创建时调用，否则可能导致属性值覆盖，保存前删除之前的座位
	 * 
	 * @param seat
	 */
	public void saveSeat(Seat seat) {
		mongoTemplate.remove(new Query(Criteria.where("roomCode").is(seat.getRoomCode()).and("index").is(seat.getIndex())), Seat.class);
		mongoTemplate.save(seat);
	}

	/**
	 * 保存座位组 只适用于创建时调用，否则可能导致属性值覆盖
	 * 
	 * @param seats
	 */
	public void saveSeats(Seat[] seats) {
		for (Seat seat : seats) {
			saveSeat(seat);
		}
	}

	/**
	 * 玩家坐下
	 * 
	 * @param roomCode
	 * @param index
	 * @param player
	 * @return
	 */
	public boolean updateSeatSit(String roomCode, int index, Player player) {
		if (player == null) {
			return false;
		}
		Long userId = player.getUserId();
		mongoTemplate.updateFirst(new Query(Criteria.where("roomCode").is(roomCode).and("index").is(index).and("userId").is(null)),
		                          new Update().addToSet("userId", userId), Seat.class);
		Seat seat = mongoTemplate.findOne(new Query(Criteria.where("roomCode").is(roomCode).and("index").is(index)), Seat.class);
		if (null != seat.getUserId() && seat.getUserId().equals(userId)) {
			Room room = roomDao.getRoomDtoByCode(roomCode);
			mongoTemplate.updateFirst(new Query(Criteria.where("roomCode").is(roomCode)), new Update().addToSet("sitCount", room.getSitCount() + 1),
			                          Room.class);
			// TODO 可能会出现sitCount不准确，是否可以直接在sql进行加减
			return true;
		}
		return false;
	}

	/**
	 * 根据房间号与下标获取Seat对象
	 * 
	 * @param roomCode
	 * @param index
	 * @return
	 */
	public Seat getSeat(String roomCode, int index) {
		return mongoTemplate.findOne(new Query(Criteria.where("roomCode").is(roomCode).and("index").is(index)), Seat.class);
	}

	/**
	 * 获取房间中的所有座位
	 * 
	 * @param roomCode
	 * @return
	 */
	public Seat[] getSeatsInRoom(String roomCode) {
		List<Seat> seats = mongoTemplate.find(new Query(Criteria.where("roomCode").is(roomCode)), Seat.class);
		if (seats.isEmpty()) {
			return new Seat[0];
		}
		Seat[] seat = new Seat[seats.size()];
		for (int i = 0; i < seats.size(); i++) {
			Seat s = seats.get(i);
			seat[s.getIndex()] = s;
		}
		return seat;
	}

	/**
	 * 更新玩家的下家的下标
	 * 
	 * @param roomCode
	 * @param index
	 * @param nextIndex
	 */
	public void updateSeatNextIndex(String roomCode, int index, int nextIndex) {
		mongoTemplate.updateFirst(new Query(Criteria.where("roomCode").is(roomCode).and("index").is(index)),
		                          new Update().addToSet("nextIndex", nextIndex), Seat.class);
	}

	/**
	 * 更新玩家的上家下标
	 * 
	 * @param roomCode
	 * @param index
	 * @param previousIndex
	 */
	public void updateSeatPreviousIndex(String roomCode, int index, int previousIndex) {
		mongoTemplate.updateFirst(new Query(Criteria.where("roomCode").is(roomCode).and("index").is(index)),
		                          new Update().addToSet("previousIndex", previousIndex), Seat.class);
	}

	/**
	 * 玩家出局
	 * 
	 * @param roomCode
	 * @param index
	 */
	public void updateSeatOut(String roomCode, int index) {
		mongoTemplate.updateFirst(new Query(Criteria.where("roomCode").is(roomCode).and("index").is(index)), new Update().addToSet("isOut", true),
		                          Seat.class);
	}

	/**
	 * 更新玩家看牌状态
	 * 
	 * @param roomCode
	 * @param index
	 */
	public void updateSeatCheck(String roomCode, int index) {
		mongoTemplate.updateFirst(new Query(Criteria.where("roomCode").is(roomCode).and("index").is(index)), new Update().addToSet("isCheck", true),
		                          Seat.class);
	}

	/**
	 * 玩家离座
	 * 
	 * @param roomCode
	 * @param index
	 */
	public void updateSeatKick(String roomCode, int index) {
		mongoTemplate.updateFirst(new Query(Criteria.where("roomCode").is(roomCode).and("index").is(index)), new Update().addToSet("userId", null),
		                          Seat.class);
		Room room = roomDao.getRoomDtoByCode(roomCode);
		mongoTemplate.updateFirst(new Query(Criteria.where("roomCode").is(roomCode)), new Update().addToSet("sitCount", room.getSitCount() - 1),
		                          Room.class);
	}

	/**
	 * 玩家弃牌
	 * 
	 * @param roomCode
	 * @param index
	 */
	public void updateSeatGiveup(String roomCode, int index) {
		mongoTemplate.updateFirst(new Query(Criteria.where("roomCode").is(roomCode).and("index").is(index)), new Update().addToSet("giveUp", true),
		                          Seat.class);
	}

	/**
	 * 玩家比牌输
	 * 
	 * @param roomCode
	 * @param index
	 */
	public void updateSeatLose(String roomCode, int index) {
		mongoTemplate.updateFirst(new Query(Criteria.where("roomCode").is(roomCode).and("index").is(index)), new Update().addToSet("isLose", true),
		                          Seat.class);
	}

	public List<Seat> getPlayingSeatInRoom(String roomCode) {
		return mongoTemplate.find(new Query(Criteria.where("roomCode").is(roomCode).and("isOut").is(false)), Seat.class);
	}
}
