package com.ms.product_service;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class ProductServiceApplicationTests {

	static MongoDBContainer mongoDbContainer = new MongoDBContainer(DockerImageName.parse("mongo:latest"));

	@LocalServerPort
	private int port;

	static {
		mongoDbContainer.start();
	}

	@BeforeEach
	void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	@Test
	void shouldCreateProduct() {
		String requestBody = """
				{
					"name": "Iphone 15",
					"description": "Iphone 15",
					"price": 100
				}
				""";

		RestAssured.given()
				.contentType("application/json")
				.body(requestBody)
				.post("/api/products")
				.then()
				.statusCode(201)
				.body("id", org.hamcrest.Matchers.notNullValue())
				.body("name", org.hamcrest.Matchers.equalTo("Iphone 15"))
				.body("description", org.hamcrest.Matchers.equalTo("Iphone 15"))
				.body("price", org.hamcrest.Matchers.equalTo(100));

	}
}
