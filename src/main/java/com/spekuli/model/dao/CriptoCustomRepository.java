package com.spekuli.model.dao;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.spekuli.model.entity.Cripto;

@Component
public class CriptoCustomRepository {

	@Autowired private MongoTemplate template;
	
	public List<Cripto> listByCodigoPeriod(String codigo, Date inicio, Date fim) {
		Criteria c1 = Criteria.where("periodo").gte(inicio);
		Criteria c2 = Criteria.where("periodo").lte(fim);
		Criteria c3 = Criteria.where("codigo").is(codigo);
		Criteria c = new Criteria().andOperator(c1, c2, c3);

		Query qry = Query.query(c).with(Sort.by(Sort.Direction.ASC, "periodo"));
		return template.find(qry, Cripto.class);	
	}
	
	public Cripto save(Cripto coin) {
		return template.save(coin);
	}	
	
}