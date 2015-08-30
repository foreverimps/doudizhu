package com.zj.platform.gamecenter.dto.socket;

import com.zj.platform.gamecenter.entity.Bureau;

public class BureauDto {

	private Integer countDown;// 局中的当前倒计时

	private Integer curIndex;// 局中当前该执行动作的玩家座位下标

	private Integer round;// 轮数

	private Integer singleNote;// 单注

	private Long totalNote;// 总注

	public BureauDto(Bureau bureau) {
		if (null != bureau && bureau.getIsEnd()) {
			countDown = bureau.getCountDown();
			curIndex = bureau.getCurIndex();
			round = bureau.getRound();
			singleNote = bureau.getSingleNote();
			totalNote = bureau.getTotalNote();
		} else {
			throw new NullPointerException();
		}
	}

	public Integer getCountDown() {
		return countDown;
	}

	public void setCountDown(Integer countDown) {
		this.countDown = countDown;
	}

	public Integer getCurIndex() {
		return curIndex;
	}

	public void setCurIndex(Integer curIndex) {
		this.curIndex = curIndex;
	}

	public Integer getRound() {
		return round;
	}

	public void setRound(Integer round) {
		this.round = round;
	}

	public Integer getSingleNote() {
		return singleNote;
	}

	public void setSingleNote(Integer singleNote) {
		this.singleNote = singleNote;
	}

	public Long getTotalNote() {
		return totalNote;
	}

	public void setTotalNote(Long totalNote) {
		this.totalNote = totalNote;
	}

}
