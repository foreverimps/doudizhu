package com.zj.platform.gamecenter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zj.platform.gamecenter.entity.Bureau;
import com.zj.platform.gamecenter.entity.Card;
import com.zj.platform.gamecenter.mongoDao.BureauDao;

@Service
public class BureauService {

	@Autowired
	private BureauDao bureauDao;

	public void save(Bureau bureauDto) {
		// TODO
	}

	public Bureau getLastBureauDtoInRoom(String roomCode) {
		return bureauDao.getLastBureauDtoInRoom(roomCode);
	}

	public void updateBureauCountDown(String id, int curCount) {
		bureauDao.updateBureauCountDown(id, curCount);
	}

	public void saveBureau(Bureau bureau) {
		bureauDao.saveBureauDto(bureau);
	}

	public void updateBureauCurIndex(String id, int curIndex) {
		bureauDao.updateBureauCurIndex(id, curIndex);
	}

	public void updateBureauRound(String id, int round) {
		bureauDao.updateBureauRound(id, round);
	}

	public void updateBureauCards(String id, List<Card> onePairCard) {
		bureauDao.updateBureauCards(id, onePairCard);
	}

	public void updateBureauTotalNote(String id, Long totalNote) {
		bureauDao.updateBureauTotalNote(id, totalNote);
	}

	public void updateBureauSingleNote(String id, int singleNote) {
		bureauDao.updateBureauSingleNote(id, singleNote);
	}

	public void changeBureauDtoRoundBeginIndex(String id, int index) {
		bureauDao.changeBureauDtoRoundBeginIndex(id, index);
	}

	public boolean updateBureauActiveCount(String id, int count, int index) {
		return bureauDao.updateBureauActiveCount(id, count, index);
	}

	public Bureau queryBureau(String roomCode) {
		return bureauDao.getLastBureauDtoInRoom(roomCode);
	}
}
