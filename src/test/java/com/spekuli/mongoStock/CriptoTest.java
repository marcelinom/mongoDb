package com.spekuli.mongoStock;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import com.spekuli.model.dao.CriptoRepository;
import com.spekuli.model.dao.GapRepository;
import com.spekuli.model.dao.ScalperRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CriptoTest {
	@Autowired private CriptoRepository histRepo;
	@Autowired private ScalperRepository snapRepo;
	@Autowired private GapRepository gapRepo;
		
	@Test
	void testaConeao() {
		RestTemplate restTemplate = new RestTemplate();
		String fooResourceUrl
		  = "http://localhost:8080/spring-rest/foos";
		ResponseEntity<String> response
		  = restTemplate.getForEntity(fooResourceUrl + "/1", String.class);
		Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
	}

}
