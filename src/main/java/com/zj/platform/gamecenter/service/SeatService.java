package com.zj.platform.gamecenter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zj.platform.gamecenter.entity.Player;
import com.zj.platform.gamecenter.entity.Seat;
import com.zj.platform.gamecenter.mongoDao.SeatDao;

@Service
public class SeatService {

	@Autowired
	private SeatDao seatDao;

	public Seat getSeat(String roomCode, int index) {
		return seatDao.getSeat(roomCode, index);
	}

	public Seat[] getSeatsInRoom(String roomCode) {
		return seatDao.getSeatsInRoom(roomCode);
	}

	public List<Seat> getPlayingSeatInRoom(String roomCode) {
		return new ArrayList<Seat>();
	}

	public void saveSeats(Seat[] seats) {
		seatDao.saveSeats(seats);
	}

	public boolean updateSeatSit(String roomCode, int index, Player player) {
		return seatDao.updateSeatSit(roomCode, index, player);
	}

	public void updateSeatNextIndex(String roomCode, int index, int nextIndex) {
		seatDao.updateSeatNextIndex(roomCode, index, nextIndex);
	}

	public void updateSeatPreviousIndex(String roomCode, int index, int previousIndex) {
		seatDao.updateSeatPreviousIndex(roomCode, index, previousIndex);
	}

	public void updateSeatOut(String roomCode, int index) {
		seatDao.updateSeatOut(roomCode, index);
	}

	public void updateSeatKick(String roomCode, int index) {
		seatDao.updateSeatKick(roomCode, index);
	}

	public void updateSeatCheck(String roomCode, int index) {
		seatDao.updateSeatCheck(roomCode, index);
	}

	public void updateSeatGiveup(String roomCode, int index) {
		seatDao.updateSeatGiveup(roomCode, index);
	}

	public void updateSeatLose(String roomCode, int index) {
		seatDao.updateSeatLose(roomCode, index);
	}

	/**
	 * 随机获取其他座位的索引
	 * 
	 * @param index
	 * @param seatList
	 * @return
	 */
	public Seat randomGetSet(Long userId, List<Seat> seatList) {
		List<Seat> list = new ArrayList<Seat>();
		for (Seat seat : seatList) {
			if (userId.longValue() != seat.getUserId()) {
				list.add(seat);
			}
		}
		Random random = new Random(list.size());
		int random_num = random.nextInt();
		return list.get(random_num);
	}

}
