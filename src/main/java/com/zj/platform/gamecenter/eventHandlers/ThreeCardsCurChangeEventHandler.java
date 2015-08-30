package com.zj.platform.gamecenter.eventHandlers;

import org.springframework.beans.factory.annotation.Autowired;

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
 * 当前玩家切换处理<br>
 * <p>
 * Copyright: Copyright (c) 2015年1月6日 下午11:01:47
 * <p>
 * Company:
 * <p>
 * 
 * @author H.L.Wan
 * @version 1.0.0
 */
public class ThreeCardsCurChangeEventHandler extends SystemHandler {

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

	public static final Integer ACTION_COUNT_DOWN = 25;

	@Override
	public int eventCode() {
		return ThreeCardsEventTypeEnum.NOTES_NEXT.getCode();
	}

	@Override
	public void handleEvent(UserEvent userEvent) {
		String roomCode = userEvent.getRoomCode();
		Integer curRound = userEvent.getRound();
		if (curRound == 0) {
			// TODO 睡眠时间 与前台动画有关

		}
		int curIndex = userEvent.getCurIndex();
		Bureau curBureau = bureauService.getLastBureauDtoInRoom(roomCode);
		seatService.getSeat(roomCode, curIndex);
		// 防止并发的冲突做的检查
		// if (curSeat.getIsOut()) {
		// curBureau.changeCurSeat();
		// } else {
		ActionDto action = new ActionDto();
		action.setRoomCode(roomCode);
		action.setUserId(userEvent.getUserId());
		action.setCommandId(PlayGameTypeEnum.CUR_CHANGE.getCode());
		action.setContent(curIndex);
		Bureau roomBureau = curBureau;
		// 消息发送到前台
		messageSender.sendToUserIds(action, playerService.getUserIdsByRoomCode(roomCode));
		// 开始倒计时
		boolean flag = true;
		int curCount = ACTION_COUNT_DOWN;
		while (flag && curCount >= 0) {
			bureauService.updateBureauCountDown(curBureau.getId(), curCount);
			// 条件检查 当前玩家是否为计时的玩家 当前局是否为计时开始的一局 当前轮数是否为开始的轮数
			try {
				Thread.sleep(1000);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			curBureau = bureauService.getLastBureauDtoInRoom(roomCode);
			flag = (!curBureau.getIsEnd()) && curBureau.getCurIndex() == curIndex && curRound.equals(curBureau.getRound())
			        && curBureau.getId().equals(roomBureau.getId());
			curCount--;
			// }
			// 如果超时，则当前玩家弃牌
			if (curCount == -1 && flag) {
				Seat seat = seatService.getSeat(roomCode, curIndex);
				seat.giveUp();
			}
		}

	}
}
