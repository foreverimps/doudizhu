package com.zj.platform.gamecenter.eventHandlers;

import org.springframework.beans.factory.annotation.Autowired;

import com.zj.platform.gamecenter.constant.EventTypeEnum.ThreeCardsEventTypeEnum;
import com.zj.platform.gamecenter.dto.socket.ActionDto;
import com.zj.platform.gamecenter.entity.Player;
import com.zj.platform.gamecenter.entity.Seat;
import com.zj.platform.gamecenter.events.UserEvent;
import com.zj.platform.gamecenter.jms.RemoteMessageSender;
import com.zj.platform.gamecenter.service.BureauService;
import com.zj.platform.gamecenter.service.MongoCacheService;
import com.zj.platform.gamecenter.service.PlayerService;
import com.zj.platform.gamecenter.service.SeatService;

public class ThreeCardsCompareCardsEventHandler extends SystemHandler {

	@Autowired
	private RemoteMessageSender messageSender;

	@Autowired
	private MongoCacheService cacheService;

	@Autowired
	private BureauService bureauService;

	@Autowired
	private PlayerService playerService;

	@Autowired
	private SeatService seatService;

	@Override
	public int eventCode() {
		return ThreeCardsEventTypeEnum.COMPARE_CARDS.getCode();
	}

	@Override
	public void handleEvent(UserEvent userEvent) {
		ActionDto actionDto = new ActionDto();
		actionDto.setUserId(userEvent.getUserId());
		Seat to = userEvent.getCompareTo();
		Integer note = userEvent.getNote();
		CompareDto dto = new CompareDto();
		dto.setIndex(to.getIndex());
		dto.setNote(note);
		Player toPlayer = playerService.getPlayerByUserId(to.getUserId());
		if (toPlayer != null) {
			dto.setUserId(toPlayer.getUserId());
		}
		dto.setIsLose(to.getIsLose());
		actionDto.setContent(dto);
		String roomCode = userEvent.getRoomCode();
		actionDto.setRoomCode(roomCode);
		actionDto.setRound(userEvent.getRound());
		if (userEvent.getCurIndex() != -1) {
			actionDto.setNextOptIndex(userEvent.getCurIndex());
			Seat seat = seatService.getSeat(roomCode, userEvent.getCurIndex());
			actionDto.setNextOptUserId(seat.getUserId());
		}
		messageSender.sendToUserIds(actionDto, playerService.getUserIdsByRoomCode(roomCode));
	}

	public class CompareDto {

		private Integer index;// 对家的座位号

		private Long userId;// 对家的玩家Id

		private Integer note;// 比牌所上的注数

		private Boolean isLose;// 对家是否比牌输，即为true时，表示CompareDto输

		public Integer getIndex() {
			return index;
		}

		public void setIndex(Integer index) {
			this.index = index;
		}

		public Long getUserId() {
			return userId;
		}

		public void setUserId(Long userId) {
			this.userId = userId;
		}

		public Integer getNote() {
			return note;
		}

		public void setNote(Integer note) {
			this.note = note;
		}

		public Boolean getIsLose() {
			return isLose;
		}

		public void setIsLose(Boolean isLose) {
			this.isLose = isLose;
		}

	}

}
