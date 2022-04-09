package com.spekuli.model.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Component;

import com.spekuli.model.entity.Cripto;

@Component
public interface CriptoRepository extends MongoRepository<Cripto, String> {

	@Query("{codigo:'?0'}")
	Cripto findByCodigo(String codigo);

	@Query("{codigo:'?0', periodo: {$gte:{$date: '?1'}, $lte:{$date: '?2'}}}")
	List<Cripto> listByCodigoPeriod(String codigo, Date inicio, Date fim);
	
	long count();
}