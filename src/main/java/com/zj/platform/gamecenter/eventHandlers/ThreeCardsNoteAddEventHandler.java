package com.zj.platform.gamecenter.eventHandlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zj.platform.gamecenter.constant.EventTypeEnum.ThreeCardsEventTypeEnum;
import com.zj.platform.gamecenter.constant.PlayGameTypeEnum;
import com.zj.platform.gamecenter.dto.socket.ActionDto;
import com.zj.platform.gamecenter.entity.Seat;
import com.zj.platform.gamecenter.events.UserEvent;
import com.zj.platform.gamecenter.jms.RemoteMessageSender;
import com.zj.platform.gamecenter.service.BureauService;
import com.zj.platform.gamecenter.service.MongoCacheService;
import com.zj.platform.gamecenter.service.PlayerService;
import com.zj.platform.gamecenter.service.SeatService;

/**
 * 玩家加注的事件处理. <br>
 * <p>
 * Copyright: Copyright (c) 2015年1月8日 下午11:08:04
 * <p>
 * Company:
 * <p>
 * 
 * @author H.L.Wan
 * @version 1.0.0
 */
@Component
public class ThreeCardsNoteAddEventHandler extends SystemHandler {

	@Autowired
	private RemoteMessageSender messageSender;

	@Autowired
	private BureauService bureauService;

	@Autowired
	private MongoCacheService cacheService;

	@Autowired
	private PlayerService playerService;

	@Autowired
	private SeatService seatService;

	@Override
	public int eventCode() {
		return ThreeCardsEventTypeEnum.NOTES_ADD.getCode();
	}

	@Override
	public void handleEvent(UserEvent userEvent) {
		ActionDto actionDto = new ActionDto();
		actionDto.setUserId(userEvent.getUserId());
		actionDto.setCommandId(PlayGameTypeEnum.ADD_CALL.getCode());
		Integer amount = userEvent.getNote();
		actionDto.setContent(amount);
		String roomCode = userEvent.getRoomCode();
		actionDto.setRoomCode(roomCode);
		if (userEvent.getCurIndex() != -1) {
			actionDto.setNextOptIndex(userEvent.getCurIndex());
			actionDto.setRound(userEvent.getRound());
			Seat seat = seatService.getSeat(roomCode, userEvent.getCurIndex());
			actionDto.setNextOptUserId(seat.getUserId());
			messageSender.sendToUserIds(actionDto, playerService.getUserIdsByRoomCode(roomCode));
		}
	}

}
