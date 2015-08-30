package com.zj.platform.gamecenter.entity;

import java.util.List;

import com.zj.platform.gamecenter.constant.EventTypeEnum;
import com.zj.platform.gamecenter.constant.EventTypeEnum.ThreeCardsEventTypeEnum;
import com.zj.platform.gamecenter.constant.PlayerStatusEnum;
import com.zj.platform.gamecenter.events.AyncUserEventPublisher;
import com.zj.platform.gamecenter.events.ThreeCardsEvent;
import com.zj.platform.gamecenter.service.BureauService;
import com.zj.platform.gamecenter.service.PlayerService;
import com.zj.platform.gamecenter.service.RoomService;
import com.zj.platform.gamecenter.service.SeatService;
import com.zj.platform.gamecenter.utils.ApplicationContextUtil;
import com.zj.platform.gamecenter.utils.CardUtils;

public class Seat {

	private static BureauService bureauService = ApplicationContextUtil.getBean(BureauService.class);

	private static RoomService roomService = ApplicationContextUtil.getBean(RoomService.class);

	private static SeatService seatService = ApplicationContextUtil.getBean(SeatService.class);

	private static AyncUserEventPublisher publisher = ApplicationContextUtil.getBean(AyncUserEventPublisher.class);

	private static PlayerService playerService = ApplicationContextUtil.getBean(PlayerService.class);

	/** 座位序号 */
	private int index;

	private int previousIndex;

	private int nextIndex;

	/** 座位上的玩家 */
	private String roomCode;

	private Boolean isCheck;

	private Boolean isOut;

	private Boolean isLose;

	private Boolean giveUp;

	private Long userId;

	public Seat(int index, Room roomDto) {
		roomCode = roomDto.getCode();
		previousIndex = -1;
		nextIndex = -1;
		this.index = index;
		userId = null;
		init();
	}

	// 初始化
	private void init() {
		isOut = true;
		isLose = true;
		isCheck = true;
		giveUp = true;
	}

	/**
	 * 开局前初始化，设置属性
	 */
	public void initBeforeBureau() {
		if (null != userId) {
			isOut = false;
			isLose = false;
			isCheck = false;
			giveUp = false;
		}
	}

	/**
	 * @return 被踢的玩家
	 */
	public Player kickPlayerDto() {
		Player player = playerService.getPlayerByUserId(userId);
		if (null != userId) {
			// 抛出用户离座事件
			playerService.updatePlayDtoStatus(userId, PlayerStatusEnum.WAIT.getCode());
			seatService.updateSeatKick(roomCode, index);
			ThreeCardsEvent event = new ThreeCardsEvent(userId, EventTypeEnum.ThreeCardsEventTypeEnum.STAND);
			event.setIndex(index);
			event.setRoomCode(roomCode);
			publisher.publishUserEvent(event);
		}
		return player;
	}

	/**
	 * 准备
	 */
	public void sayReady() {
		Player player = playerService.getPlayerByUserId(userId);// 根据userId获取玩家信息
		if (null == player || player.getStatus() == PlayerStatusEnum.GAMEING.getCode()) {
			return;
		}
		Room roomDto = roomService.getRoomDtoByCode(roomCode);
		int readyCount = roomDto.getReadyCount() + 1;
		if (player.getStatus() != PlayerStatusEnum.READY.getCode()) {// TODO
			                                                         // 是不是只能等待状态才能准备？
			player.setStatus(PlayerStatusEnum.READY.getCode());
			playerService.updatePlayDtoStatus(userId, PlayerStatusEnum.READY.getCode());
			roomDto.setReadyCount(readyCount);
			roomService.updateRoomReadyCount(roomCode, readyCount);
		}
		Seat[] seats = seatService.getSeatsInRoom(roomCode);
		if (readyCount == seats.length) {// TODO seats.lengt 这个是坐下了
			                             // 才加1
			// 判断是否可直接开局
			Bureau bureauDto = bureauService.getLastBureauDtoInRoom(roomCode);
			roomDto.createBureauDto(bureauDto);
		} else {
			// 抛出用户准备事件
			ThreeCardsEvent readyEvent = new ThreeCardsEvent(userId, EventTypeEnum.ThreeCardsEventTypeEnum.READY);
			readyEvent.setRoomCode(roomCode);
			publisher.publishUserEvent(readyEvent);
			// 判断是否开始准备前的倒计时
			// 如果已在倒计时过程中，则不新开倒计时
			if (readyCount >= 3 && roomDto.getCountDown() == 0) {
				// 开始开局前倒计时
				ThreeCardsEvent countDownBeforeBureauEvent = new ThreeCardsEvent(player.getUserId(),
				        EventTypeEnum.ThreeCardsEventTypeEnum.COUNT_DOWN_BEFORE_BUREAU);
				readyEvent.setRoomCode(roomCode);
				publisher.publishUserEvent(countDownBeforeBureauEvent);
			}
		}
	}

