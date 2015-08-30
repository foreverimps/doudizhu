package com.zj.platform.gamecenter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zj.platform.gamecenter.service.AiService;
import com.zj.platform.gamecenter.service.socket.SocketService;

@RequestMapping("/ai")
@Controller
public class AiController {

	@Autowired
	private AiService aiService;

	@Autowired
	private SocketService socketService;

	@RequestMapping(value = "/testInsert")
	public void initAi() {
		// Room room = new Room();
		// room.setBottomNote(20);
		// room.setBureau(null);
		// room.setCode("123" + Math.random());
		// room.setMaxNote(500);
		// room.setPassword("password");
		// room.setPlayerList(null);
		// room.setSiteTime(10);
		// room.setType(1);
		aiService.initAi();
	}

	@RequestMapping(value = "/testFindAll")
	public void startGame() {
		aiService.testFindAll();
	}

	@RequestMapping(value = "/findByCodeAndType")
	public void findByCodeAndType(String code, int type) {
		aiService.findByCodeAndType(code, type);
	}

	@RequestMapping(value = "/foreverimps")
	public String index() {
		return "index";
	}

}
