package com.zj.platform.gamecenter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.zj.platform.gamecenter.constant.PlayGameTypeEnum;
import com.zj.platform.gamecenter.dto.socket.ActionDto;
import com.zj.platform.gamecenter.dto.socket.PlayGameDto;
import com.zj.platform.gamecenter.entity.Bureau;
import com.zj.platform.gamecenter.entity.Player;
import com.zj.platform.gamecenter.entity.Room;
import com.zj.platform.gamecenter.entity.Seat;
import com.zj.platform.gamecenter.entity.SocketTask;
import com.zj.platform.gamecenter.entity.User;
import com.zj.platform.gamecenter.mongoDao.RoomDao;
import com.zj.platform.gamecenter.service.socket.SocketService;

@Service
public class AiService {

	private static final Logger logger = LoggerFactory.getLogger(AiService.class);

	public List<User> aiUserList = new ArrayList<User>();

	public List<Room> roomList = new ArrayList<Room>();

	@Autowired
	private SocketService socketService;

	@Autowired
	private RoomService roomService;

	@Autowired
	private UserService userService;

	@Autowired
	private SocketTaskService socketTaskService;

	@Autowired
	private RoomDao roomDao;

	@Autowired
	private MongoCacheService mongoCacheService;

	@Autowired
	private BureauService bureauService;

	@Autowired
	private PlayerService playerService;

	@Autowired
	private SeatService seatService;

	/**
	 * 初始化机器人。 根据房间个数，初始化机器人个数
	 */
	public void initAi() {
		List<Room> room_list = mongoCacheService.queryAllRoom();
		List<User> user_list = mongoCacheService.queryAllAiUser();
		int user_size = user_list.size();
		int j = 0;
		for (Room room : room_list) {
			int sitCount = room.getSitCount();
			for (int i = j; i < user_size; i++) {
				j = i;
				sitCount++;
				PlayGameDto playGameDto = new PlayGameDto();
				playGameDto.setRoomCode(room.getCode());
				playGameDto.setType(PlayGameTypeEnum.SIT.getCode());// 进入房间并坐下
				playGameDto.setUserId(user_list.get(i).getId());
				roomService.playGame(playGameDto, user_list.get(i));// 通知其他用户

				PlayGameDto _playGameDto = new PlayGameDto();
				_playGameDto.setRoomCode(room.getCode());
				_playGameDto.setType(PlayGameTypeEnum.READY.getCode());// 准备
				                                                       // TODO
				                                                       // 希望可以做到延迟几秒钟再准备
				_playGameDto.setUserId(user_list.get(i).getId());
				roomService.playGame(_playGameDto, user_list.get(i));// 通知其他用户
				if (sitCount > 4) {
					continue;
				}
			}

		}
		// 1.获取全部房间
		// 2.获取全部机器人
		// 3.进入房间，当房间人数大于4人的时候，停止进入
		// 4.获取socket_task，分析是否为当前操作者，或者说此房间的局还没结束

		readSocketTask(user_list);
	}

