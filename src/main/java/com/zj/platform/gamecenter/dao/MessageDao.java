package com.zj.platform.gamecenter.dao;

import java.util.List;

import com.zj.platform.gamecenter.entity.Message;

public interface MessageDao {

	public void insert(Message message);

	public void read(Message message);

	public List<Message> queryBy2FieldUserId(Long userId);

}
