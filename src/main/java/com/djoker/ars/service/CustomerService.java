package com.djoker.ars.service;

import java.util.List;
import java.util.Optional;

import com.djoker.ars.model.customer.Customer;

public interface CustomerService {
	
	List<Customer> getAllCustomers();
	
	Optional<Customer> getCustomerById(Long id);
	
	Customer findByEmail(String email);
	
}
