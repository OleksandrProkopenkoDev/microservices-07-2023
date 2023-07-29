package com.spro.productservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spro.productservice.dto.ProductRequest;
import com.spro.productservice.model.Product;
import com.spro.productservice.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

	@Container
	static MongoDBContainer mongoDBContainer =
			new MongoDBContainer("mongo:4.4.2");
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ProductRepository productRepository;

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
		dynamicPropertyRegistry.add(
				"spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@Test
	void shouldCreateProduct() throws Exception {
		ProductRequest request = getProductRequest();
		String requestAsString = objectMapper.writeValueAsString(request);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
					.contentType(MediaType.APPLICATION_JSON)
					.content(requestAsString))
				.andExpect(status().isCreated());
		assertEquals(1, productRepository.findAll().size());
	}

	@Test
	void shouldGetAllProducts() throws Exception {
		// Given
		Product product1 = Product.builder()
				.name("S30")
				.description("samsung flagman smartphone")
				.price(BigDecimal.valueOf(1150))
				.build();
		Product product2 = Product.builder()
				.name("A3")
				.description("samsung 5inch, ips display, 64Gb memory smartphone")
				.price(BigDecimal.valueOf(450))
				.build();
		Product product3 = Product.builder()
				.name("iphone11")
				.description("Apple 6inch, ips display, 32Gb memory smartphone")
				.price(BigDecimal.valueOf(750))
				.build();
		productRepository.saveAll(List.of(product1,product2,product3));
		// When
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/product")
						.contentType(MediaType.APPLICATION_JSON))

				.andExpect(status().isOk());
		// Then
		MvcResult mvcResult = resultActions.andReturn();
		log.info(mvcResult.getResponse().getContentAsString());
	}

	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
				.name("Iphone 13")
				.description("newest innovative smartphone")
				.price(BigDecimal.valueOf(1200))
				.build();
	}

}
