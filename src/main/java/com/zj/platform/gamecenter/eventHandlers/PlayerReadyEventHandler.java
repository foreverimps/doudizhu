package com.zj.platform.gamecenter.eventHandlers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zj.platform.gamecenter.constant.EventTypeEnum;
import com.zj.platform.gamecenter.constant.PlayGameTypeEnum;
import com.zj.platform.gamecenter.dto.socket.ActionDto;
import com.zj.platform.gamecenter.events.UserEvent;
import com.zj.platform.gamecenter.jms.RemoteMessageSender;
import com.zj.platform.gamecenter.service.MongoCacheService;
import com.zj.platform.gamecenter.service.PlayerService;

/**
 * 准备事件的处理. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2015年1月4日 下午11:16:32
 * <p>
 * Company:
 * <p>
 * 
 * @author H.L.Wan
 * @version 1.0.0
 */
@Component
public class PlayerReadyEventHandler extends SystemHandler {

	@Autowired
	private PlayerService playerService;

	@Autowired
	private RemoteMessageSender messageSender;

	@Autowired
	private MongoCacheService cacheService;

	@Override
	public int eventCode() {
		return EventTypeEnum.ThreeCardsEventTypeEnum.READY.getCode();
	}

	@Override
	public void handleEvent(UserEvent userEvent) {
		// 向前台发送消息
		String roomCode = userEvent.getRoomCode();
		List<Long> userIds = playerService.getUserIdsByRoomCode(roomCode);
		ActionDto readyActionDto = new ActionDto();
		readyActionDto.setRoomCode(roomCode);
		readyActionDto.setUserId(userEvent.getUserId());
		readyActionDto.setCommandId(PlayGameTypeEnum.READY.getCode());
		messageSender.sendToUserIds(readyActionDto, userIds);
	}
}