	/**
	 * 读取socket_task，并按照具体内容作出动作 按照每个房间的任务进行检索，不要根据用户来检索，可能会存在问题。
	 * 需要考虑，如果新增的房间怎么办？加个监听器？还是加个触发器，来触发生成线程
	 */
	private void readSocketTask(List<User> user_list) {
		// 1.根据房间数开线程
		// 2.线程里面做房间用户的处理，每次都获取最新的用户然后去查询数据
		for (User user : user_list) {
			List<SocketTask> socketTaskList = socketTaskService.findByUserId(user.getId());
			for (SocketTask socketTask : socketTaskList) {
				if (socketTaskService.lockSocketTask(socketTask.getId())) {// 对此socketTask进行加锁，防止其他服务器抢
					ActionDto actionDto = JSON.parseObject(socketTask.getContent(), ActionDto.class);
					int common_id = socketTask.getCommonId();
					// 判断此socketTask的下个操作者是否为自己
					// 如果为自己就进行操作响应
					if (common_id == PlayGameTypeEnum.CALL.getCode()) {
						actionDto.getUserId();
						Bureau bureau = bureauService.queryBureau("roomCode");
						bureau.getCurIndex();
						Player player = new Player();// 根据index获取Player
						Long _userId = player.getUserId();
						if (user.getId().longValue() == _userId.longValue()) {
							// 判断牌的大小，然后按照一定的概率进行跟注或者加注，或者弃牌。
						}
					}
					if (common_id == PlayGameTypeEnum.ADD_CALL.getCode()) {
						// 同上
					}
					if (common_id == PlayGameTypeEnum.COMPARE.getCode()) {
						// 判断是不是跟自己比，如果是跟自己比牌。如果输了，那就表示我已经不属于这局里面的了，如果赢了，那么判断下个操作是不是此用户，如果是，那么就同上。
					}
					if (common_id == PlayGameTypeEnum.CALL_OVER.getCode()) {// 如果有玩家是跟到底，那么就需要按照牌的大小来进行判断，什么时候可以跟此人开，或者其他，如果只有两个人，那么跟10把，然后开

					}
				}
			}
		}
	}

	public void testFindAll() {
		List<Room> list = roomDao.findAll();
		System.out.println(JSON.toJSON(list));
	}

	public void testInsert(Room room) {
		roomDao.insert(room);
	}

	public Room findByCodeAndType(String code, int type) {
		List<Room> roomList = roomDao.findByCodeAndType(code, type);
		System.out.println(JSON.toJSON(roomList));
		return null;
	}

	public void inRoom(int roomCount, int userCount) {// 机器人先进入房间
		for (int roomType = 1; roomType < 4; roomType++) {
			List<Room> roomList = roomDao.findByType(roomType);// 从内存里面获取房间
			Long moneyAccount = 100000L;
			List<User> aiUserList = new ArrayList<User>();
			if (roomType == 1) {
				moneyAccount = 100000L;
				aiUserList = userService.queryByMoneyAccount(0L, moneyAccount);// 获取相应金额的机器人
			} else if (roomType == 2) {
				moneyAccount = 500000L;
				aiUserList = userService.queryByMoneyAccount(100001L, moneyAccount);// 获取相应金额的机器人
			} else if (roomType == 3) {
				moneyAccount = 10000000L;
				aiUserList = userService.queryByMoneyAccount(500001L, moneyAccount);// 获取相应金额的机器人
			}

			for (User user : aiUserList) {
				if (CollectionUtils.isEmpty(roomList)) {
					break;
				}
				Room room = roomList.get(0);

				PlayGameDto playGameDto = new PlayGameDto();
				playGameDto.setRoomCode(room.getCode());
				playGameDto.setUserId(user.getId());
				playGameDto.setType(PlayGameTypeEnum.IN.getCode());
				roomService.playGame(playGameDto, user);

				roomDao.findByCode(room.getCode());
			}

		}

	}

