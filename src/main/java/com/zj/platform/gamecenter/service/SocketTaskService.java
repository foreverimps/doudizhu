package com.zj.platform.gamecenter.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zj.platform.gamecenter.constant.SocketTaskStatusEnum;
import com.zj.platform.gamecenter.entity.SocketTask;
import com.zj.platform.gamecenter.mongoDao.SocketTaskDao;

@Service
public class SocketTaskService {

	@Autowired
	private SocketTaskDao socketTaskDao;

	/**
	 * 批量保存任务
	 * 
	 * @param tasks
	 */
	public void insertTasks(Collection<SocketTask> tasks) {
		socketTaskDao.insertTasks(tasks);
	}

	/**
	 * 找出未处理的用户消息
	 * 
	 * @param userIds
	 * @return
	 */
	public List<SocketTask> findWaitProcessByUserIds(List<Long> userIds) {
		return socketTaskDao.findWaitProcessByUserIds(userIds, SocketTaskStatusEnum.WAIT_PROCESS.getCode());
	}

	/**
	 * 找出单个用户未处理的消息
	 * 
	 * @param userId
	 * @return
	 */
	public List<SocketTask> findWaitProcessByUserId(Long userId) {
		return socketTaskDao.findByUserIdAndStatus(userId, SocketTaskStatusEnum.WAIT_PROCESS.getCode());
	}

	public boolean lockSocketTask(String id) {
		return 1 == socketTaskDao.updateStatusById(id, SocketTaskStatusEnum.WAIT_PROCESS.getCode(), SocketTaskStatusEnum.LOCK.getCode());
	}

	public boolean unlockSocketTask(String id) {
		return 1 == socketTaskDao.updateStatusById(id, SocketTaskStatusEnum.LOCK.getCode(), SocketTaskStatusEnum.WAIT_PROCESS.getCode());
	}

	public int deleteById(String id) {
		return socketTaskDao.deleteById(id);
	}

	public void insert(SocketTask task) {
		socketTaskDao.insert(task);
	}

	public List<SocketTask> findByUserId(Long userId) {
		return socketTaskDao.findByUserId(userId);
	}

}
