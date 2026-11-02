package com.amit.hotelManagment.service;

import java.util.List;

import com.amit.hotelManagment.entity.User;

public interface IUserService {

	User registerUser(User user);
	List<User> getUsers();
	void deleteUser(String email);
	User getUser(String email);
	
	
}
