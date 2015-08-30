package com.zj.platform.gamecenter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zj.platform.gamecenter.dao.RankingDao;
import com.zj.platform.gamecenter.entity.Ranking;

@Service
public class RankingService {

	@Autowired
	private RankingDao rankingDao;

	public List<Ranking> queryByType(int type) {
		return rankingDao.queryByType(type);
	}

}