	/**
	 * 看牌
	 */
	public List<Card> check() {
		Player player = playerService.getPlayerByUserId(userId);
		if (null == player || player.getStatus() != PlayerStatusEnum.GAMEING.getCode()) {
			return null;
		}
		isCheck = true;
		seatService.updateSeatCheck(roomCode, index);
		ThreeCardsEvent checkEvent = new ThreeCardsEvent(userId, ThreeCardsEventTypeEnum.CHECK_CARD);
		checkEvent.setRoomCode(roomCode);
		publisher.publishUserEvent(checkEvent);
		return player.getCards();
	}

	/**
	 * 弃牌
	 */
	public boolean giveUp() {
		Player player = playerService.getPlayerByUserId(userId);
		if (null == player || player.getStatus() != PlayerStatusEnum.GAMEING.getCode()) {
			return false;
		}
		Bureau bureauDto = bureauService.getLastBureauDtoInRoom(roomCode);
		int nextIndex = -1;
		if (index == bureauDto.getCurIndex()) {
			nextIndex = bureauDto.changeCurSeat();
		}
		boolean result = bureauDto.desActiveCount(index);
		if (result && nextIndex != -1) {
			giveUp = true;
			seatService.updateSeatGiveup(roomCode, index);
			out();
			// 抛出弃牌事件
			ThreeCardsEvent giveUpEvent = new ThreeCardsEvent(userId, ThreeCardsEventTypeEnum.NOTES_OUT);
			giveUpEvent.setRoomCode(roomCode);
			giveUpEvent.setIndex(index);
			giveUpEvent.setCurIndex(nextIndex);
			publisher.publishUserEvent(giveUpEvent);
			if (bureauDto.getIsEnd()) {
				// 抛出结束事件 如果只剩下一个人，则牌局结束，计算亏损
				ThreeCardsEvent threeCardsEndEvent = new ThreeCardsEvent(null, ThreeCardsEventTypeEnum.GAME_END);
				publisher.publishUserEvent(threeCardsEndEvent);
			}
			return true;
		}
		return false;
	}

	/**
	 * 用户占座 <br>
	 * 已控制并发
	 * 
	 * @param player
	 * @return true 占座成功 false 占座失败
	 */
	public boolean playerSit(Player player) {
		if (player == null || null != userId) {
			return false;
		}
		userId = player.getUserId();
		player.setBureauIndex(0);
		player.setRoomCode(roomCode);
		playerService.updatePlayerRoomCode(player.getUserId(), roomCode);
		playerService.updatePlayerBureauIndex(player.getUserId(), 0);
		// 抛出用户占座事件
		ThreeCardsEvent event = new ThreeCardsEvent(player.getUserId(), EventTypeEnum.ThreeCardsEventTypeEnum.SIT);
		event.setIndex(index);
		event.setRoomCode(roomCode);
		publisher.publishUserEvent(event);
		return true;
	}

