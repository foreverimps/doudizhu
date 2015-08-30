package com.zj.platform.gamecenter.service.socket;

import java.util.List;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.zj.platform.gamecenter.constant.FeedbackStatusEnum;
import com.zj.platform.gamecenter.constant.ResultEnum;
import com.zj.platform.gamecenter.constant.SocketConstant;
import com.zj.platform.gamecenter.constant.SocketTaskStatusEnum;
import com.zj.platform.gamecenter.dto.LoginToken;
import com.zj.platform.gamecenter.dto.SocketOutMessage;
import com.zj.platform.gamecenter.dto.socket.AccountDto;
import com.zj.platform.gamecenter.dto.socket.FriendsDto;
import com.zj.platform.gamecenter.dto.socket.MessageDto;
import com.zj.platform.gamecenter.dto.socket.PlayGameDto;
import com.zj.platform.gamecenter.dto.socket.ProductDto;
import com.zj.platform.gamecenter.dto.socket.RankingDto;
import com.zj.platform.gamecenter.dto.socket.RoomDto;
import com.zj.platform.gamecenter.entity.Feedback;
import com.zj.platform.gamecenter.entity.Friends;
import com.zj.platform.gamecenter.entity.Player;
import com.zj.platform.gamecenter.entity.Product;
import com.zj.platform.gamecenter.entity.Ranking;
import com.zj.platform.gamecenter.entity.Room;
import com.zj.platform.gamecenter.entity.SocketTask;
import com.zj.platform.gamecenter.entity.Task;
import com.zj.platform.gamecenter.entity.User;
import com.zj.platform.gamecenter.service.FeedbackService;
import com.zj.platform.gamecenter.service.FriendsService;
import com.zj.platform.gamecenter.service.MessageService;
import com.zj.platform.gamecenter.service.MongoCacheService;
import com.zj.platform.gamecenter.service.NoticeService;
import com.zj.platform.gamecenter.service.PlayerService;
import com.zj.platform.gamecenter.service.ProductService;
import com.zj.platform.gamecenter.service.RankingService;
import com.zj.platform.gamecenter.service.RoomService;
import com.zj.platform.gamecenter.service.SocketTaskService;
import com.zj.platform.gamecenter.service.TaskService;
import com.zj.platform.gamecenter.service.UserService;
import com.zj.platform.gamecenter.service.cache.CacheService;
import com.zj.platform.gamecenter.utils.BuildResultUtil;

@Service
public class SocketService {

	@Autowired
	private UserService userService;

	@Autowired
	private RoomService roomService;

	@Autowired
	private PlayerService playerService;

	@Autowired
	private RankingService rankingService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private NoticeService noticeService;

	@Autowired
	private ProductService productService;

	@Autowired
	private CacheService cacheService;

	@Autowired
	private MongoCacheService mongoCacheService;

	@Autowired
	private SocketTaskService socketTaskService;

	@Autowired
	private FriendsService friendsService;

	private FeedbackService feedbackService;

	/**
	 * 开线程取用户其他信息，公告，消息，商品，游戏列表
	 */
	public void afterOfLoginOrReg() {

		// GetUserInfo getUserInfo = new GetUserInfo();
		// getUserInfo.start();
	}

	public SocketOutMessage getTask(int type) {
		List<Task> tasks = cacheService.getTasks(type);
		return BuildResultUtil.buildOutResult(ResultEnum.SUCCESS, tasks);
	}

	public User login(String content) {
		AccountDto accountDto = JSON.parseObject(content, AccountDto.class);

		User user = userService.login(accountDto.getAccount(), accountDto.getPassword());
		if (null == user) {
			return user;
		}
		Player playerDto = playerService.getPlayerByUserId(user.getId());
		if (null != playerDto) {
			return null;
		}
		// 加载Player信息到MongoDB中
		playerService.savePlayer(new Player(user));
		afterOfLoginOrReg();
		String token = cacheService.genToken(user);
		user.setToken(token);
		return user;
	}

	public User reg(String content) {
		AccountDto accountDto = JSON.parseObject(content, AccountDto.class);

		User user = userService.register(accountDto);
		afterOfLoginOrReg();
		String token = cacheService.genToken(user);
		user.setToken(token);
		return user;
	}

	public List<Room> gameList(String content) {
		JSON.parseObject(content, RoomDto.class);
		// return roomService.getByType(roomDto.getType(), roomDto.getPageNo(),
		// roomDto.getPageSize());
		return null;
	}

