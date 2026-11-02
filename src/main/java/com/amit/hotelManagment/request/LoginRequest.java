package com.amit.hotelManagment.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class LoginRequest {

	
	private String email;
	private String password;
}
