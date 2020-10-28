package com.djoker.ars.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.djoker.ars.model.customer.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{
	
	Customer findByEmail(String email);
	
	Customer findByUsername(String username);
	
}
