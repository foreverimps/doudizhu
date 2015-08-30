package com.zj.platform.gamecenter.mongoDao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.zj.platform.gamecenter.entity.SocketTask;

@Repository
public class SocketTaskDao extends BaseMongoDao {

	public void insertTasks(Collection<SocketTask> tasks) {
		mongoTemplate.insert(tasks, SocketTask.class);
	}

	public List<SocketTask> findWaitProcessByUserIds(List<Long> userIds, int status) {
		return mongoTemplate.find(new Query(Criteria.where("userId").in(userIds).all("status").is(status)), SocketTask.class);
	}

	public List<SocketTask> findByUserIdAndStatus(Long userId, int status) {
		return mongoTemplate.find(new Query(Criteria.where("userId").is(userId).all("status").is(status)), SocketTask.class);
	}

	public int updateStatusById(String id, int sourceStatus, int targetStatus) {
		mongoTemplate.updateFirst(new Query(Criteria.where("id").is(id).and("status").is(sourceStatus)), new Update().inc("status", targetStatus),
		                          SocketTask.class);
		return 1;
	}

	public int deleteById(String id) {
		mongoTemplate.remove(new Query(Criteria.where("id").is(id)), SocketTask.class);
		return 1;
	}

	// ======================保留======================//
	public void insert(SocketTask task) {
		mongoTemplate.insert(task);
	}

	public List<SocketTask> findByUserId(Long userId) {
		return mongoTemplate.find(new Query(Criteria.where("userId").is(userId)), SocketTask.class);
	}

}
