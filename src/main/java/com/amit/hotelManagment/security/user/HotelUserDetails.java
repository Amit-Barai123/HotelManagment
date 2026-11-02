package com.amit.hotelManagment.security.user;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.amit.hotelManagment.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HotelUserDetails implements UserDetails {
	
	private Long id;
	private String email;
	private String password;
	private Collection<GrantedAuthority> authorites;
	
	public static HotelUserDetails buildUserDetails(User user) {
		List<GrantedAuthority> authorites = user.getRoles()
				.stream()
				.map(role -> new SimpleGrantedAuthority(role.getName())
						).collect(Collectors.toList());
		
		return new HotelUserDetails(
				user.getId(),
				user.getEmail(),
				user.getPassword(),
				authorites
				);
	}
	

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorites;
	}

	@Override
	public @Nullable String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}

}
