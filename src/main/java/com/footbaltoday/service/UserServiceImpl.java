package com.footbaltoday.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.footbaltoday.entity.User;
import com.footbaltoday.repository.UserRepository;


@Service
public class UserServiceImpl {

	@Autowired
    private UserRepository userRepository;
	
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    public void save(User user) {
       user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setCreatedAt(new Date());
		user.setUpdatedAt(new Date());
        System.out.println("save::"+user);
        userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
   }
    
    public boolean checkIfEmailExist(String email) {
    	User user = findByEmail(email);
    	if(user != null) {
    		return true;
    	}
    	return false;
    }
}
