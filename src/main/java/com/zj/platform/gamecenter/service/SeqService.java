package com.zj.platform.gamecenter.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeqService {

	/**
	 * OrderCode 生成方法
	 * 
	 * @return 唯一订单号
	 * @throws Exception
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public synchronized String createRoomCode(Long userId) {
		String returnNo = "";
		// 日期 yyMMddHH
		String date = getTimeString();
		returnNo = "R" + "_" + userId + "_" + date;
		return returnNo;

	}

	/**
	 * 获取当前时间 格式:yyMMddHH
	 * 
	 * @return
	 */
	private String getTimeString() {
		Date date = Calendar.getInstance().getTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
		return dateFormat.format(date);
	}
}
