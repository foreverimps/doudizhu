package com.zj.platform.gamecenter.handler;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.zj.platform.gamecenter.constant.ResultEnum;
import com.zj.platform.gamecenter.constant.SocketConstant;
import com.zj.platform.gamecenter.dto.SocketInMessage;
import com.zj.platform.gamecenter.dto.SocketOutMessage;
import com.zj.platform.gamecenter.dto.socket.UserDto;
import com.zj.platform.gamecenter.entity.User;
import com.zj.platform.gamecenter.service.MongoCacheService;
import com.zj.platform.gamecenter.service.PlayerService;
import com.zj.platform.gamecenter.service.SocketTaskService;
import com.zj.platform.gamecenter.service.socket.HandlerService;
import com.zj.platform.gamecenter.service.socket.SocketService;
import com.zj.platform.gamecenter.session.SessionManager;
import com.zj.platform.gamecenter.utils.BuildResultUtil;

/**
 * Echo messages by implementing a Spring {@link WebGameSocketHandler}
 * abstraction.
 */
@Component
public class WebGameSocketHandler extends TextWebSocketHandler {

	// TODO session的统一管理
	private static final Logger logger = LoggerFactory.getLogger(WebGameSocketHandler.class);

	@Autowired
	private SocketService socketService;

	@Autowired
	private HandlerService handlerService;

	@Autowired
	private SessionManager sessionManager;

	@Autowired
	private MongoCacheService mongoCacheService;

	@Autowired
	private SocketTaskService socketTaskService;

	@Autowired
	private PlayerService playerService;

	class Task extends Thread {

		@Override
		public void run() {
			// while (true) {
			// try {
			// Set<String> set = user_session_map.keySet();
			// for (String userId : set) {
			// List<SocketTask> socketTaskList =
			// socketTaskService.findByUserId(Long.parseLong(userId));//
			// 获取此用户的socket_task列表
			// if (CollectionUtils.isNotEmpty(socketTaskList)) {
			// for (SocketTask socketTask : socketTaskList) {
			// boolean lockNum =
			// socketTaskService.lockSocketTask(socketTask.getId());//
			// 锁定这条记录，防止其他的线程来抢此线程
			// if (lockNum) { // 加锁成功
			// try {
			// user_session_map.get(userId).sendMessage(new
			// TextMessage(socketTask.getContent()));// 发送消息
			// socketTaskService.deleteById(socketTask.getId());//
			// 成功发送之后，就删除（物理删除）此socket_task，减少数据源
			// }
			// catch (IOException e) {
			// socketTaskService.unlockSocketTask(socketTask.getId()); // 解锁
			// e.printStackTrace();
			// }
			// }
			// }
			//
			// }
			// }
			//
			// TimeUnit.MILLISECONDS.sleep(100);
			// }
			// catch (InterruptedException e) {
			// e.printStackTrace();
			// }
			// }
		}
	}

