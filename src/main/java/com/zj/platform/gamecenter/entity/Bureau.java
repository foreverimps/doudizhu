package com.zj.platform.gamecenter.entity;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import com.zj.platform.gamecenter.constant.PlayerStatusEnum;
import com.zj.platform.gamecenter.events.AyncUserEventPublisher;
import com.zj.platform.gamecenter.service.BureauService;
import com.zj.platform.gamecenter.service.PlayerService;
import com.zj.platform.gamecenter.service.SeatService;
import com.zj.platform.gamecenter.utils.ApplicationContextUtil;

/**
 * 当人数达到游戏最低要求时由房间RoomDto对象创建一局游戏 <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2015年1月1日 上午11:08:11
 * <p>
 * Company:
 * <p>
 * 
 * @author
 * @version 1.0.0
 */
public class Bureau {

	private static AyncUserEventPublisher publisher = ApplicationContextUtil.getBean(AyncUserEventPublisher.class);

	private static SeatService seatService = ApplicationContextUtil.getBean(SeatService.class);

	private static BureauService bureauService = ApplicationContextUtil.getBean(BureauService.class);

	private static PlayerService playerService = ApplicationContextUtil.getBean(PlayerService.class);

	private String id;

	private String roomCode;

	private int round;// 第几轮

	private Long totalNote;// 总下注数

	private int bottomNote;// 底注

	private int singleNote;// 单注

	private int maxNote;

	private int countDown;// 倒计时

	private Boolean isEnd;

	private int startSeatIndex;

	private int activeCount;

	private int curIndex;

	private int roundBeginIndex;

	private int lastestOutIndex;

	private List<Card> onePairCard;// 一副牌

	/** 有序 */
	private Set<Player> playerList = new TreeSet<Player>();// 局里面的玩家集合

	public Bureau(Room roomDto, int bottomNote, int maxNote) {
		int[] cards = { 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 };
		int[] colors = { 1, 2, 3, 4 };// 黑，红，梅，方

		for (int card : cards) {
			for (int color : colors) {
				Card _card = new Card(card, color);
				onePairCard.add(_card);
			}
		}
		startSeatIndex = -1;
		roundBeginIndex = -1;
		lastestOutIndex = -1;
		curIndex = -1;
		roomCode = roomDto.getCode();
		round = 0;
		this.maxNote = maxNote;
		this.bottomNote = bottomNote;
		singleNote = bottomNote;
		isEnd = false;
		countDown = 0;
		totalNote = 0L;
		// 设置座位和玩家
		Seat[] seats = seatService.getSeatsInRoom(roomCode);
		// 设置startSeatIndex
		randomStart(seats);
		activeCount = playerList.size();

	}

	/**
	 * 开局的时候调用
	 */
	private void randomStart(Seat[] seats) {
		Random r = new Random();
		int len = seats.length;
		int start = r.nextInt(len);
		int preIndex = -1;
		for (int i = start; i < start + len; i++) {
			int index = i;
			if (index >= len) {
				index = index - len;
			}
			Seat seat = seats[index];
			Player player = playerService.getPlayerByUserId(seat.getUserId());
			if (null != player && player.getStatus() == PlayerStatusEnum.READY.getCode()) {
				seat.initBeforeBureau();
				playerList.add(player);
				player.setStatus(PlayerStatusEnum.GAMEING.getCode());
				player.setBureauIndex(player.getBureauIndex() + 1);
				playerService.updatePlayerBureauIndex(player.getUserId(), player.getBureauIndex());
				if (startSeatIndex == -1) {
					startSeatIndex = index;
					roundBeginIndex = index;
				}
				if (preIndex != -1) {
					seats[preIndex].setNextIndex(index);
					seat.setPreviousIndex(preIndex);
				}
				preIndex = index;
			} else {
				// 踢掉座位上的玩家 并重置玩家状态
				seat.kickPlayerDto();
				if (null != player) {
					player.setStatus(PlayerStatusEnum.WAIT.getCode());
				}
			}
			playerService.updatePlayDtoStatus(player.getUserId(), player.getStatus());
		}
		seats[startSeatIndex].setPreviousIndex(preIndex);
		seats[preIndex].setNextIndex(startSeatIndex);
		seatService.saveSeats(seats);
	}

