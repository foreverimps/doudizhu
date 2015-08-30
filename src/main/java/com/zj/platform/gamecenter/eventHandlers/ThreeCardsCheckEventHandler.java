package com.zj.platform.gamecenter.eventHandlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zj.platform.gamecenter.constant.EventTypeEnum.ThreeCardsEventTypeEnum;
import com.zj.platform.gamecenter.constant.PlayGameTypeEnum;
import com.zj.platform.gamecenter.dto.socket.ActionDto;
import com.zj.platform.gamecenter.events.UserEvent;
import com.zj.platform.gamecenter.jms.RemoteMessageSender;
import com.zj.platform.gamecenter.service.MongoCacheService;
import com.zj.platform.gamecenter.service.PlayerService;

/**
 * 看牌事件的处理. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2015年1月4日 下午11:17:33
 * <p>
 * Company:
 * <p>
 * 
 * @author H.L.Wan
 * @version 1.0.0
 */
@Component
public class ThreeCardsCheckEventHandler extends SystemHandler {

	@Autowired
	private RemoteMessageSender messageSender;

	@Autowired
	private PlayerService playerService;

	@Autowired
	private MongoCacheService cacheService;

	@Override
	public int eventCode() {
		return ThreeCardsEventTypeEnum.CHECK_CARD.getCode();
	}

	@Override
	public void handleEvent(UserEvent userEvent) {
		ActionDto actionDto = new ActionDto();
		actionDto.setUserId(userEvent.getUserId());
		actionDto.setCommandId(PlayGameTypeEnum.LOOK.getCode());
		String roomCode = userEvent.getRoomCode();
		actionDto.setRoomCode(roomCode);
		messageSender.sendToUserIds(actionDto, playerService.getUserIdsByRoomCode(roomCode));
	}

}
