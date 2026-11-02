package com.amit.hotelManagment.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.amit.hotelManagment.entity.Role;
import com.amit.hotelManagment.entity.User;
import com.amit.hotelManagment.exception.RoleAlreadyExistsException;
import com.amit.hotelManagment.exception.UserAlreadyExistsException;
import com.amit.hotelManagment.exception.UserNotFoundException;
import com.amit.hotelManagment.repository.RoleRepository;
import com.amit.hotelManagment.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {

	private final RoleRepository roleRepository;
	private final UserRepository userRepository;
	
	
	@Override
	public List<Role> getRoles() {
		return roleRepository.findAll();
	}

	@Override
	public Role createRole(Role role) {
		String roleName = "ROLE_"+role.getName().toUpperCase();
		
		Role therole = new Role(roleName);
		if(roleRepository.existsByName(roleName)) {
			throw new RoleAlreadyExistsException(role.getName() + "role already exists");
		}
		
		return roleRepository.save(therole);
	}

	@Override
	public void deleteRole(Long id) {
		this.removeAllUsersFromRole(id);
		roleRepository.deleteById(id);
		
	}

	@Override
	public Role findByName(String name) {
		return roleRepository.findByName(name).get();
	}

	@Override
	public User removeUserFromRole(Long userId, Long roleId) {
		Optional<User> user = userRepository.findById(userId);
		Optional<Role> role = roleRepository.findById(roleId);
		
		if(role.isPresent() && role.get().getUsers().contains(user.get())) {
			role.get().removeUserFromRole(user.get());
			roleRepository.save(role.get());
			return user.get();
		}
		
		throw new UserNotFoundException("user not found");
		
		}

	@Override
	public User assignRoleToUser(Long userId, Long roleId) {
		Optional<User> user = userRepository.findById(userId);
		Optional<Role> role = roleRepository.findById(roleId);
		
		if(user.isPresent() && user.get().getRoles().contains(role.get())) {
			throw new UserAlreadyExistsException(
					user.get().getFirstName() + "is already assigned with the role" + role.get().getName()
					);
		}
			if(role.isPresent()) {
				role.get().assignRoleToUser(user.get());
				roleRepository.save(role.get());
			}
			
			return user.get();
		}
		
		
		
	

	@Override
	public Role removeAllUsersFromRole(Long roleId) {
		Optional<Role> role = roleRepository.findById(roleId);
		role.ifPresent(Role::removeAllUsersFromRole);
		 return roleRepository.save(role.get());
	}

}
