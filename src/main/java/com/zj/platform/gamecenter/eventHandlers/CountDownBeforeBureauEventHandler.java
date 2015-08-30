package com.zj.platform.gamecenter.eventHandlers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zj.platform.gamecenter.constant.EventTypeEnum;
import com.zj.platform.gamecenter.constant.PlayGameTypeEnum;
import com.zj.platform.gamecenter.dto.socket.ActionDto;
import com.zj.platform.gamecenter.entity.Bureau;
import com.zj.platform.gamecenter.entity.Room;
import com.zj.platform.gamecenter.events.UserEvent;
import com.zj.platform.gamecenter.jms.RemoteMessageSender;
import com.zj.platform.gamecenter.service.BureauService;
import com.zj.platform.gamecenter.service.MongoCacheService;
import com.zj.platform.gamecenter.service.PlayerService;
import com.zj.platform.gamecenter.service.RoomService;

/**
 * 开局前倒计时的处理. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2015年1月4日 下午11:15:58
 * <p>
 * Company:
 * <p>
 * 
 * @author H.L.Wan
 * @version 1.0.0
 */
@Component
public class CountDownBeforeBureauEventHandler extends SystemHandler {

	/** 开局前倒计时10秒 */
	private static final int COUNT_DOWN = 10;

	@Autowired
	private RemoteMessageSender messageSender;

	@Autowired
	private RoomService roomService;

	@Autowired
	private MongoCacheService cacheService;

	@Autowired
	private BureauService bureauService;

	@Autowired
	private PlayerService playerService;

	@Override
	public int eventCode() {
		return EventTypeEnum.ThreeCardsEventTypeEnum.COUNT_DOWN_BEFORE_BUREAU.getCode();
	}

	@Override
	public void handleEvent(UserEvent userEvent) {
		// 通知前台开始倒计时
		ActionDto action = new ActionDto();
		action.setUserId(null);
		action.setCommandId(PlayGameTypeEnum.COUNT_DOWN_BEFORE_BUREAU.getCode());
		action.setContent(COUNT_DOWN);
		String roomCode = userEvent.getRoomCode();
		action.setRoomCode(roomCode);
		List<Long> userIds = playerService.getUserIdsByRoomCode(roomCode);
		messageSender.sendToUserIds(action, userIds);
		// 该倒计时为RoomDto的一个属性，如果玩家在中途加入，需要显示当前倒计时间
		int curCount = COUNT_DOWN;
		Bureau bureauDto = bureauService.getLastBureauDtoInRoom(roomCode);
		boolean flag = null == bureauDto || bureauDto.getIsEnd();
		while (curCount >= 0 && flag) {
			roomService.updateRoomCountDown(roomCode, curCount);
			try {
				Thread.sleep(1000);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			bureauDto = bureauService.getLastBureauDtoInRoom(roomCode);
			flag = null == bureauDto || bureauDto.getIsEnd();
			curCount--;

		}
		// 倒计时结束时创建一局游戏，具体是否满足创建条件在创建方法中判断
		if (curCount == -1 && flag) {
			Room room = roomService.getRoomDtoByCode(roomCode);
			room.createBureauDto(bureauDto);
		}
	}
}
