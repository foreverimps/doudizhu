package com.zj.platform.gamecenter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zj.platform.gamecenter.dao.TaskDao;
import com.zj.platform.gamecenter.entity.Task;

@Service
public class TaskService {

	@Autowired
	private TaskDao taskDao;

	public List<Task> queryByType(int type) {
		return null;
	}

}