	public SocketOutMessage quickStart(String content, String token) {
		LoginToken loginToken = cacheService.getLoginToken(token);
		if (null == loginToken) {
			return BuildResultUtil.buildOutResult(ResultEnum.ERROR, "尚未登录");
		}
		return roomService.quickStart(content, loginToken.getUser());
	}

	// public RoomDto createGame(String content, String token) {
	// RoomDto roomDto = JSON.parseObject(content, RoomDto.class);
	// User user = cacheService.getLoginToken(token).getUser();
	// roomService.create(roomDto, user);
	// // roomDto.setCode(room.getCode());
	// return roomDto;
	// }

	public SocketOutMessage playGame(String content, String token) {
		User user = cacheService.getLoginToken(token).getUser();// 从缓存中获取用户
		PlayGameDto playGameDto = JSON.parseObject(content, PlayGameDto.class);// socket传过来的内容转化为PlayGameDto
		SocketOutMessage out = roomService.playGame(playGameDto, user);
		return out;
	}

	public SocketOutMessage rankingList(RankingDto rankingDto) {
		// 获取的数据不一定是最新的，可以直接从缓存中更新得到，更新缓存半个小时
		List<Ranking> rankings = cacheService.getRankings(rankingDto.getType());
		return BuildResultUtil.buildOutResult(ResultEnum.SUCCESS, rankings);
	}

	public SocketOutMessage getFriends(Long userId) {
		List<FriendsDto> friends = cacheService.getFriendsByUserId(userId);
		return BuildResultUtil.buildOutResult(ResultEnum.SUCCESS, friends);
	}

	public SocketOutMessage feedback(String content, String token) {
		User user = cacheService.getLoginToken(token).getUser();// 从缓存中获取用户
		MessageDto messageDto = JSON.parseObject(content, MessageDto.class);
		Feedback feedback = new Feedback();
		feedback.setContent(messageDto.getContent());
		feedback.setStatus(FeedbackStatusEnum.WAIT_PROCESS.getCode());
		feedback.setUserId(user.getId());
		feedbackService.insert(feedback);
		return BuildResultUtil.buildOutResult(ResultEnum.SUCCESS);
	}

	public SocketOutMessage productList(String content) {
		ProductDto productDto = JSON.parseObject(content, ProductDto.class);
		List<Product> products = productService.queryByType(productDto.getType());
		return BuildResultUtil.buildOutResult(ResultEnum.SUCCESS, products);
	}

	public SocketOutMessage sendMsg(String content) {
		MessageDto messageDto = JSON.parseObject(content, MessageDto.class);
		IoSession session = null;
		if (null == session) {
			// 插入数据库
			return BuildResultUtil.buildOutResult(ResultEnum.SUCCESS, "");
		}
		session.write(messageDto.getContent());
		return BuildResultUtil.buildOutResult(ResultEnum.SUCCESS, "");
	}

	public void insertSocketTask(String roomCode, Long userId, int type, String content) {
		SocketTask task = new SocketTask();
		task.setRoomCode(roomCode);
		task.setUserId(userId);
		task.setType(type);
		task.setContent(content);
		task.setStatus(1);
		socketTaskService.insert(task);
	}

	/**
	 * 请求添加好友
	 * 
	 * @param content
	 * @param token
	 * @return
	 */
	public SocketOutMessage askFriends(String content, String token) {
		LoginToken loginToken = cacheService.getLoginToken(token);
		if (null == loginToken) {
			return BuildResultUtil.buildOutResult(ResultEnum.ERROR);
		}
		User user = loginToken.getUser();// 从缓存中获取用户
		if (null == user) {
			return BuildResultUtil.buildOutResult(ResultEnum.ERROR);
		}
		FriendsDto friendsDto = JSON.parseObject(content, FriendsDto.class);
		Long targetId = friendsDto.getUserId();
		User friend = userService.getById(targetId);
		if (null == friend) {
			return BuildResultUtil.buildOutResult(ResultEnum.ERROR);
		}
		Friends friends = friendsService.getByUserIdAndFriendId(user.getId(), targetId);
		if (null == friends) {
			// 将请求方信息发送给被请求方
			friendsDto.setUserId(user.getId());
			friendsDto.setAccount(user.getAccount());
			friendsDto.setFriendName(user.getAccount());
			friendsDto.setIcon(user.getIcon());
			addSocketTask(targetId, SocketConstant.FRIENDS_ADD_ASK.getCode(), friendsDto);
			return BuildResultUtil.buildOutResult(ResultEnum.SUCCESS);
		} else {
			return BuildResultUtil.buildOutResult(ResultEnum.ERROR);
		}
	}

