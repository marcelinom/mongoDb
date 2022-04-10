package com.spekuli.model.dao;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.spekuli.model.entity.Mutex;

@Component
public class MutexCustomRepository {
	@Autowired private MongoTemplate template;
	
	@Value("${instance.id}")
	private String owner;
	
	public Mutex lock() {
		Update update = new Update();
		update.set("status", Mutex.Status.BUSY);
		update.set("data", LocalDateTime.now());
		update.set("owner", owner);
		
		Criteria c1 = Criteria.where("status").is(Mutex.Status.IDLE);
		Criteria c2 = Criteria.where("id").is("binanceStream");
		Criteria c = new Criteria().andOperator(c1, c2);

		return template.findAndModify(Query.query(c), update, Mutex.class);
	}
		
	public Mutex unlock() {
		Update update = new Update();
		update.set("status", Mutex.Status.IDLE);
		update.set("data", LocalDateTime.now());
		update.set("owner", null);
		
		Criteria c1 = Criteria.where("status").is(Mutex.Status.BUSY);
		Criteria c2 = Criteria.where("id").is("binanceStream");
		Criteria c3 = Criteria.where("owner").is(owner);
		Criteria c = new Criteria().andOperator(c1, c2, c3);

		return template.findAndModify(Query.query(c), update, Mutex.class);
	}
		
}