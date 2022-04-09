package com.spekuli.model.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import com.spekuli.model.entity.Gap;

@Component
public interface GapRepository extends MongoRepository<Gap, String> {

	long count();
}