	// /**
	// * 开线程
	// */
	// public void playGame(User user, SocketTask socketTask) throws Exception {
	// if (socketTask.getCommonId() == SocketConstant.PLAY_GAME.getCode()) {
	// PlayerDto dto = JSON.parseObject(socketTask.getContent(),
	// PlayerDto.class);
	// if (!roomService.isNextOptByUserId(socketTask.getRoomCode(),
	// user.getId())) {
	// return;
	// }
	// if (socketTask.getType() == PlayGameTypeEnum.IN.getCode()) {
	// PlayGameDto playGameDto = new PlayGameDto();
	// // playGameDto.setRoomCode(roomCode);
	// playGameDto.setUserId(user.getId());
	// playGameDto.setType(PlayGameTypeEnum.READY.getCode());
	// TimeUnit.MILLISECONDS.sleep(1000);// 秒数 随机 TODO
	// roomService.playGame(playGameDto, user);
	// // TODO 注册准备事件，多少秒准备是随机的
	// } else if (socketTask.getType() == PlayGameTypeEnum.READY.getCode()) {
	// // TODO 注册发言事件，多少秒之后如果还有人没准备就提醒发言，随机几个人发言
	// } else if (socketTask.getType() == PlayGameTypeEnum.SEND_CARDS.getCode())
	// {
	// if (roomService.isNextOptByUserId(socketTask.getRoomCode(),
	// user.getId())) {
	// PlayGameDto playGameDto = new PlayGameDto();
	// // playGameDto.setRoomCode(roomCode);
	// playGameDto.setUserId(user.getId());
	// int type = 0;// TODO 随机得到，根据牌的大小来判断这个操作类型
	// playGameDto.setType(PlayGameTypeEnum.CALL.getCode());
	// TimeUnit.MILLISECONDS.sleep(1000);// 秒数 随机 TODO
	// roomService.playGame(playGameDto, user);
	// }
	// } else if (socketTask.getType() == PlayGameTypeEnum.CALL.getCode()) {
	// if (roomService.isNextOptByUserId(socketTask.getRoomCode(),
	// user.getId())) {
	// PlayGameDto playGameDto = new PlayGameDto();
	// // playGameDto.setRoomCode(roomCode);
	// playGameDto.setUserId(user.getId());
	// int type = 0;// TODO
	// // 随机得到，根据牌的大小来判断这个操作类型，如果自己的牌很大，那么就直接加注。加到最大注
	// playGameDto.setType(PlayGameTypeEnum.CALL.getCode());
	// TimeUnit.MILLISECONDS.sleep(1000);// 秒数 随机 TODO
	// roomService.playGame(playGameDto, user);
	// }
	// } else if (socketTask.getType() == PlayGameTypeEnum.ADD_CALL.getCode()) {
	// if (roomService.isNextOptByUserId(socketTask.getRoomCode(),
	// user.getId())) {
	// PlayGameDto playGameDto = new PlayGameDto();
	// // playGameDto.setRoomCode(roomCode);
	// playGameDto.setUserId(user.getId());
	// int type = 0;// TODO 随机得到，根据牌的大小来判断这个操作类型
	// playGameDto.setType(PlayGameTypeEnum.CALL.getCode());
	// TimeUnit.MILLISECONDS.sleep(1000);// 秒数 随机 TODO
	// roomService.playGame(playGameDto, user);
	// }
	// } else if (socketTask.getType() == PlayGameTypeEnum.COMPARE.getCode()) {
	// if (roomService.isNextOptByUserId(socketTask.getRoomCode(),
	// user.getId())) {
	// PlayGameDto playGameDto = new PlayGameDto();
	// // playGameDto.setRoomCode(roomCode);
	// playGameDto.setUserId(user.getId());
	// int type = 0;// TODO 判断自己的金额，并且判断自己的
	// playGameDto.setType(PlayGameTypeEnum.CALL.getCode());
	// TimeUnit.MILLISECONDS.sleep(1000);// 秒数 随机 TODO
	// roomService.playGame(playGameDto, user);
	// }
	// } else if (socketTask.getType() == PlayGameTypeEnum.CALL_OVER.getCode())
	// {
	// if (roomService.isNextOptByUserId(socketTask.getRoomCode(),
	// user.getId())) {
	// PlayGameDto playGameDto = new PlayGameDto();
	// // playGameDto.setRoomCode(roomCode);
	// playGameDto.setUserId(user.getId());
	// int type = 0;// TODO 随机得到，根据牌的大小来判断这个操作类型
	// playGameDto.setType(PlayGameTypeEnum.CALL.getCode());
	// TimeUnit.MILLISECONDS.sleep(1000);// 秒数 随机 TODO
	// roomService.playGame(playGameDto, user);
	// }
	// }
	// }
	// }

	class Task extends Thread {

		private final Room room;

		public Task(Room room) {
			this.room = room;
		}

