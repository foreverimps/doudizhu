package com.zj.platform.gamecenter.eventHandlers;

import org.springframework.beans.factory.annotation.Autowired;

import com.zj.platform.gamecenter.constant.EventTypeEnum.ThreeCardsEventTypeEnum;
import com.zj.platform.gamecenter.constant.PlayGameTypeEnum;
import com.zj.platform.gamecenter.dto.socket.ActionDto;
import com.zj.platform.gamecenter.events.UserEvent;
import com.zj.platform.gamecenter.jms.RemoteMessageSender;
import com.zj.platform.gamecenter.service.MongoCacheService;
import com.zj.platform.gamecenter.service.PlayerService;

/**
 * 新一轮开始处理 <br>
 * 不需要用 <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2015年1月6日 下午11:02:24
 * <p>
 * Company:
 * <p>
 * 
 * @author H.L.Wan
 * @version 1.0.0
 */
public class ThreeCardsRoundEventHandler extends SystemHandler {

	@Autowired
	private RemoteMessageSender messageSender;

	@Autowired
	private PlayerService playerService;

	@Autowired
	private MongoCacheService cacheService;

	@Override
	public int eventCode() {
		return ThreeCardsEventTypeEnum.ROUND_START.getCode();
	}

	@Override
	public void handleEvent(UserEvent userEvent) {
		ActionDto action = new ActionDto();
		action.setUserId(null);
		action.setCommandId(PlayGameTypeEnum.ROUND_START.getCode());
		String roomCode = userEvent.getRoomCode();
		action.setContent(roomCode);
		messageSender.sendToUserIds(action, playerService.getUserIdsByRoomCode(roomCode));
	}

}
