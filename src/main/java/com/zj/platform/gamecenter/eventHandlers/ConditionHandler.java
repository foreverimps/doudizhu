package com.zj.platform.gamecenter.eventHandlers;

import com.zj.platform.gamecenter.events.UserEvent;

/**
 * 条件性事件处理器 <br>
 * 在某些条件下处理事件，比如时间限定等，或者牌局情况，非永远有效
 * <p>
 * Copyright: Copyright (c) 2014年12月29日 下午9:27:35
 * <p>
 * Company:
 * <p>
 * 
 * @author
 * @version 1.0.0
 */
public abstract class ConditionHandler extends BaseEventHandler {

	@Override
	public final void handleEvent(UserEvent userEvent) {
		if (condition(userEvent, userEvent.getUserId())) {
			handleAfterCondition(userEvent);
		}
	}

	/**
	 * 用于判断是否执行对事件的处理
	 * 
	 * @param userEvent
	 * @param userId
	 * @param context
	 * @return true 执行 false则不执行
	 */
	public abstract boolean condition(UserEvent userEvent, Long userId);

	/**
	 * 对事件的具体业务处理
	 * 
	 * @param userEvent
	 */
	public abstract void handleAfterCondition(UserEvent userEvent);

}
