/**
 * (C) Copyright DIGITAL CHINA INFOMATION SERVICE COMPANYU LTD. Corporation All
 * Rights Reserved. System : smarthc FileName : MProducer.java
 */
package com.zj.platform.gamecenter.jms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.alibaba.fastjson.JSON;
import com.zj.platform.gamecenter.dto.socket.ActionDto;
import com.zj.platform.gamecenter.entity.Player;
import com.zj.platform.gamecenter.entity.SocketTask;
import com.zj.platform.gamecenter.entity.User;
import com.zj.platform.gamecenter.service.SocketTaskService;
import com.zj.platform.gamecenter.session.SessionManager;
import com.zj.platform.gamecenter.session.SocketSession;

public class RemoteMessageSender implements MessageListener {

	@Autowired
	private JmsTemplate jms;

	@Autowired
	private SocketTaskService socketTaskService;

	@Autowired
	private SessionManager sessionManager;

	@Resource(name = "remoteMessageDestination")
	private Destination destination;

	public void sendToPlayerDtos(ActionDto actionDto, List<Player> players) {
		List<Long> tagetUsrIds = new LinkedList<Long>();
		for (Player dto : players) {
			tagetUsrIds.add(dto.getUserId());
		}
		sendToUserIds(actionDto, tagetUsrIds);
	}

	public void sendToUsers(ActionDto actionDto, List<User> users) {
		List<Long> tagetUsrIds = new LinkedList<Long>();
		for (User user : users) {
			tagetUsrIds.add(user.getId());
		}
		sendToUserIds(actionDto, tagetUsrIds);
	}

	public void sendToUserIds(ActionDto actionDto, final List<Long> tagetUsrIds) {
		List<SocketTask> tasks = new ArrayList<SocketTask>(tagetUsrIds.size());
		for (Long targetUserId : tagetUsrIds) {
			SocketTask socketTask = new SocketTask();
			socketTask.setUserId(targetUserId);
			socketTask.setCommonId(actionDto.getCommandId());
			socketTask.setContent(JSON.toJSONString(actionDto));
			tasks.add(socketTask);
		}
		// 可加入保存功能
		socketTaskService.insertTasks(tasks);
		jms.send(destination, new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(JSON.toJSONString(tagetUsrIds));
			}
		});
	}

	@Override
	public void onMessage(Message message) {
		// 接收到消息后对本机上的用户进行发送
		TextMessage textMessage = (TextMessage) message;
		try {
			String userIdsString = textMessage.getText();
			List<Long> userIds = JSON.parseArray(userIdsString, Long.class);
			Iterator<Long> it = userIds.iterator();
			List<Long> existUserIds = new ArrayList<Long>(sessionManager.getUserIds());
			List<Long> filter = new ArrayList<Long>();
			while (it.hasNext()) {
				// 去掉Session不在本地的用户Id
				Long userId = it.next();
				if (existUserIds.contains(userId)) {
					filter.add(userId);
				}
			}
			// 查询数据库中的未发送消息
			List<SocketTask> tasks = socketTaskService.findWaitProcessByUserIds(filter);
			Map<Long, SocketSession> sessions = sessionManager.getSessionMap();
			for (SocketTask task : tasks) {
				// 向用户发送
				boolean lockNum = socketTaskService.lockSocketTask(task.getId());// 锁定这条记录，防止其他的线程来抢此线程
				if (lockNum) { // 加锁成功
					try {
						SocketSession session = sessions.get(task.getUserId());
						if (null == session || session.isClose()) {
							// session已关闭
							socketTaskService.unlockSocketTask(task.getId()); // 解锁
						} else {
							// 发送消息
							session.sendMessage(task.getContent());
							socketTaskService.deleteById(task.getId());// 成功发送之后，就删除（物理删除）此socket_task，减少数据源
						}
					}
					catch (Exception e) {
						socketTaskService.unlockSocketTask(task.getId()); // 解锁
						e.printStackTrace();
					}
				}
			}
		}
		catch (JMSException e) {
			e.printStackTrace();
		}
	}

	/**
	 * main函数.
	 * 
	 * @param args
	 *            启动参数
	 * @throws Exception
	 *             Exception
	 */
	public static void main(String... args) throws Exception {
		LinkedList<Long> list = new LinkedList<Long>();
		list.add(1l);
		list.add(2l);
		list.add(3l);
		list.add(4l);
		list.add(5l);
		list.add(6l);
		list.add(7l);
		list.add(8l);
		System.out.println(list);
		for (Long l : list) {
			System.out.println(l);
			if (l.equals(3L)) {
				list.add(9l);
				System.out.println(list);
			}
		}
	}

}
