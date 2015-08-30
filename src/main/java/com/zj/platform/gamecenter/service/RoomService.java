package com.zj.platform.gamecenter.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zj.platform.gamecenter.constant.PlayGameTypeEnum;
import com.zj.platform.gamecenter.constant.ResultEnum;
import com.zj.platform.gamecenter.dto.SocketOutMessage;
import com.zj.platform.gamecenter.dto.socket.PlayGameDto;
import com.zj.platform.gamecenter.dto.socket.RoomDto;
import com.zj.platform.gamecenter.entity.Bureau;
import com.zj.platform.gamecenter.entity.Card;
import com.zj.platform.gamecenter.entity.Player;
import com.zj.platform.gamecenter.entity.Room;
import com.zj.platform.gamecenter.entity.Seat;
import com.zj.platform.gamecenter.entity.User;
import com.zj.platform.gamecenter.mongoDao.RoomDao;
import com.zj.platform.gamecenter.utils.BuildResultUtil;

@Service
public class RoomService {

	@Autowired
	private RoomDao roomDao;

	private static final Logger logger = LoggerFactory.getLogger(RoomService.class);

	@Autowired
	private CardService cardService;

	@Autowired
	private UserService userService;

	@Autowired
	private SeqService seqService;

	@Autowired
	private SocketTaskService socketTaskService;

	@Autowired
	private MongoCacheService mongoCacheService;

	@Autowired
	private SeatService seatService;

	@Autowired
	private BureauService bureauService;

	@Autowired
	private PlayerService playerService;

	// // 存放所有的房间
	// private final List<Room> allRoom = new ArrayList<Room>(12);
	//
	// // roomMap<roomCode,Room>
	// private final HashMap<String, Room> roomMap = new HashMap<String,
	// Room>();
	//
	// // List<userId> 所有房间的用户id的集合
	// private final List<Long> allRoomUserIdList = new ArrayList<Long>();

	// public List<Room> queryByPage() {
	// return null;
	// }

	/**
	 * 快速开始
	 * 
	 * @param content
	 *            房间类型,为空时自动选择房间类型
	 * @param user
	 * @return
	 */
	public SocketOutMessage quickStart(String content, User user) {
		Player player = playerService.getPlayerByUserId(user.getId());
		try {
			int type = -1;
			if (StringUtils.isEmpty(content)) {
				// TODO 获取Type值
				type = 0;
			} else {
				type = Integer.parseInt(content);
			}
			// 找到一个座位让用户坐下
			RoomDto entryRoom = findFitSeat(player, type);
			if (entryRoom == null) {
				return BuildResultUtil.buildOutResult(ResultEnum.ERROR, "加入房间超时，请重试");
			}
			// 将Room对象的当前信息返回到前台
			return BuildResultUtil.buildOutResult(ResultEnum.SUCCESS, entryRoom);
		}
		catch (Exception e) {
			return BuildResultUtil.buildOutResult(ResultEnum.ERROR);
		}
	}

	public RoomDto findFitSeat(Player player, int type) {
		return roomDao.findFitSeat(player, type);
	}

	// public int getPlaceByUserIdAndRoomCode(Long userId, String roomCode) {
	// Room room = roomMap.get(roomCode);
	// for (PlayerDto dto : room.getPlayerList()) {
	// if (dto.getUserId().longValue() == userId.longValue()) {
	// return dto.getPlace();
	// }
	// }
	// return 0;
	// }

	// /** 创建游戏 */
	// public Room create(RoomDto roomDto, User user) {
	// Room room = new Room();
	// String code = seqService.createRoomCode(user.getId());
	// BeanUtils.copyProperties(roomDto, room);
	// room.setCode(code);
	//
	// List<PlayerDto> playerList = new ArrayList<PlayerDto>(12);
	// PlayerDto playerDto = new PlayerDto(user.getId(),
	// PlayerStatusEnum.WAIT.getCode(), 1, user.getAccount());
	// playerDto.setIngotAccountAmount(user.getIngotAccountAmount());
	// playerDto.setMoneyAccountAmount(user.getMoneyAccountAmount());
	// playerList.add(playerDto);
	// room.setPlayerList(playerList);
	//
	// allRoom.add(room);
	// roomMap.put(code, room);
	// allRoomUserIdList.add(user.getId());
	//
	// return room;
	// }
	//
	// public List<Room> getByType(int type, int pageNo, int pageSize) {
	// return null;
	// }

