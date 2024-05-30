package com.example.demo;

import com.example.demo.Model.Category;
import com.example.demo.Model.Product;
import com.example.demo.Model.Role;
import com.example.demo.Model.User;
import com.example.demo.Service.Implement.CategoryService;
import com.example.demo.Service.Implement.ProductService;
import com.example.demo.Service.Implement.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class DemoApplication implements  CommandLineRunner{

	@Autowired
	private UserService userService;
	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;

	public static void main(String[] args) {

		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Seed data for products
		if (!dataExists()) {
			// Seed data for products
			seedData();
		}
	}
	private boolean dataExists() {
		// Implement a check to see if data already exists in the database
		// You can use a query or any logic suitable for your application
		// Return true if data exists, false otherwise
		// For example, you can check if the admin user already exists
		return userService.findByUserName("admin") != null;
	}

	private void seedData() {
		// Create a category
		User admin = new User("admin","admin","0357549569","admin@gmail.com", bCryptPasswordEncoder.encode("admin"), Role.ADMIN, true);

		userService.insert(admin);
	}
}
