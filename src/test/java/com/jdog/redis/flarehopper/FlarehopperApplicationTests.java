package com.jdog.redis.flarehopper;

import javax.activation.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FlarehopperApplicationTests {

	@Autowired
	DataSource source;

	@Test

	void contextLoads() {

		System.out.println(source.getClass());
	}

}
