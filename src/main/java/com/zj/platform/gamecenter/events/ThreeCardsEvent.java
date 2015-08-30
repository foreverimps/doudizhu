package com.zj.platform.gamecenter.events;

import com.zj.platform.gamecenter.constant.EventTypeEnum;

/**
 * 金花事件 <br>
 * <p>
 * Copyright: Copyright (c) 2014年12月29日 下午9:02:19
 * <p>
 * Company:
 * <p>
 * 
 * @author H.L.Wan
 * @version 1.0.0
 */
public class ThreeCardsEvent extends UserEvent {

	private EventTypeEnum.ThreeCardsEventTypeEnum eventType;

	public ThreeCardsEvent(Long userId, EventTypeEnum.ThreeCardsEventTypeEnum eventType) {
		super(userId);
		this.eventType = eventType;
	}

	@Override
	public int getEventCode() {
		if (null != eventType) {
			return eventType.getCode();
		}
		return -1;
	}

	public void setEventType(EventTypeEnum.ThreeCardsEventTypeEnum eventType) {
		this.eventType = eventType;
	}

}
