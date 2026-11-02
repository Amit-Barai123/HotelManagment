package com.amit.hotelManagment.service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amit.hotelManagment.entity.Role;
import com.amit.hotelManagment.entity.User;
import com.amit.hotelManagment.exception.UserAlreadyExistsException;
import com.amit.hotelManagment.exception.UserNotFoundException;
import com.amit.hotelManagment.repository.RoleRepository;
import com.amit.hotelManagment.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
	
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	

	@Override
	public User registerUser(User user) {
	if(userRepository.existsByEmail(user.getEmail())) {
		throw new UserAlreadyExistsException("user already exists with email" + user.getEmail());
	}
	user.setPassword(passwordEncoder.encode(user.getPassword()));
	Role userRole = roleRepository.findByName("ROLE_USER").get();
    user.setRoles(Collections.singleton(userRole));
	return userRepository.save(user);
	
	}

	@Override
	public List<User> getUsers() {
		return userRepository.findAll();
	}

	@Override
	@Transactional
	public void deleteUser(String email) {
		User theUser = getUser(email);
		
		if(theUser != null) {
			 theUser.getRoles().clear();
			 
			  
			userRepository.deleteByEmail(email);
		}
		
		
	}

	@Override
	public User getUser(String email) {
		return userRepository.findByEmail(email)
				.orElseThrow(()-> new UserNotFoundException("user not found"));
				
	}

}