		@Override
		public void run() {
			while (true) {
				List<User> ai_user_list = mongoCacheService.queryAiUserOfRoom(room.getCode());
				for (User user : ai_user_list) {
					List<SocketTask> socketTaskList = socketTaskService.findByUserId(user.getId());
					for (SocketTask socketTask : socketTaskList) {
						if (socketTaskService.lockSocketTask(socketTask.getId())) {// 对此socketTask进行加锁，防止其他服务器抢
							int common_id = socketTask.getCommonId();
							// 判断此socketTask的下个操作者是否为自己
							// 如果为自己就进行操作响应
							if (common_id == PlayGameTypeEnum.CALL.getCode()) {

							}
							if (common_id == PlayGameTypeEnum.ADD_CALL.getCode()) {
								// 同上
							}
							if (common_id == PlayGameTypeEnum.COMPARE.getCode()) {
								// 判断是不是跟自己比，如果是跟自己比牌。如果输了，那就表示我已经不属于这局里面的了，如果赢了，那么判断下个操作是不是此用户，如果是，那么就同上。
							}
							if (common_id == PlayGameTypeEnum.CALL_OVER.getCode()) {// 如果有玩家是跟到底，那么就需要按照牌的大小来进行判断，什么时候可以跟此人开，或者其他，如果只有两个人，那么跟10把，然后开

							}
						}
					}
				}
			}
		}
	}

	public void process_ai_call(SocketTask socketTask, User user) {// 表示上家跟注
		ActionDto actionDto = JSON.parseObject(socketTask.getContent(), ActionDto.class);// 其他玩家的操作消息
		Long userId = actionDto.getUserId();// 其他玩家userId
		String roomCode = socketTask.getRoomCode();
		Bureau bureau = bureauService.queryBureau(roomCode);
		// Seat ai_seat = seatService.getSeat(roomCode,
		// actionDto.getNextOptIndex());
		Long _userId = actionDto.getUserId();
		if (userId.longValue() == _userId.longValue()) {// 判断是否为当前操作者
			// 判断牌的大小，然后按照一定的概率进行跟注或者加注，或者弃牌。
			// 是否游戏中为最大牌，那么就需要获取轮数，并判断现在总共有几人是在游戏中的，需要一个一个来进行比牌
			if (playerService.isMaxCard(_userId, roomCode)) {// 手牌最大
				if (bureau.getRound() > 30) { // 直接选择一个进行比牌
					// 获取游戏中的所有index
					List<Seat> seatList = seatService.getPlayingSeatInRoom(roomCode);
					// 随机取一个
					Seat seat = seatService.randomGetSet(userId, seatList);
					// 组装用户操作信息
					PlayGameDto playGameDto = new PlayGameDto();
					playGameDto.setToPlace(seat.getIndex());
					playGameDto.setToUserId(seat.getUserId());
					playGameDto.setType(PlayGameTypeEnum.COMPARE.getCode());

					roomService.playGame(playGameDto, user);
				} else {
					// 按照一定的几率进行比牌
				}
			} else {// 手牌不是最大
				int card_type = playerService.getCardTypeOfUser(_userId, roomCode);
				if (card_type > 3) {// 获取牌的类型
					if (randomNum(15)) {
						// 调用弃牌
					} else {
						// 跟注
					}
				} else {
					// 50%的概率弃牌
					if (playerService.isMinCard(_userId, "roomCode")) {// 为最小的
						if (randomNum(85)) {
							// 调用弃牌
						} else {
							// 跟注
						}
					} else {
						if (randomNum(50)) {
							// 调用弃牌
						} else {
							// 跟注
						}
					}
				}
			}
		}
	}

	public void process_ai_add_call(SocketTask socketTask) {// 表示上家加注

	}

	public void process_ai_compare(SocketTask socketTask) {// 表示上家比牌

	}

	public void process_ai_call_over(SocketTask socketTask) {// 表示上家跟到底

	}

	/**
	 * 概率
	 * 
	 * @param num
	 * @return
	 */
	public boolean randomNum(int num) {
		if (num > 100) {
			return false;
		} else {
			Random random = new Random(100);
			int random_num = random.nextInt();
			if (random_num > num) {
				return false;
			}
		}
		return true;
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
}
