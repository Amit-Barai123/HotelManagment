package com.amit.hotelManagment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amit.hotelManagment.entity.Role;
import java.util.List;


public interface RoleRepository extends JpaRepository<Role,Long> {
Optional<Role> findByName(String name);

boolean existsByName(String name);

}
