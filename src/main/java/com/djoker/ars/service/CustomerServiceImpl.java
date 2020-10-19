package com.djoker.ars.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.djoker.ars.model.customer.Customer;
import com.djoker.ars.repository.CustomerRepository;

@Service
public class CustomerServiceImpl implements CustomerService{

	@Autowired
	private CustomerRepository customerRepo;
	
	@Override
	public List<Customer> getAllCustomers() {
		return customerRepo.findAll();
	}

	@Override
	public Optional<Customer> getCustomerById(Long id) {
		return customerRepo.findById(id);
	}

	@Override
	public Customer findByEmail(String email) {
		return customerRepo.findByEmail(email);
	}

}
