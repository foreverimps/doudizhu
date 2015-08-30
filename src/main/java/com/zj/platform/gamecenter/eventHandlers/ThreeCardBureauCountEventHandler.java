package com.zj.platform.gamecenter.eventHandlers;

import com.zj.platform.gamecenter.constant.EventTypeEnum.ThreeCardsEventTypeEnum;
import com.zj.platform.gamecenter.events.UserEvent;

/**
 * 玩家在一个房间中连续玩的局数. <br>
 * 不使用
 * <p>
 * Copyright: Copyright (c) 2015年2月2日 下午11:43:04
 * <p>
 * Company:
 * <p>
 * 
 * @author
 * @version 1.0.0
 */
public class ThreeCardBureauCountEventHandler extends SystemHandler {

	@Override
	public int eventCode() {
		return ThreeCardsEventTypeEnum.BUREAU_COUNT.getCode();
	}

	@Override
	public void handleEvent(UserEvent userEvent) {
		int count = userEvent.getBureauCount();
		// 奖励策略
		switch (count) {
			case 500:
				break;
			case 100:
				break;
			case 50:
				break;
			case 20:
				break;
			case 10:
				break;
		}
	}

}