	/**
	 * 项目启动，开启任务线程，此线程是个死循环，主要来扫描socket_task的表数据
	 */
	@PostConstruct
	public void init() {
		logger.info("WebGameSocketHandler.....................init");
		// Task task = new Task();
		// task.start();
	}

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		logger.info("message.getPayload().toString() = " + message.getPayload().toString());
		SocketInMessage<String> inMessage = JSON.parseObject(message.getPayload().toString(), new TypeReference<SocketInMessage<String>>() {
		});
		SocketOutMessage outMsg = new SocketOutMessage();
		// if (!handlerService.hasHands(session.getId(), session_map.keySet()))
		// { // 是否握手
		if (!handlerService.hasHands(session.getId(), sessionManager.getSessionIds())) { // 是否握手
			if (inMessage.getId().intValue() == SocketConstant.HAND_SHAKE.getCode()) { // 握手
				logger.info("握手成功。。。。。。。。。。");
				// addSession(session);// 握手进来
				return;
			} else { // 直接断开
				logger.info("必须先握手.session close.");
				session.close();
				return;
			}
		} else { // 已握手
			// 处理具体的业务
			outMsg = process_web(inMessage.getId(), inMessage.getContent(), inMessage.getToken(), session);
		}
		outMsg.setId(inMessage.getId());
		session.sendMessage(new TextMessage(JSON.toJSONString(outMsg)));
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		logger.info("client is conncted...............");
		JSONObject json = new JSONObject();
		json.put("id", 1);
		json.put("status", 1);
		session.sendMessage(new TextMessage(json.toJSONString()));
	}

	/** 处理web客户端信息 */
	public SocketOutMessage process_web(Integer id, String content, String token, WebSocketSession session) {
		if (SocketConstant.LOGIN.getCode() == id) {
			User user = socketService.login(content);
			if (null == user) {
				return BuildResultUtil.buildOutResult(ResultEnum.ERROR, "账号或密码错误");
			}
			// insertUserSession(String.valueOf(user.getId()), session);
			// TODO 调整加入Session位置
			insertUserSession(user.getId(), session);
			UserDto dto = new UserDto();
			BeanUtils.copyProperties(user, dto);
			return BuildResultUtil.buildOutResult(ResultEnum.SUCCESS, dto);
		}
		if (SocketConstant.REG.getCode() == id) {// 注册
			User user = socketService.reg(content);
			if (null == user) {
				return BuildResultUtil.buildOutResult(ResultEnum.ERROR, "注册失败");
			}
			// insertUserSession(String.valueOf(user.getId()), session);
			// insertUserSession(user.getId(), session);
			return BuildResultUtil.buildOutResult(ResultEnum.SUCCESS, user);
		}
		if (SocketConstant.GAME_LIST.getCode() == id) {// 获取游戏列表
			return BuildResultUtil.buildOutResult(ResultEnum.SUCCESS, socketService.gameList(content));
		}
		// if (SocketConstant.CREATE_GAME.getCode() == id) {// 创建游戏
		// return BuildResultUtil.buildOutResult(ResultEnum.SUCCESS,
		// socketService.createGame(content, token));
		// }
		if (SocketConstant.QUICK_START.getCode() == id) {// 快速开始
			return socketService.quickStart(content, token);
		}
		if (SocketConstant.PLAY_GAME.getCode() == id) {// 玩游戏，游戏中的每个操作都通过此接口来处理
			return socketService.playGame(content, token);
		}
		if (SocketConstant.TASK.getCode() == id) {// 获取任务列表
			return socketService.getTask(Integer.parseInt(content));
		}
		if (SocketConstant.SEND_MSG.getCode() == id) {// 发送消息
			return socketService.sendMsg(content);
		}
		if (SocketConstant.GET_PRODUCTS.getCode() == id) {// 获取商品列表
			return socketService.productList(content);
		}
		if (SocketConstant.FRIENDS.getCode() == id) {// 获取好友列表
			return socketService.getFriends(Long.parseLong(content));
		}
		if (SocketConstant.FEEDBACK.getCode() == id) {// 获取公告列表
			return socketService.feedback(content, token);
		}
		return null;
	}

	/**
	 * 添加web socket session
	 * 
	 * @param session
	 * @param session_map
	 */
	// public void addSession(WebSocketSession session) {
	// session_map.put(session.getId(), session);
	// }

	/**
	 * 添加 用户id，client session
	 * 
	 * @param userId
	 * @param session
	 */
	public void insertUserSession(Long userId, WebSocketSession session) {
		// if (user_session_map.containsKey(userId)) {
		// String sessionId = user_session_map.get(userId).getId();
		// user_session_map.remove(userId);// 删除原来的
		// session_map.remove(sessionId);
		// }
		// user_session_map.put(userId, session);
		sessionManager.addSession(userId, session);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		super.afterConnectionClosed(session, status);
		long userId = sessionManager.removeSession(session);
		playerService.removePlayer(userId);
	}

}
