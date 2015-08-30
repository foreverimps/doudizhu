package com.zj.platform.gamecenter.mongoDao;

import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.zj.platform.gamecenter.constant.UserSourceEnum;
import com.zj.platform.gamecenter.entity.User;

@Repository
public class UserDao extends BaseMongoDao {

	public List<User> findAll() {
		return mongoTemplate.find(new Query(), User.class);
	}

	public List<User> findAllAi() {
		return mongoTemplate.find(new Query(Criteria.where("source").is(UserSourceEnum.S_ROBOT)), User.class);
	}

	public void findAndModify(String id) {
		mongoTemplate.updateFirst(new Query(Criteria.where("id").is(id)), new Update().inc("age", 3), User.class);

	}

	public User findOne(String id) {
		return mongoTemplate.findOne(new Query(Criteria.where("id").is(id)), User.class);

	}

	public List<User> findByCodeAndType(String code, int type) {
		Query query = new Query();
		query.addCriteria(new Criteria("code").is(code));
		query.addCriteria(new Criteria("type").is(type));
		return mongoTemplate.find(query, User.class);

	}

	public User findByCode(String code) {
		Query query = new Query();
		query.addCriteria(new Criteria("code").is(code));
		return mongoTemplate.findOne(query, User.class);

	}

	public List<User> findByType(int type) {
		Query query = new Query();
		query.addCriteria(new Criteria("type").is(type));
		return mongoTemplate.find(query, User.class);

	}

	public void insert(User user) {
		mongoTemplate.insert(user);
	}

	public void update(User user) {
		Query query = new Query();
		query.addCriteria(new Criteria("_id").is(user.getId()));
		Update update = new Update();
		// update.set("bottomNote", room.getBottomNote());
		// update.set("code", room.getCode());
		mongoTemplate.updateFirst(query, update, User.class);
	}
}
