package com.viruchith.recruitmentpals.jwt;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AppAuthenticationManager implements AuthenticationManager {

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String[] userAndType = (authentication.getPrincipal() + "").split("\\|");
	    
		String username = userAndType[0];
		
		String type = userAndType[1];
		
		String password = authentication.getCredentials() + "";
		
		UserDetails user = userDetailsService.loadUserByUsername(authentication.getPrincipal()+"");
		
		
		if(user==null) {
			throw new BadCredentialsException("Incorrect Username (or) Email !");
		}
		
		 System.out.println(authentication.getPrincipal()+ " : "+authentication.getCredentials());
		 
		 System.out.println(passwordEncoder.encode(password));
		 
		 System.out.println(passwordEncoder.matches(password,user.getPassword()));
		 
		 if (password.isEmpty() || !passwordEncoder.matches(password,user.getPassword())) {
			        throw new BadCredentialsException("Incorrect Password");
		 }
		 
		
		return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(),authentication.getAuthorities());
	}

}