	/**
	 * 跟注
	 * 
	 * @return 操作是否成功
	 */
	public boolean noteOn() {
		Player player = playerService.getPlayerByUserId(userId);
		Bureau bureauDto = bureauService.getLastBureauDtoInRoom(roomCode);
		if (null == bureauDto || bureauDto.getCurIndex() != index
		        || (null == player || player.getStatus() != PlayerStatusEnum.GAMEING.getCode() || isOut)) {
			return false;
		}
		// 不能小于单注
		if (!isCheck && player.getMoneyAccountAmount() < bureauDto.getSingleNote()) {
			return false;
		}
		if (isCheck && player.getMoneyAccountAmount() < 2 * bureauDto.getSingleNote()) {
			return false;
		}
		int note = 0;
		if (isCheck) {
			note = bureauDto.getSingleNote() * 2;
		} else {
			note = bureauDto.getSingleNote();
		}
		handleNote(player, note);
		ThreeCardsEvent noteOnEvent = new ThreeCardsEvent(userId, ThreeCardsEventTypeEnum.NOTES_IN);
		noteOnEvent.setRoomCode(roomCode);
		noteOnEvent.setNote(note);
		noteOnEvent.setCurIndex(bureauDto.changeCurSeat());
		noteOnEvent.setRound(bureauDto.getRound());
		publisher.publishUserEvent(noteOnEvent);
		return true;
	}

	public boolean handleNote(Player player, int amount) {
		// 对个人注码的修改
		Bureau bureauDto = bureauService.getLastBureauDtoInRoom(roomCode);
		Long sumNote = player.getSumNote();
		player.setSumNote(sumNote + amount);
		// 更新玩家上的总注数
		playerService.updatePlayerSumNote(player.getUserId(), sumNote + amount);
		// 对局中注码的修改
		Long totalNote = bureauDto.getTotalNote();
		bureauDto.setTotalNote(totalNote + amount);
		// 更新局中的总注
		bureauService.updateBureauTotalNote(bureauDto.getId(), totalNote);
		return true;
	}

	/**
	 * 加注
	 * 
	 * @param amount
	 * @return
	 */
	public boolean noteAdd(int amount) {
		Player player = playerService.getPlayerByUserId(userId);
		Bureau bureauDto = bureauService.getLastBureauDtoInRoom(roomCode);
		if (null == bureauDto || bureauDto.getCurIndex() != index
		        || (null == player || player.getStatus() != PlayerStatusEnum.GAMEING.getCode() || isOut)) {
			return false;
		}
		// TODO 筹码种类处理
		if (amount < bureauDto.getSingleNote()) {
			return false;
		}
		if (amount > bureauDto.getMaxNote()) {
			return false;
		}
		// 不能小于单注
		if (!isCheck && player.getMoneyAccountAmount() < amount) {
			return false;
		}
		if (isCheck && player.getMoneyAccountAmount() < 2 * amount) {
			return false;
		}
		bureauDto.setSingleNote(amount);
		bureauService.updateBureauSingleNote(bureauDto.getId(), amount);
		int note = 0;
		if (isCheck) {
			note = bureauDto.getSingleNote() * 2;
		} else {
			note = bureauDto.getSingleNote();
		}
		handleNote(player, note);
		// 抛出加注的事件
		ThreeCardsEvent noteAddEvent = new ThreeCardsEvent(userId, ThreeCardsEventTypeEnum.NOTES_ADD);
		noteAddEvent.setRoomCode(roomCode);
		noteAddEvent.setNote(note);
		noteAddEvent.setCurIndex(bureauDto.changeCurSeat());
		noteAddEvent.setRound(bureauDto.getRound());
		publisher.publishUserEvent(noteAddEvent);
		return true;
	}

