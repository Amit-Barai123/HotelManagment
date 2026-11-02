package com.amit.hotelManagment.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amit.hotelManagment.entity.User;
import com.amit.hotelManagment.request.LoginRequest;
import com.amit.hotelManagment.response.JwtResponse;
import com.amit.hotelManagment.security.jwt.JwtService;
import com.amit.hotelManagment.security.user.HotelUserDetails;
import com.amit.hotelManagment.service.IUserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final IUserService userService;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	
	@PostMapping("/register-user")
	public ResponseEntity<?> registerUser(@RequestBody User user){
		try {
			userService.registerUser(user);
			return ResponseEntity.ok("Registration sucessfull");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest){
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
				);
		
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String jwt = jwtService.generateToken((UserDetails) authentication.getPrincipal());
		HotelUserDetails userDetails = (HotelUserDetails) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorites().stream()
				.map(grant -> grant.getAuthority()).toList();
		
		return ResponseEntity.ok(new JwtResponse(
				userDetails.getId(),
				userDetails.getEmail(),
				jwt,
				roles
				));
	}
	
}
