package com.amit.hotelManagment.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {

	private Long id;
	private String email;
	private String token;
	private List<String> roles;
	
	
	
}