	// /** 根据code获取room */
	// public Room getRoomByCode(String code) {
	// return roomMap.get(code);
	// }

	// /** 根据code获取room里面的List<userId> */
	// public List<Long> getUserIdsByRoomCode(String roomCode) {
	// Room room = roomMap.get(roomCode);
	// List<PlayerDto> playerdDtos = room.getPlayerList();
	// List<Long> userIds = new ArrayList<Long>();
	// if (CollectionUtils.isNotEmpty(playerdDtos)) {
	// for (PlayerDto dto : playerdDtos) {
	// userIds.add(dto.getUserId());
	// }
	// }
	// return userIds;
	// }

	/** 玩游戏 */
	public SocketOutMessage playGame(PlayGameDto playGameDto, User user) {
		Long userId = user.getId();
		String roomCode = playGameDto.getRoomCode();
		int index = playGameDto.getPlace();
		int type = playGameDto.getType();
		Player player = playerService.getPlayerByUserId(user.getId());
		if (player == null) {
			return BuildResultUtil.buildOutResult(ResultEnum.ERROR, "玩家尚未登录");
		}
		if (roomCode.equals(player.getRoomCode())) {
			return BuildResultUtil.buildOutResult(ResultEnum.ERROR, "数据不符");
		}
		// 占座
		if (PlayGameTypeEnum.SIT.getCode() == type) {
			Seat seat = seatService.getSeat(roomCode, index);
			if (null == seat || null != seat.getUserId()) {
				return BuildResultUtil.buildOutResult(ResultEnum.ERROR, "座位不存在或已被占");
			}
			if (seatService.updateSeatSit(roomCode, index, player)) {
				seat.playerSit(player);
				return BuildResultUtil.buildOutResult(ResultEnum.SUCCESS);
			}
			return BuildResultUtil.buildOutResult(ResultEnum.ERROR, "座位已被占");
		}
		// 离座
		if (PlayGameTypeEnum.STAND.getCode() == type) {
			Seat seat = seatService.getSeat(roomCode, index);
			if (null == seat || (!userId.equals(seat.getUserId()))) {
				return BuildResultUtil.buildOutResult(ResultEnum.ERROR, "数据不符");
			}
			if (!seat.getIsOut()) {
				return BuildResultUtil.buildOutResult(ResultEnum.ERROR, "还在游戏过程中，不可换离座");
			}
			seat.kickPlayerDto();
			return BuildResultUtil.buildOutResult(ResultEnum.SUCCESS);
		}
		// 换位置
		if (PlayGameTypeEnum.CHANGE_PLACE.getCode() == type) {// 换座位
			Seat seat = seatService.getSeat(roomCode, index);
			if (null == seat || (!userId.equals(seat.getUserId()))) {
				return BuildResultUtil.buildOutResult(ResultEnum.ERROR, "数据不符");
			}
			if (!seat.getIsOut()) {
				return BuildResultUtil.buildOutResult(ResultEnum.ERROR, "还在游戏过程中，不可换座位");
			}
			Seat newSeat = seatService.getSeat(roomCode, playGameDto.getToPlace());
			if (seatService.updateSeatSit(roomCode, newSeat.getIndex(), player)) {
				// 成功坐下
				newSeat.playerSit(player);
				seat.kickPlayerDto();
				return BuildResultUtil.buildOutResult(ResultEnum.SUCCESS);
			}
			return BuildResultUtil.buildOutResult(ResultEnum.ERROR);
		}
		// 准备
		if (PlayGameTypeEnum.READY.getCode() == type) {
			Seat seat = seatService.getSeat(roomCode, index);
			if (null == seat || (!userId.equals(seat.getUserId()))) {
				return BuildResultUtil.buildOutResult(ResultEnum.ERROR, "数据不符");
			}
			seat.sayReady();
			return BuildResultUtil.buildOutResult(ResultEnum.SUCCESS);
		}
		// 比较
		if (PlayGameTypeEnum.COMPARE.getCode() == type) {// 比较的时候需要看是否轮到此人
			Seat seat = seatService.getSeat(roomCode, index);
			if (null == seat || (!userId.equals(seat.getUserId()))) {
				return BuildResultUtil.buildOutResult(ResultEnum.ERROR, "数据不符");
			}
			if (seat.cardCompare(playGameDto.getToPlace())) {
				// 比牌成功
				return BuildResultUtil.buildOutResult(ResultEnum.SUCCESS);
			}
			return BuildResultUtil.buildOutResult(ResultEnum.ERROR);
		}

		// 看牌
		if (PlayGameTypeEnum.LOOK.getCode() == type) {
			Seat seat = seatService.getSeat(roomCode, index);
			if (null == seat || (!userId.equals(seat.getUserId()))) {
				return BuildResultUtil.buildOutResult(ResultEnum.ERROR, "数据不符");
			}
			List<Card> cards = seat.check();
			return BuildResultUtil.buildOutResult(ResultEnum.SUCCESS, cards);
		}
		// 弃牌
		if (PlayGameTypeEnum.GIVE_UP.getCode() == type) {
			Seat seat = seatService.getSeat(roomCode, index);
			if (null == seat || (!userId.equals(seat.getUserId()))) {
				return BuildResultUtil.buildOutResult(ResultEnum.ERROR, "数据不符");
			}
			Bureau bureau = bureauService.getLastBureauDtoInRoom(roomCode);
			// 防止并发
			while (!bureau.getIsEnd() && (!seat.giveUp())) {
				bureau = bureauService.getLastBureauDtoInRoom(roomCode);
			}
			if (bureau.getIsEnd()) {
				return BuildResultUtil.buildOutResult(ResultEnum.ERROR, "牌局已结束");
			}
			return BuildResultUtil.buildOutResult(ResultEnum.SUCCESS);
		}
		// 跟注
		if (PlayGameTypeEnum.CALL.getCode() == type) {
			Seat seat = seatService.getSeat(roomCode, index);
			if (null == seat || (!userId.equals(seat.getUserId()))) {
				return BuildResultUtil.buildOutResult(ResultEnum.ERROR, "数据不符");
			}
			if (seat.noteOn()) {
				return BuildResultUtil.buildOutResult(ResultEnum.SUCCESS);
			}
			return BuildResultUtil.buildOutResult(ResultEnum.ERROR);
		}
		// 加注
		if (PlayGameTypeEnum.ADD_CALL.getCode() == type) {
			Seat seat = seatService.getSeat(roomCode, index);
			if (null == seat || (!userId.equals(seat.getUserId()))) {
				return BuildResultUtil.buildOutResult(ResultEnum.ERROR, "数据不符");
			}
			if (seat.noteAdd(playGameDto.getAmount())) {
				return BuildResultUtil.buildOutResult(ResultEnum.SUCCESS);
			}
			return BuildResultUtil.buildOutResult(ResultEnum.ERROR);
		}
		// 退出房间
		if (PlayGameTypeEnum.OUT.getCode() == type) {
			Seat seat = seatService.getSeat(roomCode, index);
			if (null == seat || (!userId.equals(seat.getUserId()))) {
				return BuildResultUtil.buildOutResult(ResultEnum.ERROR, "数据不符");
			}
			if (!seat.getIsOut()) {
				return BuildResultUtil.buildOutResult(ResultEnum.ERROR, "还在游戏过程中，不可离座");
			}
			seat.kickPlayerDto();
			player.setRoomCode("");
			playerService.updatePlayerRoomCode(userId, "");
			return BuildResultUtil.buildOutResult(ResultEnum.SUCCESS);
		}
		return BuildResultUtil.buildOutResult(ResultEnum.SUCCESS);
	}

