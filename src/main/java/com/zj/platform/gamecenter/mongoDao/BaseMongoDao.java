package com.zj.platform.gamecenter.mongoDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public abstract class BaseMongoDao {

	@Autowired
	protected MongoTemplate mongoTemplate;
}
