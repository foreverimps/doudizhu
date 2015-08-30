package com.zj.platform.gamecenter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zj.platform.gamecenter.dao.FeedbackDao;
import com.zj.platform.gamecenter.entity.Feedback;

@Service
public class FeedbackService {

	@Autowired
	private FeedbackDao feedbackDao;

	public void insert(Feedback feedback) {
		feedbackDao.insert(feedback);
	}
}
