package com.zj.platform.gamecenter.eventHandlers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zj.platform.gamecenter.constant.EventTypeEnum;
import com.zj.platform.gamecenter.constant.PlayGameTypeEnum;
import com.zj.platform.gamecenter.constant.PlayerStatusEnum;
import com.zj.platform.gamecenter.dto.socket.ActionDto;
import com.zj.platform.gamecenter.dto.socket.SeatDto;
import com.zj.platform.gamecenter.entity.Player;
import com.zj.platform.gamecenter.entity.Seat;
import com.zj.platform.gamecenter.events.UserEvent;
import com.zj.platform.gamecenter.jms.RemoteMessageSender;
import com.zj.platform.gamecenter.service.BureauService;
import com.zj.platform.gamecenter.service.MongoCacheService;
import com.zj.platform.gamecenter.service.PlayerService;
import com.zj.platform.gamecenter.service.SeatService;

/**
 * 坐下事件的处理. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2015年1月4日 下午11:16:54
 * <p>
 * Company:
 * <p>
 * 
 * @author H.L.Wan
 * @version 1.0.0
 */
@Component
public class PlayerSitEventHandler extends SystemHandler {

	/** 占位在不准备情况下最多持续15秒 */
	private static final int SITE_TIME_MAX = 15;

	@Autowired
	private RemoteMessageSender messageSender;

	@Autowired
	private MongoCacheService cacheService;

	@Autowired
	private BureauService bureauService;

	@Autowired
	private SeatService seatService;

	@Autowired
	private PlayerService playerService;

	@Override
	public int eventCode() {
		return EventTypeEnum.ThreeCardsEventTypeEnum.SIT.getCode();
	}

	@Override
	public void handleEvent(UserEvent userEvent) {
		// 发送占座消息
		String roomCode = userEvent.getRoomCode();
		List<Long> userIds = playerService.getUserIdsByRoomCode(roomCode);
		int index = userEvent.getIndex();
		Seat seat = seatService.getSeat(roomCode, index);
		Player player = playerService.getPlayerByUserId(seat.getUserId());
		ActionDto sitActionDto = new ActionDto();
		sitActionDto.setUserId(userEvent.getUserId());
		sitActionDto.setCommandId(PlayGameTypeEnum.SIT.getCode());
		sitActionDto.setRoomCode(roomCode);
		sitActionDto.setContent(new SeatDto(seat, player));
		messageSender.sendToUserIds(sitActionDto, userIds);
		int curCount = SITE_TIME_MAX;
		boolean flag = null != player && player.getStatus() == PlayerStatusEnum.WAIT.getCode() && player.getUserId().equals(userEvent.getUserId());
		while (flag && curCount >= 0) {
			try {
				Thread.sleep(1000);
			}
			catch (InterruptedException e) {
			}
			seat = seatService.getSeat(roomCode, seat.getIndex());
			player = playerService.getPlayerByUserId(seat.getUserId());
			flag = null != player && player.getStatus() == PlayerStatusEnum.WAIT.getCode() && player.getUserId().equals(userEvent.getUserId());
			curCount--;
		}
		// 检查玩家状态 如果还是占位状态 则踢掉所占位置
		// 有可能玩家在一局游戏开始的时候已被踢掉
		// 不可踢掉后来坐上的玩家
		if (curCount == -1 && flag) {
			seat.kickPlayerDto();
		}
	}

}
