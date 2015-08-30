package com.zj.platform.gamecenter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.zj.platform.gamecenter.entity.Room;
import com.zj.platform.gamecenter.service.RoomService;

public class GameInit implements InitializingBean {

	@Autowired
	private RoomService roomService;

	@Override
	public void afterPropertiesSet() throws Exception {
		List<Room> rooms = new ArrayList<Room>();
		roomService.createRooms(rooms);
	}
}
