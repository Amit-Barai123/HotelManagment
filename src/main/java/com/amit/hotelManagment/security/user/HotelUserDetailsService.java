package com.amit.hotelManagment.security.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.amit.hotelManagment.entity.User;
import com.amit.hotelManagment.exception.UserNotFoundException;
import com.amit.hotelManagment.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HotelUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		User user = userRepository.findByEmail(email) 
				.orElseThrow(()-> new UserNotFoundException("User not found"));
		
		return HotelUserDetails.buildUserDetails(user);
	}

}
