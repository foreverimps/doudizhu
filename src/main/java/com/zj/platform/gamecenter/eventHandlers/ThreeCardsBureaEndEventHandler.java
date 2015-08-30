package com.zj.platform.gamecenter.eventHandlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zj.platform.gamecenter.constant.EventTypeEnum.ThreeCardsEventTypeEnum;
import com.zj.platform.gamecenter.constant.PlayGameTypeEnum;
import com.zj.platform.gamecenter.dto.socket.ActionDto;
import com.zj.platform.gamecenter.entity.Bureau;
import com.zj.platform.gamecenter.entity.Player;
import com.zj.platform.gamecenter.entity.Seat;
import com.zj.platform.gamecenter.events.UserEvent;
import com.zj.platform.gamecenter.jms.RemoteMessageSender;
import com.zj.platform.gamecenter.service.BureauService;
import com.zj.platform.gamecenter.service.MongoCacheService;
import com.zj.platform.gamecenter.service.PlayerService;
import com.zj.platform.gamecenter.service.SeatService;

/**
 * 一局结束的事件处理 <br>
 * <p>
 * Copyright: Copyright (c) 2015年1月8日 下午11:35:30
 * <p>
 * Company:
 * <p>
 * 
 * @author H.L.Wan
 * @version 1.0.0
 */
@Component
public class ThreeCardsBureaEndEventHandler extends SystemHandler {

	@Autowired
	private RemoteMessageSender messageSender;

	@Autowired
	private MongoCacheService cacheService;

	@Autowired
	private SeatService seatService;

	@Autowired
	private BureauService bureauService;

	@Autowired
	private PlayerService playerService;

	@Override
	public int eventCode() {
		return ThreeCardsEventTypeEnum.GAME_END.getCode();
	}

	@Override
	public void handleEvent(UserEvent userEvent) {
		String roomCode = userEvent.getRoomCode();
		Bureau bureau = bureauService.getLastBureauDtoInRoom(roomCode);
		Seat winner = null;
		Seat[] seats = seatService.getSeatsInRoom(roomCode);
		for (Seat seat : seats) {
			if (!seat.getIsOut()) {
				winner = seat;
			}
		}
		if (winner == null) {
			return;
		}
		Long amount = bureau.getTotalNote();
		ActionDto action = new ActionDto();
		action.setUserId(winner.getUserId());
		action.setCommandId(PlayGameTypeEnum.GAME_WIN.getCode());
		action.setRoomCode(roomCode);
		// 玩家连赢的奖励
		// TODO 抽成？普通场扣赢家的 其它的每个人都扣

		// TODO 每个人胜负统计与保存
		Player player = playerService.getPlayerByUserId(winner.getUserId());
		amount = amount + player.getMoneyAccountAmount() - player.getSumNote();
		player.setMoneyAccountAmount(amount);
		playerService.updatePlayerMoneyAmount(player);
		for (Seat seat : seats) {
			if (player.getUserId().equals(seat.getUserId())) {
				continue;
			}
			Player sPlayer = playerService.getPlayerByUserId(seat.getUserId());
			sPlayer.setMoneyAccountAmount(sPlayer.getMoneyAccountAmount() - sPlayer.getSumNote());
			playerService.updatePlayerMoneyAmount(sPlayer);
		}
		messageSender.sendToUserIds(action, playerService.getUserIdsByRoomCode(roomCode));

	}

}