	/** 发牌 */
	// public HashMap<Long, List<int[]>> getJinHuaCard(List<Long> userIds) {
	// return cardService.gen3Card(bureau);
	// }

	// /** 更新房间信息 */
	// public SocketOutMessage updateRoom(Room room, PlayGameDto playGameDto,
	// User user) {
	// int amount = playGameDto.getAmount();// 下注的数量
	// int type = playGameDto.getType();// 操作类型：准备，跟注，加注等
	// Long userId = user.getId();// 操作者的用户ID
	// String code = room.getCode();// 操作者所在的房间
	// if (PlayGameTypeEnum.IN.getCode() == type) {
	// PlayerDto dto = new PlayerDto(userId, PlayerStatusEnum.WAIT.getCode(),
	// getNewPlace(room), user.getAccount());// 初始化进入的玩家
	// dto.setIngotAccountAmount(user.getIngotAccountAmount());//
	// 设置玩家的元宝数量，从用户属性中获取到
	// dto.setMoneyAccountAmount(user.getMoneyAccountAmount());//
	// 设置玩家的游戏币数量，从用户属性中获取到
	// dto.setType(type);// 设置玩家的操作类型：准备，跟注，加注等
	// dto.setBottomNote(room.getBottomNote());// 设置当前的最低注数，当有人加注的时候，这个数量就得重新设置。
	// List<PlayerDto> playerList = room.getPlayerList();
	// playerList.add(dto);
	// room.setPlayerList(playerList);
	// clearRoomCards(room);// 防止用户的牌被读取到，返回给用户之前，必须清空房间用户所有的牌的信息。
	// notifyRoomUser(code, type, SocketConstant.PLAY_GAME.getCode(), dto);//
	// 通知所有房间用户，此用户的具体操作
	// return BuildResultUtil.buildOutResult(ResultEnum.SUCCESS, room);//
	// 返回结果给当前操作者
	// } else if (PlayGameTypeEnum.READY.getCode() == type) {
	// updatePlayerStatus(room, PlayerStatusEnum.READY.getCode(), userId, type);
	// PlayerDto dto = getPlayerByUserId(room, userId);
	// notifyRoomUser(code, type, SocketConstant.PLAY_GAME.getCode(), dto);
	// return BuildResultUtil.buildOutResult(ResultEnum.SUCCESS);
	// }
	//
	// else if (PlayGameTypeEnum.CHANGE_PLACE.getCode() == type) {
	// notifyRoomUser(code, type, SocketConstant.PLAY_GAME.getCode(),
	// playGameDto);
	// return updatePlayerPlace(room, userId, playGameDto.getToPlace());
	// }
	//
	// else if (PlayGameTypeEnum.LOOK.getCode() == type) {
	// return lookCard(room, userId);
	// }
	//
	// else if (PlayGameTypeEnum.COMPARE.getCode() == type) {
	// return playerCompare(room, playGameDto);
	// }
	//
	// else if (PlayGameTypeEnum.GIVE_UP.getCode() == type) {
	// return playerGiveUp(room, userId);
	// }
	//
	// else if (PlayGameTypeEnum.CALL.getCode() == type) {
	// return updatePlayerNote(room, room.getBottomNote(), userId, type);
	// }
	//
	// else if (PlayGameTypeEnum.ADD_CALL.getCode() == type) {
	// if (amount > room.getBottomNote()) {
	// room.setBottomNote(amount);
	// return updatePlayerNote(room, amount, userId, type);
	// } else {
	// return BuildResultUtil.buildOutResult(ResultEnum.ERROR, "金额必须大于之前的金额");
	// }
	// }
	//
	// else if (PlayGameTypeEnum.OUT.getCode() == type) {
	// if (room.getPlayerList().size() == 1) {// 表示还剩一人
	// roomMap.remove(room.getCode());
	// return BuildResultUtil.buildOutResult(ResultEnum.SUCCESS);
	// }
	// return playerOut(room, userId);
	// }
	// return null;
	// }

