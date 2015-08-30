package com.zj.platform.gamecenter.eventHandlers;

import com.zj.platform.gamecenter.events.UserEvent;

public interface EventHandler {

	int eventCode();

	void handleEvent(UserEvent userEvent);

}
