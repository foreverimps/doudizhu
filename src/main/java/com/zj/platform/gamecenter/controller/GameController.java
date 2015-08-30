package com.zj.platform.gamecenter.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zj.platform.gamecenter.entity.Notice;
import com.zj.platform.gamecenter.service.NoticeService;

@RequestMapping("/game")
@Controller
public class GameController {

	private static final Logger logger = LoggerFactory.getLogger(GameController.class);

	@Autowired
	private NoticeService noticeService;

	/**
	 * 获取公告，返回页面
	 */
	@RequestMapping(value = "/getNotice")
	public String getNotice(HttpServletRequest request, Model model) {
		List<Notice> noticelList = noticeService.queryAllNotices();
		model.addAttribute("noticelList", noticelList);
		return "notice_list";
	}
}