	//
	// /** 清空房间用户牌的信息 */
	// public Room clearRoomCards(Room room) {
	// List<PlayerDto> playerDtoList = new ArrayList<PlayerDto>();
	// for (PlayerDto dto : room.getPlayerList()) {
	// dto.setCards(null);
	// playerDtoList.add(dto);
	// }
	// room.setPlayerList(playerDtoList);
	//
	// return room;
	// }

	// /** 房间所用添加socket_task */
	// public void notifyRoomUser(String code, int type, int commonId, Object
	// obj) {
	// List<Long> userIds = getUserIdsByRoomCode(code);
	// for (Long userId : userIds) {
	// SocketOutMessage out = new SocketOutMessage();
	// out.setId(commonId);
	// out.setContent(obj);
	// out.setCode(ResultEnum.SUCCESS.getCode());
	// // logger.info("SocketOutMessage = " + JSON.toJSONString(out));
	// insertSocketTask(code, userId, type, JSON.toJSONString(out), commonId);
	// }
	// }

	// /** 添加socket_task */
	// public void insertSocketTask(String roomCode, Long userId, int type,
	// String content, int commonId) {
	// logger.info("insertSocketTask");
	// SocketTask task = new SocketTask();
	// task.setRoomCode(roomCode);
	// task.setUserId(userId);
	// task.setType(type);
	// task.setCommonId(commonId);
	// task.setContent(content);
	// task.setStatus(SocketTaskStatusEnum.WAIT_PROCESS.getCode());
	// socketTaskService.insert(task);
	// }

