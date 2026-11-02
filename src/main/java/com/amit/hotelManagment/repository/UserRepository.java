package com.amit.hotelManagment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.amit.hotelManagment.entity.User;
import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	

	boolean existsByEmail(String email);
	Optional<User>  findByEmail(String email);
	
	void deleteByEmail(String email);

}
