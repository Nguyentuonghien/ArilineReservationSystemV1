package com.djoker.ars.service;

import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

import com.djoker.ars.model.customer.Customer;
import com.djoker.ars.repository.CustomerRepository;
import com.djoker.ars.repository.RoleRepository;

class CustomerServiceImplTest {

	@Mock
	private CustomerRepository customerRepo;
	
	@Mock
	private RoleRepository roleRepo;
	
	@Mock
	private BCryptPasswordEncoder encoder;
	
	private CustomerServiceImpl customerServiceImpl;
	 
	private Customer customer;
	
	@BeforeEach
	void setup() {
		initMocks(this);
		customerServiceImpl = new CustomerServiceImpl(customerRepo, roleRepo, encoder);
		
		customer = new Customer("john", "doll", "johny@gmail.com");
		//customer = Customer.builder().id(1l).firstName("john").lastName("doll").email("johny@gmail.com").build();
		
		Mockito.when(customerRepo.save(any())).thenReturn(customer);
		Mockito.when(customerRepo.findByEmail(anyString())).thenReturn(customer);
	}

	@Test
	public void testFindCustomerByEmail() {
		final String email = "johny@gmail.com";
		final Customer customerTest = customerServiceImpl.findByEmail(email);

		assertEquals(email, customerTest.getEmail());
	}
	
	@Test
	public void testSaveCustomer() {
		final String email = "johny@gmail.com";
		Customer customerTest = customerServiceImpl.saveCustomer(customer);

		assertEquals(email, customerTest.getEmail());
	}
	
}


