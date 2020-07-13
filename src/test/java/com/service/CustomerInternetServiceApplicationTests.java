package com.service;

import com.service.entities.Product;
import com.service.entities.UserProfile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CustomerInternetServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerInternetServiceApplicationTests {

	UserProfile userProfile;
	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;

	private String getRootUrl() {
		return "http://localhost:" + port;
	}

	@BeforeEach
	public void setUp() {
		userProfile = new UserProfile();
		Product product = new Product();
		product.setProductType("Optic Fiber 300");
		userProfile.setUsername("admin");
		userProfile.setAddress("Germany");
		userProfile.setProduct(product);
	}

	@Test
	void contextLoads() {
	}

	@Test
	public void testCreateInternetServiceRequest() {

		ResponseEntity<?> postResponse = restTemplate.postForEntity(getRootUrl() + "/createInternetServiceRequest", userProfile, UserProfile.class);
		assertNotNull(postResponse.getStatusCode().CREATED);
	}

	@Test
	public void testCancelSubscriptionByRequestId() {
		int id = 1;
		restTemplate.postForEntity(getRootUrl() + "/createInternetServiceRequest", userProfile, UserProfile.class);
		UserProfile userProfile = restTemplate.getForObject(getRootUrl() + "/cancelSubscriptionByRequestId/" + id, UserProfile.class);
		assertTrue(!userProfile.getUsername().isEmpty());
	}

	@Test
	public void testGetAllOrders() {
		restTemplate.postForEntity(getRootUrl() + "/createInternetServiceRequest", userProfile, UserProfile.class);
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/getAllRequestedOrders",
				HttpMethod.GET, entity, String.class);
		assertNotNull(response);
	}

	@AfterEach
	public void tearDown() {
		userProfile= null;
	}
}