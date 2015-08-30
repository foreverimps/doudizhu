package com.zj.platform.gamecenter.handler;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zj.platform.gamecenter.constant.ResultEnum;
import com.zj.platform.gamecenter.constant.SocketConstant;
import com.zj.platform.gamecenter.dto.SocketInMessage;
import com.zj.platform.gamecenter.dto.SocketOutMessage;
import com.zj.platform.gamecenter.entity.User;
import com.zj.platform.gamecenter.service.MongoCacheService;
import com.zj.platform.gamecenter.service.PlayerService;
import com.zj.platform.gamecenter.service.SocketTaskService;
import com.zj.platform.gamecenter.service.socket.HandlerService;
import com.zj.platform.gamecenter.service.socket.SocketService;
import com.zj.platform.gamecenter.session.MinaSocketSessionWrap;
import com.zj.platform.gamecenter.session.SessionManager;
import com.zj.platform.gamecenter.utils.BuildResultUtil;

public class SocketHandler extends IoHandlerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(SocketHandler.class);

	@Autowired
	private SocketService socketService;

	@Autowired
	private HandlerService handlerService;

	@Autowired
	private SocketTaskService socketTaskService;

	@Autowired
	private SessionManager sessionManager;

	@Autowired
	private MongoCacheService mongoCacheService;

	@Autowired
	private PlayerService playerService;

	@PostConstruct
	public void init() {
		logger.info("SocketHandler.....................init");
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		super.messageReceived(session, message);
		session.write("服务端接收到客户端的消息是：" + JSON.toJSONString(message));
		SocketInMessage<String> inMessage = JSON.parseObject((String) message, new TypeReference<SocketInMessage<String>>() {
		});
		SocketOutMessage outMsg = new SocketOutMessage();
		if (!handlerService.hasHands(MinaSocketSessionWrap.ID_PREFIX + String.valueOf(session.getId()), sessionManager.getSessionIds())) { // 是否握手
			if (inMessage.getId().intValue() == SocketConstant.HAND_SHAKE.getCode()) { // 握手
				logger.info("握手成功。。。。。。。。。。");
				// addSession(session);// 握手进来
				// sessionM
				return;
			} else { // 直接断开
				logger.warn("必须先握手.session close.");
				session.close(true);
				return;
			}
		} else { // 已握手
			// 处理具体的业务
			outMsg = process_client(inMessage.getId(), inMessage.getContent(), inMessage.getToken(), session);
		}
		outMsg.setId(inMessage.getId());
		session.write(JSON.toJSONString(outMsg));
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		super.exceptionCaught(session, cause);
		if (cause instanceof IOException) {
			deleteSession(session.getId());
		}
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		super.sessionClosed(session);
		Long userId = sessionManager.removeSession(session);
		playerService.removePlayer(userId);
	}

	/** 处理client客户端信息 */
	public SocketOutMessage process_client(Integer id, String content, String token, IoSession session) {
		if (SocketConstant.LOGIN.getCode() == id) {
			User user = socketService.login(content);
			if (null == user) {
				return BuildResultUtil.buildOutResult(ResultEnum.ERROR, "账号或密码错误");
			}
			insertUserSession(user.getId(), session);
			// insertUserSession(String.valueOf(user.getId()), session);
			// TODO 调整加入Session位置
			// 加入Session
			// 上线事件 -->好友上线通知
			// -->读取离线消息
			// TODO 上线异步通知 事件方式？
			return BuildResultUtil.buildOutResult(ResultEnum.SUCCESS, user);
		}
		if (SocketConstant.REG.getCode() == id) {
			User user = socketService.reg(content);
			if (null == user) {
				return BuildResultUtil.buildOutResult(ResultEnum.ERROR, "注册失败");
			}
			// insertUserSession(String.valueOf(user.getId()), session);
			// insertUserSession(user.getId(), session);
			return BuildResultUtil.buildOutResult(ResultEnum.SUCCESS, user);
		}
		if (SocketConstant.GAME_LIST.getCode() == id) {
			return BuildResultUtil.buildOutResult(ResultEnum.SUCCESS, socketService.gameList(content));
		}
		// if (SocketConstant.CREATE_GAME.getCode() == id) {
		// return BuildResultUtil.buildOutResult(ResultEnum.SUCCESS,
		// socketService.createGame(content, token));
		// }
		if (SocketConstant.QUICK_START.getCode() == id) {
			return socketService.quickStart(content, token);
		}
		if (SocketConstant.PLAY_GAME.getCode() == id) {
			return socketService.playGame(content, token);
		}
		if (SocketConstant.TASK.getCode() == id) {
			return socketService.getTask(Integer.parseInt(content));
		}
		if (SocketConstant.SEND_MSG.getCode() == id) {
			return socketService.sendMsg(content);
		}
		if (SocketConstant.GET_PRODUCTS.getCode() == id) {
			return socketService.productList(content);
		}
		if (SocketConstant.FRIENDS.getCode() == id) {
			return socketService.getFriends(Long.parseLong(content));
		}
		if (SocketConstant.FEEDBACK.getCode() == id) {
			return socketService.feedback(content, token);
		}
		if (SocketConstant.FRIENDS_ADD_ASK.getCode() == id) {
			return socketService.askFriends(content, token);
		}
		if (SocketConstant.FRIENDS_ADD_AGREE.getCode() == id) {
			return socketService.agreeFriends(content, token);
		}
		if (SocketConstant.FRIENDS_RENAME.getCode() == id) {
			return socketService.renameFriend(content, token);
		}
		if (SocketConstant.FRIENDS_RELEIVE.getCode() == id) {
			return socketService.relieveFriend(content, token);
		}
		return null;
	}

	/**
	 * 添加 用户id，client session
	 * 
	 * @param userId
	 * @param session
	 */
	public void insertUserSession(Long userId, IoSession session) {
		sessionManager.addSession(userId, session);
	}

	/**
	 * 删除session
	 * 
	 * @param session
	 */
	public void deleteSession(long sessionId) {
		sessionManager.removeSessionBySessionId(String.valueOf(sessionId));
	}

}
