package com.djoker.ars.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.djoker.ars.model.customer.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{

	Role findByRole(String role);
	
}