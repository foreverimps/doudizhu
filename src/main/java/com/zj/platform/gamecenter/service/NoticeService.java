package com.zj.platform.gamecenter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zj.platform.gamecenter.dao.NoticeDao;
import com.zj.platform.gamecenter.entity.Notice;

@Service
public class NoticeService {

	@Autowired
	private NoticeDao noticeDao;

	public List<Notice> queryAllNotices() {
		return noticeDao.queryAll();
	}

}
