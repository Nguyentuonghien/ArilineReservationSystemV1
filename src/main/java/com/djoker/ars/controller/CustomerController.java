package com.djoker.ars.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.djoker.ars.model.customer.Customer;
import com.djoker.ars.service.CustomerService;

@Controller
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	@GetMapping("/login")
	public String login(Model model) {
		Customer customer = new Customer();
		model.addAttribute("customer", customer);
		
		return "login";
	}
	
}
