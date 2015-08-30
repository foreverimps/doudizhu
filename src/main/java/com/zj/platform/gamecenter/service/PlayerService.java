package com.zj.platform.gamecenter.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zj.platform.gamecenter.entity.Bureau;
import com.zj.platform.gamecenter.entity.Card;
import com.zj.platform.gamecenter.entity.Player;
import com.zj.platform.gamecenter.mongoDao.PlayerDao;
import com.zj.platform.gamecenter.utils.CardUtils;

@Service
public class PlayerService {

	@Autowired
	private BureauService bureauService;

	@Autowired
	private PlayerDao playerDao;

	@Autowired
	private CardService cardService;

	/**
	 * 此用户的手牌是否为最大
	 * 
	 * @param userId
	 * @return
	 */
	public boolean isMaxCard(Long userId, String roomCode) {
		Bureau bureau = bureauService.queryBureau(roomCode);
		List<Player> playerlList = playerDao.getPlayingByRoomCode(bureau.getId());
		Long winerUserId = 0L;
		if (CollectionUtils.isNotEmpty(playerlList)) {
			for (int i = 0; i < playerlList.size(); i++) {
				if (i == 0) {
					winerUserId = playerlList.get(i).getUserId();
				} else {
					List<Card> preCard = playerlList.get(i - 1).getCards();
					List<Card> nextCard = playerlList.get(i).getCards();
					if (CardUtils.compareCard(preCard, nextCard) == 1) {
						winerUserId = playerlList.get(i - 1).getUserId();
					} else {
						winerUserId = playerlList.get(i).getUserId();
					}
				}
			}
		}
		if (userId.longValue() == winerUserId.longValue()) {
			return true;
		}
		return false;
	}

	/**
	 * 此用户的手牌是否为最小
	 * 
	 * @param userId
	 * @return
	 */
	public boolean isMinCard(Long userId, String roomCode) {
		Bureau bureau = bureauService.queryBureau(roomCode);
		List<Player> playerlList = playerDao.getPlayingByRoomCode(bureau.getId());
		Long loserUserId = 0L;
		if (CollectionUtils.isNotEmpty(playerlList)) {
			for (int i = 0; i < playerlList.size(); i++) {
				if (i == 0) {
					loserUserId = playerlList.get(i).getUserId();
				} else {
					List<Card> preCard = playerlList.get(i - 1).getCards();
					List<Card> nextCard = playerlList.get(i).getCards();
					if (CardUtils.compareCard(preCard, nextCard) == 1) {
						loserUserId = playerlList.get(i).getUserId();
					} else {
						loserUserId = playerlList.get(i - 1).getUserId();
					}
				}
			}
		}
		if (userId.longValue() == loserUserId.longValue()) {
			return true;
		}
		return false;
	}

	/**
	 * 此用户的手牌是否为最大
	 * 
	 * @param userId
	 * @return
	 */
	public boolean getAllGamingIndex(String roomCode) {
		Bureau bureau = bureauService.queryBureau(roomCode);
		List<Card> user_cards = new ArrayList<Card>();
		List<Player> playerlList = playerDao.getPlayingByRoomCode(bureau.getId());
		if (CollectionUtils.isNotEmpty(playerlList)) {
			for (Player player : playerlList) {
				// if (player.getUserId().longValue() == userId.longValue()) {
				// }
			}
		}
		// TODO 只比较游戏中的玩家，不比较弃牌的
		return true;
	}

	/**
	 * 获取用户手牌的类型
	 * 
	 * @param userId
	 * @param roomCode
	 * @return
	 */
	public int getCardTypeOfUser(Long userId, String roomCode) {
		Bureau bureau = bureauService.queryBureau(roomCode);
		bureau.getPlayerList();
		// TODO
		return 1;
	}

	public Player getPlayerByUserId(Long userId) {
		return playerDao.getPlayerByUserId(userId);
	}

	public void updatePlayerRoomCode(Long userId, String roomCode) {
		playerDao.updatePlayerRoomCode(userId, roomCode);
	}

	public void savePlayer(Player player) {
		playerDao.savePlayer(player);
	}

	public void updatePlayerBureauIndex(Long userId, int index) {
		playerDao.updatePlayerBureauIndex(userId, index);
	}

	public void updatePlayDtoStatus(Long userId, int status) {
		playerDao.updatePlayDtoStatus(userId, status);
	}

	public void updatePlayerSumNote(Long userId, long sumNote) {
		playerDao.updatePlayerSumNote(userId, sumNote);
	}

	public void removePlayer(Long userId) {
		playerDao.removePlayer(userId);
	}

	public List<Long> getUserIdsByRoomCode(String roomCode) {
		return playerDao.getUserIdsByRoomCode(roomCode);
	}

	public void updatePlayerMoneyAmount(Player player) {
		playerDao.updatePlayerMoneyAmount(player);
	}

	public void updatePlayerCard(Long userId, List<Card> card) {
		playerDao.updatePlayerCard(userId, card);
	}
}
