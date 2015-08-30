package com.zj.platform.gamecenter.dao;

import java.util.List;

import com.zj.platform.gamecenter.entity.Ranking;

public interface RankingDao {

	public List<Ranking> queryAll();

	public List<Ranking> queryByType(int type);

}
