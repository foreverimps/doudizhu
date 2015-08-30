package com.zj.platform.gamecenter.mongoDao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.zj.platform.gamecenter.dto.socket.RoomDto;
import com.zj.platform.gamecenter.entity.Bureau;
import com.zj.platform.gamecenter.entity.Player;
import com.zj.platform.gamecenter.entity.Room;
import com.zj.platform.gamecenter.entity.Seat;

@Repository
public class RoomDao extends BaseMongoDao {

	@Autowired
	private SeatDao seatDao;

	@Autowired
	private BureauDao bureauDo;

	public List<Room> findAll() {
		return mongoTemplate.find(new Query(), Room.class);
	}

	public void findAndModify(String id) {
		mongoTemplate.updateFirst(new Query(Criteria.where("id").is(id)), new Update().inc("age", 3), Room.class);

	}

	public Room findOne(String id) {
		return mongoTemplate.findOne(new Query(Criteria.where("id").is(id)), Room.class);

	}

	public List<Room> findByCodeAndType(String code, int type) {
		Query query = new Query();
		query.addCriteria(new Criteria("code").is(code));
		query.addCriteria(new Criteria("type").is(type));
		return mongoTemplate.find(query, Room.class);

	}

	public Room findByCode(String code) {
		Query query = new Query();
		query.addCriteria(new Criteria("code").is(code));
		return mongoTemplate.findOne(query, Room.class);

	}

	public List<Room> findByType(int type) {
		Query query = new Query();
		query.addCriteria(new Criteria("type").is(type));
		return mongoTemplate.find(query, Room.class);

	}

	public void insert(Room room) {
		mongoTemplate.insert(room);
	}

	public void update(Room room) {
		Query query = new Query();
		query.addCriteria(new Criteria("_id").is(room.getId()));
		Update update = new Update();
		update.set("bottomNote", room.getBottomNote());
		update.set("code", room.getCode());
		mongoTemplate.updateFirst(query, update, Room.class);
	}

	public void createRooms(List<Room> rooms) {
		for (Room room : rooms) {
			createRoom(room);
		}
	}

	/**
	 * 创建房间
	 * 
	 * @param room
	 */
	public void createRoom(Room room) {
		saveRoom(room);
		for (int i = 0; i < Room.MAX_SEAT_COUNT; i++) {
			Seat seat = new Seat(i, room);
			seatDao.saveSeat(seat);
		}
	}

	/**
	 * 根据编号获取房间对象
	 * 
	 * @param code
	 * @return
	 */
	public Room getRoomDtoByCode(String code) {
		return mongoTemplate.findOne(new Query(Criteria.where("code").is(code)), Room.class);
	}

	/**
	 * 更新房间的倒计时
	 * 
	 * @param roomCode
	 * @param countDown
	 */
	public void updateRoomCountDown(String roomCode, int countDown) {
		mongoTemplate.updateFirst(new Query(Criteria.where("code").is(roomCode)), new Update().addToSet("countDown", countDown), Room.class);
	}

	/**
	 * 更新房间的准备人的个数
	 * 
	 * @param roomCode
	 * @param readyCount
	 */
	public void updateRoomReadyCount(String roomCode, int readyCount) {
		mongoTemplate.updateFirst(new Query(Criteria.where("code").is(roomCode)), new Update().addToSet("readyCount", readyCount), Room.class);
	}

	/**
	 * 保存房间，只适用于创建时调用，否则可能导致属性值覆盖 保存前删除之前的Room Bureau Seat
	 * 
	 * @param room
	 */
	public void saveRoom(Room room) {
		// 删除已有的room与Seat
		mongoTemplate.remove(new Query(Criteria.where("code").is(room.getCode())), Room.class);
		mongoTemplate.remove(new Query(Criteria.where("roomCode").is(room.getCode())), Bureau.class);
		mongoTemplate.remove(new Query(Criteria.where("roomCode").is(room.getCode())), Seat.class);
		mongoTemplate.save(room);
	}

	/**
	 * 进入房间时找一个合适的座位让玩家坐下
	 * 
	 * @param player
	 * @param roomType
	 * @return
	 */
	public RoomDto findFitSeat(Player player, int roomType) {
		// 尝试7次
		int count = 0;
		boolean hasSit = false;
		int index = -1;
		Room room = null;
		while (count <= 6 && (!hasSit)) {
			room = mongoTemplate.findOne(new Query(Criteria.where("sitCount").is(Room.MAX_SEAT_COUNT - 1 - count).and("type").is(roomType)),
			                             Room.class);
			if (null != room) {
				for (int i = 0; i < Room.MAX_SEAT_COUNT; i++) {
					index = i;
					// 检查是否已坐上
					hasSit = seatDao.updateSeatSit(room.getCode(), index, player);
					if (hasSit) {
						break;
					}
				}
			}
			count++;
		}
		if (hasSit) {
			String roomCode = room.getCode();
			Seat seat = seatDao.getSeat(roomCode, index);
			seat.playerSit(player);
			Bureau bureau = bureauDo.getLastBureauDtoInRoom(roomCode);
			Seat[] seats = seatDao.getSeatsInRoom(roomCode);
			return new RoomDto(room, bureau, seats);
		} else {
			return null;
		}

	}
}
