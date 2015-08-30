package com.zj.platform.gamecenter.eventHandlers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zj.platform.gamecenter.constant.EventTypeEnum;
import com.zj.platform.gamecenter.constant.PlayGameTypeEnum;
import com.zj.platform.gamecenter.dto.socket.ActionDto;
import com.zj.platform.gamecenter.entity.Player;
import com.zj.platform.gamecenter.events.UserEvent;
import com.zj.platform.gamecenter.jms.RemoteMessageSender;
import com.zj.platform.gamecenter.service.MongoCacheService;
import com.zj.platform.gamecenter.service.PlayerService;

/**
 * 站起事件的处理. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2015年1月4日 下午11:17:15
 * <p>
 * Company:
 * <p>
 * 
 * @author H.L.Wan
 * @version 1.0.0
 */
@Component
public class PlayerStandEventHandler extends SystemHandler {

	@Autowired
	private PlayerService playerService;

	@Autowired
	private RemoteMessageSender messageSender;

	@Autowired
	private MongoCacheService cacheService;

	@Override
	public int eventCode() {
		return EventTypeEnum.ThreeCardsEventTypeEnum.STAND.getCode();
	}

	@Override
	public void handleEvent(UserEvent userEvent) {
		String roomCode = userEvent.getRoomCode();
		int index = userEvent.getIndex();
		List<Long> userIds = playerService.getUserIdsByRoomCode(roomCode);
		ActionDto standActionDto = new ActionDto();
		standActionDto.setUserId(userEvent.getUserId());
		standActionDto.setRoomCode(roomCode);
		standActionDto.setCommandId(PlayGameTypeEnum.STAND.getCode());
		standActionDto.setContent(index);
		messageSender.sendToUserIds(standActionDto, userIds);
		Player player = playerService.getPlayerByUserId(userEvent.getUserId());
		player.setMoneyAccountAmount(player.getMoneyAccountAmount() - player.getSumNote());
		playerService.updatePlayerMoneyAmount(player);
	}

}