	/**
	 * 比牌
	 * 
	 * @param index
	 *            比牌的位置序号
	 * @return
	 */
	public boolean cardCompare(int index) {
		Player player = playerService.getPlayerByUserId(userId);
		Bureau bureauDto = bureauService.getLastBureauDtoInRoom(roomCode);
		if (null == bureauDto || bureauDto.getCurIndex() != index
		        || (null == player || player.getStatus() != PlayerStatusEnum.GAMEING.getCode() || isOut)) {
			return false;
		}
		Seat[] seats = seatService.getSeatsInRoom(roomCode);
		if (index >= seats.length) {
			return false;
		}
		Seat cSeat = seats[index];
		if (null == cSeat.userId || cSeat.isOut) {
			return false;
		}
		Player cPlayer = playerService.getPlayerByUserId(cSeat.userId);
		// 对本人与注码的处理
		Long note = 0L;
		if (isCheck) {
			note = bureauDto.getSingleNote() * 4L;
		} else {
			note = bureauDto.getSingleNote() * 2L;
		}
		Long playerLeftNote = player.getMoneyAccountAmount();
		note = note < playerLeftNote ? note : playerLeftNote;
		int result = CardUtils.compareCard(player.getCards(), cPlayer.getCards());
		Seat loser = null;
		if (result == 1) {
			// 对方出局
			loser = cSeat;
		} else {
			// 比牌输 本人出局
			loser = this;
		}
		if (bureauDto.desActiveCount(loser.index)) {
			handleNote(player, note.intValue());
			loser.isLose = true;
			seatService.updateSeatLose(roomCode, loser.index);
			loser.out();
			// 抛出比牌事件 与结果
			ThreeCardsEvent compareCardsEvent = new ThreeCardsEvent(userId, ThreeCardsEventTypeEnum.COMPARE_CARDS);
			compareCardsEvent.setRoomCode(roomCode);
			compareCardsEvent.setCompareTo(cSeat);
			compareCardsEvent.setNote(note.intValue());
			compareCardsEvent.setCurIndex(bureauDto.changeCurSeat());
			compareCardsEvent.setRound(bureauDto.getRound());
			publisher.publishUserEvent(compareCardsEvent);
			if (bureauDto.getIsEnd()) {
				// 抛出结束事件 如果只剩下一个人，则牌局结束，计算亏损
				ThreeCardsEvent threeCardsEndEvent = new ThreeCardsEvent(null, ThreeCardsEventTypeEnum.GAME_END);
				threeCardsEndEvent.setRoomCode(roomCode);
				publisher.publishUserEvent(threeCardsEndEvent);
			}
			return true;
		}
		return false;
	}

	/**
	 * 出局处理
	 */
	public void out() {
		isOut = true;
		Seat[] seats = seatService.getSeatsInRoom(roomCode);
		seats[previousIndex].nextIndex = nextIndex;
		seats[nextIndex].previousIndex = previousIndex;
		// 保存Seat
		seatService.updateSeatNextIndex(roomCode, previousIndex, seats[previousIndex].nextIndex);
		seatService.updateSeatPreviousIndex(roomCode, nextIndex, seats[nextIndex].previousIndex);
		seatService.updateSeatOut(roomCode, index);
		Bureau bureauDto = bureauService.getLastBureauDtoInRoom(roomCode);
		// 修改一轮开始座位
		if (index == bureauDto.getRoundBeginIndex()) {
			bureauDto.setRoundBeginIndex(nextIndex);
			// 保存缓存中的BureauDto
			bureauService.changeBureauDtoRoundBeginIndex(bureauDto.getId(), nextIndex);
		}
	}

	// ======================get方法=====================//

	public Boolean getIsLose() {
		return isLose;
	}

	public void setIsLose(Boolean isLose) {
		this.isLose = isLose;
	}

	public Boolean getGiveUp() {
		return giveUp;
	}

	public void setGiveUp(Boolean giveUp) {
		this.giveUp = giveUp;
	}

	public Boolean getIsOut() {
		return isOut;
	}

	public void setIsOut(Boolean isOut) {
		this.isOut = isOut;
	}

	public Boolean getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(Boolean isCheck) {
		this.isCheck = isCheck;
	}

	public int getIndex() {
		return index;
	}

	public int getPreviousIndex() {
		return previousIndex;
	}

	public void setPreviousIndex(int previousIndex) {
		this.previousIndex = previousIndex;
	}

	public int getNextIndex() {
		return nextIndex;
	}

	public void setNextIndex(int nextIndex) {
		this.nextIndex = nextIndex;
	}

	public String getRoomCode() {
		return roomCode;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setRoomCode(String roomCode) {
		this.roomCode = roomCode;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
