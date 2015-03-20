package net.jettdigital.cereal.tests.integration;

import net.jettdigital.cereal.Application;

import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static com.jayway.restassured.RestAssured.*;
import static org.junit.Assert.*;

import com.jayway.restassured.RestAssured;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class TestServerUp {
	
	@Value("${local.server.port}")
    int port;
	
	@Before
	public void setup() {
		RestAssured.port = port;
	}
	
	@Test
	public void testServerUp() {
		when().get("/api/ping").then().statusCode(HttpStatus.SC_OK);
	}

	
}

