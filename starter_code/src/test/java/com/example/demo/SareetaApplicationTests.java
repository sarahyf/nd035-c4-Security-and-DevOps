package com.example.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URI;

import javax.annotation.Generated;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import com.example.demo.controllers.CartController;
import com.example.demo.controllers.ItemController;
import com.example.demo.controllers.OrderController;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import com.sun.tools.sjavac.Log;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
public class SareetaApplicationTests {

	private static String username;

	@Autowired
	private UserController userController;

	@Autowired
	private CartController cartController;

	@Autowired
	private ItemController itemController;

	@Autowired
	private OrderController orderController;

	
	@Test
	public void testCreatingUser() {
		CreateUserRequest createUserRequest = creatingUserRequest();
		username = createUserRequest.getUsername();
		User newUser = userController.createUser(createUserRequest).getBody();
		User gettingUser = userController.findByUserName(createUserRequest.getUsername()).getBody();

		assertNotNull(gettingUser);
		assertEquals(newUser.getId(), gettingUser.getId());
		assertEquals(newUser.getUsername(), gettingUser.getUsername());
		assertEquals(newUser.getPassword(), gettingUser.getPassword());
	}

	@Test
	public void testFailedRequest() {

	}

	@Test
	public void testGettingItems() {
		itemController.getItems();
	}

	@Test
	public void testAddToCart() {
		testCreatingUser();
		ModifyCartRequest modifyCartRequest = modifyingCartRequest();

		//login
		// userDetailsServiceImpl.loadUserByUsername(username);
		//authorization token
		


		Cart cart = cartController.addTocart(modifyCartRequest).getBody();
		System.out.println(cart.getId());
		System.out.println(cart.getTotal());
		System.out.println(cart.getUser());
		System.out.println(cart.getItems().get(0).getId());

		// assertEquals(modifyCartRequest.getUsername(), cart.getUser().getUsername());
		// assertEquals(modifyCartRequest.getItemId(), (cart.getItems().get(cart.getItems().size()-1).getId()));
		// assertEquals(modifyCartRequest.get, cart.getTotal());
	}


	private CreateUserRequest creatingUserRequest() {
		CreateUserRequest createUserRequest = new CreateUserRequest();
		createUserRequest.setUsername("username");
		createUserRequest.setPassword("password");
		createUserRequest.setConfirmPassword("password");
		return createUserRequest;
	}

	private ModifyCartRequest modifyingCartRequest() {
		ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
		modifyCartRequest.setUsername(username);
		modifyCartRequest.setItemId(1);
		modifyCartRequest.setQuantity(1);
		return modifyCartRequest;
	}

}