	// /** 更新房间里玩家的状态 */
	// public void updatePlayerStatus(Room room, int status, Long userId, int
	// type) {
	// List<PlayerDto> playerList = room.getPlayerList();// 老的
	// List<PlayerDto> _playerList = new ArrayList<PlayerDto>();// 新的
	// for (PlayerDto dto : playerList) {
	// if (dto.getUserId().longValue() == userId.longValue()) {
	// dto.setStatus(PlayerStatusEnum.READY.getCode());
	// dto.setType(type);
	// }
	// _playerList.add(dto);
	// }
	// room.setPlayerList(_playerList);
	// roomMap.put(room.getCode(), room);
	// }

	// /** 获取用户牌 */
	// public SocketOutMessage lookCard(Room room, Long userId) {
	// List<PlayerDto> playerList = room.getPlayerList();// 老的
	// List<Card> cards = new ArrayList<Card>();
	// PlayerDto _dto = new PlayerDto();
	// for (PlayerDto dto : playerList) {
	// if (dto.getUserId().longValue() == userId.longValue()) {
	// cards = dto.getCards();
	// BeanUtils.copyProperties(dto, _dto);
	// continue;
	// }
	// }
	// if (CollectionUtils.isEmpty(cards)) {
	// return BuildResultUtil.buildOutResult(ResultEnum.ERROR, "不在本房间");
	// }
	// roomMap.put(room.getCode(), room);
	// _dto.setCards(null);
	// _dto.setType(PlayGameTypeEnum.LOOK.getCode());
	// notifyRoomUser(room.getCode(), PlayGameTypeEnum.LOOK.getCode(),
	// SocketConstant.PLAY_GAME.getCode(), _dto);
	// _dto.setCards(cards);
	// return BuildResultUtil.buildOutResult(ResultEnum.SUCCESS, _dto);
	// }
	//
	// /** 更新房间里玩家的下注的数量 */
	// public SocketOutMessage updatePlayerNote(Room room, int note, Long
	// userId, int type) {
	// List<PlayerDto> playerList = room.getPlayerList();// 老的
	// List<PlayerDto> _playerList = new ArrayList<PlayerDto>();// 新的
	// PlayerDto _dto = new PlayerDto();
	// for (PlayerDto dto : playerList) {
	// if (dto.getUserId().longValue() == userId.longValue()) {
	// dto.setNote(dto.getNote() + note);
	// dto.setType(type);
	// BeanUtils.copyProperties(dto, _dto);
	// // _dto = dto，不能使用，引用传递
	// }
	// _playerList.add(dto);
	// }
	// room.setPlayerList(_playerList);
	//
	// roomMap.put(room.getCode(), room);
	// _dto.setCards(null);
	// _dto.setBottomNote(room.getBottomNote());
	// notifyRoomUser(room.getCode(), type, SocketConstant.PLAY_GAME.getCode(),
	// _dto);
	// return BuildResultUtil.buildOutResult(ResultEnum.SUCCESS);
	// }
	//
	// /** 玩家退出房间 */
	// public SocketOutMessage playerOut(Room room, Long userId) {
	// List<PlayerDto> playerList = room.getPlayerList();// 老的
	// List<PlayerDto> _playerList = new ArrayList<PlayerDto>();// 新的
	// // List<PlayerDto> historyPlayerList = room.getHistoryPlayerList();
	// for (PlayerDto dto : playerList) {
	// if (dto.getUserId().longValue() != userId.longValue()) {// 不相等才放入
	// _playerList.add(dto);
	// } else {
	// // historyPlayerList.add(dto);
	// }
	// }
	// // room.setHistoryPlayerList(historyPlayerList);
	// room.setPlayerList(_playerList);
	// roomMap.put(room.getCode(), room);
	//
	// int gameingNum = getNumByStatus(room,
	// PlayerStatusEnum.GAMEING.getCode());
	// if (gameingNum == 1) {// 只有一个人在游戏中，那么就表示此人胜利
	// List<Long> userIds = getUserIdByStatus(room,
	// PlayerStatusEnum.GAMEING.getCode());
	// Long winerUserId = userIds.get(0);
	// return playerGameOver(room, winerUserId);// 游戏结束
	// }
	//
	// notifyRoomUser(room.getCode(), PlayGameTypeEnum.OUT.getCode(),
	// SocketConstant.PLAY_GAME.getCode(), null);
	// return BuildResultUtil.buildOutResult(ResultEnum.SUCCESS);
	// }
	//
	// /** 弃牌 */
	// public SocketOutMessage playerCompare(Room room, PlayGameDto playGameDto)
	// {
	// List<PlayerDto> playerList = room.getPlayerList();// 老的
	// List<PlayerDto> _playerList = new ArrayList<PlayerDto>();// 新的
	// Long lowerUserId = playGameDto.getUserId();
	// // if (playGameDto.getWinerUserId().longValue() ==
	// // playGameDto.getUserId().longValue()) {
	// // lowerUserId = playGameDto.getToUserId();
	// // }
	// // List<PlayerDto> historyPlayerList = room.getHistoryPlayerList();
	// for (PlayerDto dto : playerList) {
	// if (dto.getUserId().longValue() == lowerUserId.longValue()) {// 相等
	// dto.setStatus(PlayerStatusEnum.WAIT.getCode());
	// // historyPlayerList.add(dto);
	// }
	// _playerList.add(dto);
	// }
	// // room.setHistoryPlayerList(historyPlayerList);
	// room.setPlayerList(_playerList);
	//
	// int gameingNum = getNumByStatus(room,
	// PlayerStatusEnum.GAMEING.getCode());
	// if (gameingNum == 1) {// 只有一个人在游戏中，那么就表示此人胜利
	// List<Long> userIds = getUserIdByStatus(room,
	// PlayerStatusEnum.GAMEING.getCode());
	// Long winerUserId = userIds.get(0);
	// return playerGameOver(room, winerUserId);// 游戏结束
	// }
	// roomMap.put(room.getCode(), room);
	// notifyRoomUser(room.getCode(), PlayGameTypeEnum.COMPARE.getCode(),
	// SocketConstant.PLAY_GAME.getCode(), playGameDto);
	// return BuildResultUtil.buildOutResult(ResultEnum.SUCCESS);
	// }
	//
	// /** 弃牌 */
	// public SocketOutMessage playerGiveUp(Room room, Long userId) {
	// List<PlayerDto> playerList = room.getPlayerList();// 老的
	// List<PlayerDto> _playerList = new ArrayList<PlayerDto>();// 新的
	// // List<PlayerDto> historyPlayerList = room.getHistoryPlayerList();
	// for (PlayerDto dto : playerList) {
	// if (dto.getUserId().longValue() == userId.longValue()) {// 相等
	// dto.setStatus(PlayerStatusEnum.WAIT.getCode());
	// // historyPlayerList.add(dto);
	// }
	// _playerList.add(dto);
	// }
	// // room.setHistoryPlayerList(historyPlayerList);
	// room.setPlayerList(_playerList);
	//
	// int gameingNum = getNumByStatus(room,
	// PlayerStatusEnum.GAMEING.getCode());
	// if (gameingNum == 1) {// 只有一个人在游戏中，那么就表示此人胜利
	// List<Long> userIds = getUserIdByStatus(room,
	// PlayerStatusEnum.GAMEING.getCode());
	// Long winerUserId = userIds.get(0);
	// return playerGameOver(room, winerUserId);// 游戏结束
	// }
	// roomMap.put(room.getCode(), room);
	// notifyRoomUser(room.getCode(), PlayGameTypeEnum.GIVE_UP.getCode(),
	// SocketConstant.PLAY_GAME.getCode(), null);
	// return BuildResultUtil.buildOutResult(ResultEnum.SUCCESS);
	// }
	//
	// /** 游戏结束 */
	// public SocketOutMessage playerGameOver(Room room, Long userId) {
	// List<PlayerDto> playerList = room.getPlayerList();// 老的
	// List<PlayerDto> _playerList = new ArrayList<PlayerDto>();// 新的
	// // List<PlayerDto> historyPlayerList = room.getHistoryPlayerList();
	// for (PlayerDto dto : playerList) {
	// if (dto.getUserId().longValue() == userId.longValue()) {// 相等
	// dto.setStatus(PlayerStatusEnum.WAIT.getCode());
	// // historyPlayerList.add(dto);
	// }
	// _playerList.add(dto);
	// }
	// // room.setWinerUserId(userId);
	// // room.setHistoryPlayerList(historyPlayerList);
	// room.setPlayerList(_playerList);
	// roomMap.put(room.getCode(), room);
	// notifyRoomUser(room.getCode(), PlayGameTypeEnum.GAME_OVER.getCode(),
	// SocketConstant.PLAY_GAME.getCode(), null);
	// return BuildResultUtil.buildOutResult(ResultEnum.SUCCESS);
	// }
	//
	// /** 获取房间具体玩的牌 */
	// public List<Card> getPlayerCards(Room room, Long userId) {
	// for (PlayerDto dto : room.getBureau().getPlayerList()) {
	// if (userId.longValue() == dto.getUserId().longValue()) {
	// return dto.getCards();
	// }
	// }
	// return null;
	// }
	//
	// /** 获取新的座位号 */
	// public int getNewPlace(Room room) {
	// // List<Integer> placeList = new
	// // ArrayList<Integer>(room.getPersonNum());
	// // for (int i = 1; i < room.getPersonNum() + 1; i++) {
	// // placeList.add(i);
	// // }
	// // for (PlayerDto dto : room.getPlayerList()) {
	// // placeList.remove((Object) dto.getPlace());
	// // }
	// // return placeList.get(0);
	// return 0;
	// }
	//
	// /** 更新玩家的座位号 */
	// public SocketOutMessage updatePlayerPlace(Room room, Long userId, Integer
	// toPlace) {
	// List<Integer> placeList = new ArrayList<Integer>();
	// for (PlayerDto dto : room.getPlayerList()) {
	// placeList.add(dto.getPlace());
	// }
	// if (placeList.contains(toPlace)) {
	// return BuildResultUtil.buildOutResult(ResultEnum.ERROR, "此位置有人");
	// }
	//
	// List<PlayerDto> playerList = room.getPlayerList();// 老的
	// List<PlayerDto> _playerList = new ArrayList<PlayerDto>();// 新的
	// for (PlayerDto dto : playerList) {
	// if (dto.getUserId().longValue() == userId.longValue()) {
	// dto.setPlace(toPlace);
	// }
	// _playerList.add(dto);
	// }
	// room.setPlayerList(_playerList);
	// roomMap.put(room.getCode(), room);
	// return BuildResultUtil.buildOutResult(ResultEnum.SUCCESS);
	// }
	//
	// /** 获取下一个操作的用户 */
	// public Long getNextOptUserId(Room room) {
	// List<PlayerDto> playerDtos = getPlayerByStatus(room,
	// PlayerStatusEnum.GAMEING.getCode());
	// Map<Integer, Long> place2UserId = new HashMap<Integer, Long>();
	// List<Integer> places = new ArrayList<Integer>();
	// Integer workPlace = 0;
	// for (PlayerDto dto : playerDtos) {
	// place2UserId.put(dto.getPlace(), dto.getUserId());
	// places.add(dto.getPlace());
	// // if (dto.getIsCurrWork()) {
	// // workPlace = dto.getPlace();
	// // }
	// }
	// Integer[] placesInt = places.toArray(new Integer[places.size()]);//
	// 座位进行排序
	// Arrays.sort(placesInt);
	// Integer nextPlace = workPlace + 1;
	// if (!places.contains(nextPlace)) {
	// nextPlace = placesInt[0];
	// }
	// return place2UserId.get(nextPlace);
	//
	// }
	//
	// /** 判断是否下一个工作者 */
	// public boolean isNextOptByUserId(Room room, Long userId) {
	// List<PlayerDto> playerDtos = getPlayerByStatus(room,
	// PlayerStatusEnum.GAMEING.getCode());
	// Map<Integer, Long> place2UserId = new HashMap<Integer, Long>();
	// List<Integer> places = new ArrayList<Integer>();
	// Integer workPlace = 0;
	// for (PlayerDto dto : playerDtos) {
	// place2UserId.put(dto.getPlace(), dto.getUserId());
	// places.add(dto.getPlace());
	// // if (dto.getIsCurrWork()) {
	// // workPlace = dto.getPlace();
	// // }
	// }
	// Integer[] placesInt = places.toArray(new Integer[places.size()]);//
	// 座位进行排序
	// Arrays.sort(placesInt);
	// Integer nextPlace = workPlace + 1;
	// if (!places.contains(nextPlace)) {
	// nextPlace = placesInt[0];
	// }
	// Long newtUserId = place2UserId.get(nextPlace);
	// if (newtUserId.longValue() == userId.longValue()) {
	// return true;
	// }
	// return false;
	// }
	//
	// /** 判断是否下一个工作者 */
	// public boolean isNextOptByUserId(String roomCode, Long userId) {
	// Room room = roomMap.get(roomCode);
	// List<PlayerDto> playerDtos = getPlayerByStatus(room,
	// PlayerStatusEnum.GAMEING.getCode());
	// Map<Integer, Long> place2UserId = new HashMap<Integer, Long>();
	// List<Integer> places = new ArrayList<Integer>();
	// Integer workPlace = 0;
	// for (PlayerDto dto : playerDtos) {
	// place2UserId.put(dto.getPlace(), dto.getUserId());
	// places.add(dto.getPlace());
	// // if (dto.getIsCurrWork()) {
	// // workPlace = dto.getPlace();
	// // }
	// }
	// Integer[] placesInt = places.toArray(new Integer[places.size()]);//
	// 座位进行排序
	// Arrays.sort(placesInt);
	// Integer nextPlace = workPlace + 1;
	// if (!places.contains(nextPlace)) {
	// nextPlace = placesInt[0];
	// }
	// Long newtUserId = place2UserId.get(nextPlace);
	// if (newtUserId.longValue() == userId.longValue()) {
	// return true;
	// }
	// return false;
	// }
	//
	// /** 判断用户是否处于此状态下 */
	// public boolean isAtStatus(Long userId, Room room, int status) {
	// int _status = getStatusOfPlayer(room, userId);
	// if (status == _status) {
	// return true;
	// }
	// return false;
	// }
	//
	// /** 获取此玩家的状态 */
	// public int getStatusOfPlayer(Room room, Long userId) {
	// List<PlayerDto> playerList = room.getPlayerList();
	// for (PlayerDto dto : playerList) {
	// if (dto.getUserId().longValue() == userId.longValue()) {
	// return dto.getStatus();
	// }
	// }
	// return 0;
	// }
	//
	// /** 获取玩家 */
	// public PlayerDto getPlayerByUserId(Room room, Long userId) {
	// List<PlayerDto> playerList = room.getPlayerList();
	// for (PlayerDto dto : playerList) {
	// if (dto.getUserId().longValue() == userId.longValue()) {
	// return dto;
	// }
	// }
	// return null;
	// }
	//
	// /** 此状态下的用户数量 */
	// public int getNumByStatus(Room room, int status) {
	// List<Long> userIds = getUserIdByStatus(room, status);
	// if (CollectionUtils.isEmpty(userIds)) {
	// return 0;
	// }
	// return userIds.size();
	// }
	//
	// /** 此状态下的List<userId> */
	// public List<Long> getUserIdByStatus(Room room, int status) {
	// List<PlayerDto> playerList = room.getPlayerList();
	// List<Long> list = new ArrayList<Long>();
	// for (PlayerDto dto : playerList) {
	// if (dto.getStatus() == status) {
	// list.add(dto.getUserId());
	// }
	// }
	// return list;
	// }
	//
	// /** 此状态下的List<PlayerDto> */
	// public List<PlayerDto> getPlayerByStatus(Room room, int status) {
	// List<PlayerDto> playerList = room.getPlayerList();
	// List<PlayerDto> list = new ArrayList<PlayerDto>();
	// for (PlayerDto dto : playerList) {
	// if (dto.getStatus() == status) {
	// list.add(dto);
	// }
	// }
	// return list;
	// }
	//
	// /** 此状态下的List<PlayerDto> */
	// public PlayerDto getPlayerByPlace(Room room, int place) {
	// List<PlayerDto> playerList = room.getPlayerList();
	// for (PlayerDto dto : playerList) {
	// if (dto.getPlace() == place) {
	// return dto;
	// }
	// }
	// return null;
	// }

	public void createRooms(List<Room> rooms) {
		for (Room room : rooms) {
			createRoom(room);
		}
	}

	/**
	 * 创建房间
	 * 
	 * @param room
	 */
	public void createRoom(Room room) {
		roomDao.createRoom(room);
	}

	/**
	 * 根据编号获取房间对象
	 * 
	 * @param code
	 * @return
	 */
	public Room getRoomDtoByCode(String code) {
		return roomDao.getRoomDtoByCode(code);
	}

	/**
	 * 更新房间的倒计时
	 * 
	 * @param roomCode
	 * @param countDown
	 */
	public void updateRoomCountDown(String roomCode, int countDown) {
		roomDao.updateRoomCountDown(roomCode, countDown);
	}

	/**
	 * 更新房间的准备人的个数
	 * 
	 * @param roomCode
	 * @param readyCount
	 */
	public void updateRoomReadyCount(String roomCode, int readyCount) {
		roomDao.updateRoomReadyCount(roomCode, readyCount);
	}
}