	/**
	 * 同意添加好友
	 * 
	 * @param content
	 * @param token
	 * @return
	 */
	public SocketOutMessage agreeFriends(String content, String token) {
		LoginToken loginToken = cacheService.getLoginToken(token);
		if (null == loginToken) {
			return BuildResultUtil.buildOutResult(ResultEnum.ERROR);
		}
		User user = loginToken.getUser();// 从缓存中获取用户
		if (null == user) {
			return BuildResultUtil.buildOutResult(ResultEnum.ERROR);
		}
		FriendsDto friends = JSON.parseObject(content, FriendsDto.class);
		Long targetId = friends.getUserId();
		User friend = userService.getById(targetId);
		if (null == friend) {
			return BuildResultUtil.buildOutResult(ResultEnum.ERROR);
		}
		if (friendsService.addFriend(user, friend)) {
			// 通知好友双方
			addSocketTask(user.getId(), SocketConstant.FRIENDS_ADD_AGREE.getCode(), friends);
			friends.setUserId(user.getId());
			friends.setAccount(user.getAccount());
			friends.setFriendName(user.getAccount());
			friends.setIcon(friend.getIcon());
			addSocketTask(targetId, SocketConstant.FRIENDS_ADD_AGREE.getCode(), friends);
			return BuildResultUtil.buildOutResult(ResultEnum.SUCCESS);
		}
		return BuildResultUtil.buildOutResult(ResultEnum.ERROR);
	}

	/**
	 * 解除好友
	 * 
	 * @param content
	 * @param token
	 * @return
	 */
	public SocketOutMessage relieveFriend(String content, String token) {
		LoginToken loginToken = cacheService.getLoginToken(token);
		if (null == loginToken) {
			return BuildResultUtil.buildOutResult(ResultEnum.ERROR);
		}
		User user = loginToken.getUser();// 从缓存中获取用户
		if (null == user) {
			return BuildResultUtil.buildOutResult(ResultEnum.ERROR);
		}
		FriendsDto friends = JSON.parseObject(content, FriendsDto.class);
		if (friendsService.relieveFriend(friends.getId(), user.getId())) {
			// 通知好友双方
			addSocketTask(user.getId(), SocketConstant.FRIENDS_RELEIVE.getCode(), friends);
			Long targetId = friends.getUserId();
			friends.setUserId(user.getId());
			addSocketTask(targetId, SocketConstant.FRIENDS_RELEIVE.getCode(), friends);
			return BuildResultUtil.buildOutResult(ResultEnum.SUCCESS);
		}
		return BuildResultUtil.buildOutResult(ResultEnum.ERROR);
	}

	/**
	 * 修改好友备注TODO
	 * 
	 * @param content
	 * @param token
	 * @return
	 */
	public SocketOutMessage renameFriend(String content, String token) {
		LoginToken loginToken = cacheService.getLoginToken(token);
		if (null == loginToken) {
			return BuildResultUtil.buildOutResult(ResultEnum.ERROR);
		}
		User user = loginToken.getUser();// 从缓存中获取用户
		if (null == user) {
			return BuildResultUtil.buildOutResult(ResultEnum.ERROR);
		}
		FriendsDto friends = JSON.parseObject(content, FriendsDto.class);
		if (friendsService.renameFriendName(friends.getId(), user.getId(), friends.getFriendName())) {
			addSocketTask(user.getId(), SocketConstant.FRIENDS_RENAME.getCode(), friends);
			return BuildResultUtil.buildOutResult(ResultEnum.SUCCESS);
		}
		return BuildResultUtil.buildOutResult(ResultEnum.ERROR);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void addSocketTask(Long userId, int commandId, Object content) {
		SocketTask task = new SocketTask();
		task.setUserId(userId);
		task.setCommonId(commandId);
		task.setContent(JSON.toJSONString(content));
		task.setStatus(SocketTaskStatusEnum.WAIT_PROCESS.getCode());
		socketTaskService.insert(task);
	}

}
