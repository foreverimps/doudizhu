package com.zj.platform.gamecenter.eventHandlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zj.platform.gamecenter.constant.EventTypeEnum.ThreeCardsEventTypeEnum;
import com.zj.platform.gamecenter.constant.PlayGameTypeEnum;
import com.zj.platform.gamecenter.dto.socket.ActionDto;
import com.zj.platform.gamecenter.entity.Bureau;
import com.zj.platform.gamecenter.entity.Seat;
import com.zj.platform.gamecenter.events.UserEvent;
import com.zj.platform.gamecenter.jms.RemoteMessageSender;
import com.zj.platform.gamecenter.service.BureauService;
import com.zj.platform.gamecenter.service.MongoCacheService;
import com.zj.platform.gamecenter.service.PlayerService;
import com.zj.platform.gamecenter.service.SeatService;

/**
 * 弃牌事件的处理. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2015年1月4日 下午11:18:12
 * <p>
 * Company:
 * <p>
 * 
 * @author H.L.Wan
 * @version 1.0.0
 */
@Component
public class ThreeCardsGiveUpEventHandler extends SystemHandler {

	@Autowired
	private RemoteMessageSender messageSender;

	@Autowired
	private MongoCacheService cacheService;

	@Autowired
	private BureauService bureauService;

	@Autowired
	private PlayerService playerService;

	@Autowired
	private SeatService seatService;

	@Override
	public int eventCode() {
		return ThreeCardsEventTypeEnum.NOTES_OUT.getCode();
	}

	@Override
	public void handleEvent(UserEvent userEvent) {
		String roomCode = userEvent.getRoomCode();
		int nextOptIndex = userEvent.getCurIndex();
		Bureau bureau = bureauService.getLastBureauDtoInRoom(roomCode);
		ActionDto actionDto = new ActionDto();
		actionDto.setUserId(userEvent.getUserId());
		actionDto.setRoomCode(roomCode);
		actionDto.setCommandId(PlayGameTypeEnum.GIVE_UP.getCode());
		actionDto.setContent(roomCode);
		if (nextOptIndex != -1) {
			actionDto.setNextOptIndex(nextOptIndex);
			Seat seat = seatService.getSeat(roomCode, nextOptIndex);
			actionDto.setNextOptUserId(seat.getUserId());
			actionDto.setRound(bureau.getRound());
		}
		// 向前台玩家发送弃牌的消息通知
		messageSender.sendToUserIds(actionDto, playerService.getUserIdsByRoomCode(roomCode));
	}

}
