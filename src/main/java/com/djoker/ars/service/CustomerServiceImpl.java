package com.djoker.ars.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.djoker.ars.model.customer.Customer;
import com.djoker.ars.model.customer.Role;
import com.djoker.ars.repository.CustomerRepository;
import com.djoker.ars.repository.RoleRepository;

/** "UserDetailsService" là một interface trung tâm trong Spring Security
     Để tìm kiếm "Tài khoản người dùng và các vai trò của người dùng đó" (user đó là ai? và có quyền gì?) 
     Nó được sử dụng bởi Spring Security mỗi lần người dùng đăng nhập vào hệ thống. 
*/

@Service
public class CustomerServiceImpl implements CustomerService, UserDetailsService{

	@Autowired
	private CustomerRepository customerRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
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

	@Override
	public void deleteCustomerById(Long id) {
		customerRepo.deleteById(id);	
	}

	@Override
	public Customer saveCustomer(Customer customer) {
		Customer tempCustomer = new Customer();
		
		// set all field trong form register(đăng kí) vào đối tượng tempCustomer(id tự tăng nên k cần set)
		tempCustomer.setEmail(customer.getEmail());
		tempCustomer.setFirstName(customer.getFirstName());
		tempCustomer.setLastName(customer.getLastName());
		tempCustomer.setUsername(customer.getUsername());
		// mã hóa password khi ta setPassword() cho user
		tempCustomer.setPassword(passwordEncoder.encode(customer.getPassword()));
		
		Role role = roleRepo.findByRole("ADMIN");		
		// set role cho customer
		tempCustomer.setRoles(new HashSet<Role>(Arrays.asList(role)));
		
		customerRepo.save(tempCustomer);
		
		return tempCustomer;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// đầu tiên mình query xuống database xem có user đó không  
		Customer customer = customerRepo.findByUsername(username);
		// nếu khong tìm thấy User thì mình thông báo lỗi
		if(customer == null) {
			throw new UsernameNotFoundException("Invalid details!");
		}		
		List<GrantedAuthority> authorities = getCustomerAuthorities(customer.getRoles());
		
		return buildUserForAuthentication(customer, authorities);
	}
	
	// một GrantedAuthority là một quyền được ban cho user 
	// các quyền đều có tiền tố là "ROLE_" , ví dụ như ROLE_ADMIN, ROLE_MEMBER ...
	private List<GrantedAuthority> getCustomerAuthorities(Set<Role> roles) {
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		for(Role role : roles) {
			// ROLE_USER, ROLE_ADMIN,..
			authorities.add(new SimpleGrantedAuthority(role.getRole()));
		}
		List<GrantedAuthority> grantedAuthorities = new ArrayList<>(authorities);
		
		return grantedAuthorities;
	}
	
	/*
	 * private List<GrantedAuthority> getCustomerAuthorities(List<String> roles) {
	 * List<GrantedAuthority> authorities = new ArrayList<>(); for (String role :
	 * roles) { // ROLE_USER, ROLE_ADMIN,.. authorities.add(new
	 * SimpleGrantedAuthority(role)); } return authorities; }
	 */
	
	private UserDetails buildUserForAuthentication(Customer customer, List<GrantedAuthority> authorities) {
		return new User(customer.getEmail(), customer.getPassword(), true, true, true, true, authorities);
	}

	@Override
	public Customer findCustomerByUsername(String username) {
		return customerRepo.findByUsername(username);
	}

}



