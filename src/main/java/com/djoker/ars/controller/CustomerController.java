package com.djoker.ars.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.djoker.ars.model.customer.Customer;
import com.djoker.ars.service.CustomerService;

@Controller
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	@GetMapping("/")
	public String showAllCustomer(Model model) {
		List<Customer> listCustomers = customerService.getAllCustomers();
		model.addAttribute("listCustomerAccounts", listCustomers);
		return "index";
	}
	
	@GetMapping("/login")
	public String login(Model model) {
		Customer customer = new Customer();
		model.addAttribute("customer", customer);		
		return "login";
	}
	
	@GetMapping("/showCustomerForm")
	public String showCustomerForm(Model model) {
		Customer customer = new Customer();
		model.addAttribute("customerObj", customer);
		return "register";
	}
	
	@PostMapping("/register")
	public String saveCustomer(@ModelAttribute("customerObj") @Valid Customer customer, BindingResult bindingResult, Model model) {
		Customer customerTemp = customerService.findByEmail(customer.getEmail());
		// tìm thấy customerTemp đã tồn tại trong DB --> báo lỗi
		if(customerTemp != null) {
			bindingResult.rejectValue("email", "There is already an account with this email");
		}		
		if(bindingResult.hasErrors()) {
			return "register";
		}		
		customerService.saveCustomer(customer);
		
		return "redirect:/";
	}
	
	@GetMapping("/deleteCustomer/{id}")
	public String deleteCustomer(@PathVariable("id") Long id, Model model) {
		customerService.deleteCustomerById(id);
		model.addAttribute("message", "Delete customer successfully!");
		return "redirect:/"; 
	}
	
}







