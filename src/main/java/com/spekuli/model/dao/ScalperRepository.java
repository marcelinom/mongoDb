package com.spekuli.model.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Component;

import com.spekuli.model.entity.Scalper;

@Component
public interface ScalperRepository extends MongoRepository<Scalper, String> {

	@Query("{codigo:'?0', data: {$gte:'?1', $lte:'?2'}}")
	List<Scalper> listar(String codigo, Date inicio, Date fim);

	long count();
}