package com.example.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.net.URI;

import javax.annotation.Generated;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import com.example.demo.controllers.CartController;
import com.example.demo.controllers.ItemController;
import com.example.demo.controllers.OrderController;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import com.sun.tools.sjavac.Log;

import org.hibernate.annotations.Tuplizer;
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

	private static User user;

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
		User newUser = userController.createUser(createUserRequest).getBody();
		user = newUser;
		User gettingUser = userController.findByUserName(createUserRequest.getUsername()).getBody();

		assertNotNull(gettingUser);
		assertEquals(newUser.getId(), gettingUser.getId());
		assertEquals(newUser.getUsername(), gettingUser.getUsername());
		assertEquals(newUser.getPassword(), gettingUser.getPassword());
	}

	@Test
	public void testFailedRequest() {
		CreateUserRequest createUserRequest = failedCreatingUserRequest();
		User newUser = userController.createUser(createUserRequest).getBody();
		User gettingUser = userController.findByUserName(createUserRequest.getUsername()).getBody();

		assertNull(newUser);
		assertNull(gettingUser);
		assertEquals(null, newUser);
		assertEquals(null, gettingUser);
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
		cart.setUser(user);
		// System.out.println(cart.getId());
		// System.out.println(cart.getTotal());
		// System.out.println(cart.getUser());
		// System.out.println(cart.getUser().getUsername());
		// System.out.println(cart.getItems().get(0).getId());

		Item item = cart.getItems().get(cart.getItems().size()-1);

		assertEquals(modifyCartRequest.getUsername(), cart.getUser().getUsername());
		// assertEquals(modifyCartRequest.getItemId(), (cart.getItems().get(cart.getItems().size()-1).getId()));
		assertEquals(modifyCartRequest.getItemId(), item.getId().longValue());
		// Item item = itemController.getItemById(modifyCartRequest.getItemId());
		// assertEquals((modifyCartRequest.getQuantity()) * ((itemController.getItemById(modifyCartRequest.getItemId())).getBody().getPrice()), cart.getTotal());
		long expectedInDouble = modifyCartRequest.getQuantity() * item.getPrice().longValue();
		long cartTotalInDouble = cart.getTotal().longValue();
		assertEquals(expectedInDouble, cartTotalInDouble);
	}

	@Test
	public void testSubmittingOrder() {
		testCreatingUser();
		testAddToCart();
		orderController.submit(user.getUsername());
	}


	private CreateUserRequest creatingUserRequest() {
		CreateUserRequest createUserRequest = new CreateUserRequest();
		createUserRequest.setUsername("username");
		createUserRequest.setPassword("password");
		createUserRequest.setConfirmPassword("password");
		return createUserRequest;
	}

	private CreateUserRequest failedCreatingUserRequest() {
		CreateUserRequest createUserRequest = new CreateUserRequest();
		createUserRequest.setUsername("username");
		createUserRequest.setPassword("password");
		createUserRequest.setConfirmPassword("pass");
		return createUserRequest;
	}

	private ModifyCartRequest modifyingCartRequest() {
		ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
		modifyCartRequest.setUsername(user.getUsername());
		modifyCartRequest.setItemId(1);
		modifyCartRequest.setQuantity(1);
		return modifyCartRequest;
	}

}
