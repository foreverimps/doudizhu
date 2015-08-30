package com.zj.platform.gamecenter.eventHandlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zj.platform.gamecenter.constant.EventTypeEnum.ThreeCardsEventTypeEnum;
import com.zj.platform.gamecenter.constant.PlayGameTypeEnum;
import com.zj.platform.gamecenter.dto.socket.ActionDto;
import com.zj.platform.gamecenter.entity.Bureau;
import com.zj.platform.gamecenter.entity.Card;
import com.zj.platform.gamecenter.entity.Player;
import com.zj.platform.gamecenter.entity.Seat;
import com.zj.platform.gamecenter.events.AyncUserEventPublisher;
import com.zj.platform.gamecenter.events.UserEvent;
import com.zj.platform.gamecenter.jms.RemoteMessageSender;
import com.zj.platform.gamecenter.service.BureauService;
import com.zj.platform.gamecenter.service.CardService;
import com.zj.platform.gamecenter.service.MongoCacheService;
import com.zj.platform.gamecenter.service.PlayerService;
import com.zj.platform.gamecenter.service.SeatService;

/**
 * 开局事件的处理. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2015年1月4日 下午11:17:54
 * <p>
 * Company:
 * <p>
 * 
 * @author H.L.Wan
 * @version 1.0.0
 */
@Component
public class ThreeCardsGameStartEventHandler extends SystemHandler {

	@Autowired
	private RemoteMessageSender messageSender;

	@Autowired
	private MongoCacheService cacheService;

	@Autowired
	private CardService cardService;

	@Autowired
	private SeatService seatService;

	@Autowired
	private BureauService bureauService;

	@Autowired
	private static AyncUserEventPublisher publisher;

	@Autowired
	private PlayerService playerService;

	@Override
	public int eventCode() {
		return ThreeCardsEventTypeEnum.GAME_START_CARDS.getCode();
	}

	@Override
	public void handleEvent(UserEvent userEvent) {
		// 发牌 发牌结果考虑后期html方式，安全起见在看牌之前不发送至前台
		String roomCode = userEvent.getRoomCode();
		Bureau bureauDto = bureauService.getLastBureauDtoInRoom(roomCode);
		// 向前台用户发送 游戏开局的消息
		ActionDto action = new ActionDto();
		action.setUserId(null);
		action.setCommandId(PlayGameTypeEnum.SEND_CARDS.getCode());
		int firstIndex = bureauDto.changeCurSeat();
		action.setNextOptIndex(firstIndex);
		Seat seat = seatService.getSeat(roomCode, firstIndex);
		action.setNextOptUserId(seat.getUserId());
		action.setRoomCode(roomCode);
		StartDto start = new StartDto();
		Long sumNote = 0L;
		// 上底注
		List<Player> playerDtos = new ArrayList<Player>(bureauDto.getPlayerList());
		List<Long> userIds = new ArrayList<Long>(playerDtos.size());
		for (Player player : playerDtos) {
			playerService.updatePlayerSumNote(player.getUserId(), bureauDto.getBottomNote());
			// if (player.getBureauIndex() > 3) {
			// ThreeCardsEvent bureauCountEvent = new
			// ThreeCardsEvent(player.getUserId(),
			// ThreeCardsEventTypeEnum.BUREAU_COUNT);
			// bureauCountEvent.setBureauCount(player.getBureauIndex());
			// publisher.publishUserEvent(bureauCountEvent);
			// }
			// 局中的注数更新
			sumNote += bureauDto.getBottomNote();
			bureauDto.setTotalNote(sumNote);
			bureauService.updateBureauTotalNote(bureauDto.getId(), sumNote);
			userIds.add(player.getUserId());
		}
		start.setUserIds(userIds);
		start.setSumNote(sumNote);
		action.setContent(start);
		// 给玩家发牌 并保存发牌结果
		messageSender.sendToUserIds(action, playerService.getUserIdsByRoomCode(roomCode));
		Map<Long, List<Card>> userCards = cardService.gen3Card(userIds, bureauDto.getOnePairCard());
		bureauService.updateBureauCards(bureauDto.getId(), bureauDto.getOnePairCard());
		for (Long userId : userCards.keySet()) {
			playerService.updatePlayerCard(userId, userCards.get(userId));
		}
		// 将这一局信息保存到数据库 TODO
		bureauDto = bureauService.getLastBureauDtoInRoom(roomCode);
		bureauService.saveBureau(bureauDto);

	}

	class StartDto {

		private List<Long> userIds;

		private Long sumNote;

		public List<Long> getUserIds() {
			return userIds;
		}

		public void setUserIds(List<Long> userIds) {
			this.userIds = userIds;
		}

		public Long getSumNote() {
			return sumNote;
		}

		public void setSumNote(Long sumNote) {
			this.sumNote = sumNote;
		}

	}
}