	/**
	 * 下一个玩家开始
	 * 
	 * @return
	 */
	public int changeCurSeat() {
		Seat[] seats = seatService.getSeatsInRoom(roomCode);
		if (isEnd) {
			return -1;
		}
		if (curIndex == -1) {
			// 第一轮的情况
			curIndex = roundBeginIndex;
		} else {
			Seat curSeat = seats[curIndex];
			curIndex = curSeat.getNextIndex();
			// 如果已出局，删除没必要的指针
		}
		bureauService.updateBureauCurIndex(id, curIndex);
		// 抛出当前人变动事件 倒计时开始
		// ThreeCardsEvent curChangeEvent = new
		// ThreeCardsEvent(seats[curIndex].getUserId(),
		// ThreeCardsEventTypeEnum.NOTES_NEXT);
		// curChangeEvent.setRoomCode(roomCode);
		// curChangeEvent.setCurIndex(curIndex);
		// curChangeEvent.setRound(round);
		// publisher.publishUserEvent(curChangeEvent);
		if (curIndex == roundBeginIndex) {
			round++;
			bureauService.updateBureauRound(id, round);
			// 抛出新一轮事件
			// ThreeCardsEvent roundEvent = new ThreeCardsEvent(null,
			// ThreeCardsEventTypeEnum.ROUND_START);
			// roundEvent.putContextElement("room", roomCode);
			// publisher.publishUserEvent(roundEvent);
		}
		return curIndex;
	}

	/**
	 * 将当前局中有效人数减1，适用于弃牌与比牌
	 * 
	 * @param index
	 *            出局的位置序号
	 * @return 是否成功出局
	 */
	public boolean desActiveCount(int index) {
		synchronized (this) {
			if (!isEnd) {
				activeCount--;
				boolean updateResult = bureauService.updateBureauActiveCount(id, activeCount, index);
				if (!updateResult) {
					return false;
				}
				if (activeCount == 1) {
					// 结束
					isEnd = true;
				}
				return true;
			}
			return false;
		}
	}

	// ===================set/get方法=================//

	public Boolean getIsEnd() {
		return isEnd;
	}

	public void setIsEnd(Boolean isEnd) {
		this.isEnd = isEnd;
	}

	public int getCountDown() {
		return countDown;
	}

	public void setCountDown(int countDown) {
		this.countDown = countDown;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public Long getTotalNote() {
		return totalNote;
	}

	public void setTotalNote(Long totalNote) {
		this.totalNote = totalNote;
	}

	public Set<Player> getPlayerList() {
		return playerList;
	}

	public void setPlayerList(Set<Player> playerList) {
		this.playerList = playerList;
	}

	public int getStartSeatIndex() {
		return startSeatIndex;
	}

	public void setStartSeatIndex(int startSeatIndex) {
		this.startSeatIndex = startSeatIndex;
	}

	public int getActiveCount() {
		return activeCount;
	}

	public void setActiveCount(int activeCount) {
		this.activeCount = activeCount;
	}

	public int getBottomNote() {
		return bottomNote;
	}

	public void setBottomNote(int bottomNote) {
		this.bottomNote = bottomNote;
	}

	public int getSingleNote() {
		return singleNote;
	}

	public void setSingleNote(int singleNote) {
		this.singleNote = singleNote;
	}

	public int getCurIndex() {
		return curIndex;
	}

	public void setCurIndex(int curIndex) {
		this.curIndex = curIndex;
	}

	public int getRoundBeginIndex() {
		return roundBeginIndex;
	}

	public void setRoundBeginIndex(int roundBeginIndex) {
		this.roundBeginIndex = roundBeginIndex;
	}

	public String getRoomCode() {
		return roomCode;
	}

	public void setRoomCode(String roomCode) {
		this.roomCode = roomCode;
	}

	public int getLastestOutIndex() {
		return lastestOutIndex;
	}

	public void setLastestOutIndex(int lastestOutIndex) {
		this.lastestOutIndex = lastestOutIndex;
	}

	public int getMaxNote() {
		return maxNote;
	}

	public void setMaxNote(int maxNote) {
		this.maxNote = maxNote;
	}

	public List<Card> getOnePairCard() {
		return onePairCard;
	}

	public void setOnePairCard(List<Card> onePairCard) {
		this.onePairCard = onePairCard;
	}

}
