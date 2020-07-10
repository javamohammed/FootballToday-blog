package com.footbaltoday.service;


import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.footbaltoday.entity.User;
import com.footbaltoday.repository.UserRepository;


@Service
public class UserDetailsServiceImpl  implements UserDetailsService {

	@Autowired
    private UserRepository userRepository;
	
	private UserDetailsImpl userDetailsImpl;
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		//System.out.println("===="+email); 
		if (email.trim().isEmpty()) {
			throw new UsernameNotFoundException("Email is empty");
		}
 
		User user = userRepository.findByEmail(email);
	        if (user == null) throw new UsernameNotFoundException(email);
	        
	        userDetailsImpl = new UserDetailsImpl(user);
	        return userDetailsImpl;
	}
	
	public UserDetailsImpl getUserDetails() {
		return this.userDetailsImpl;
	}

}
