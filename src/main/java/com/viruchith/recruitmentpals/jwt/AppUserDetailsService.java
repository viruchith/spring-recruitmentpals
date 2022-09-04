package com.viruchith.recruitmentpals.jwt;

import java.util.ArrayList;

import javax.transaction.UserTransaction;

import org.hibernate.usertype.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.viruchith.recruitmentpals.helpers.StandardMessages;
import com.viruchith.recruitmentpals.helpers.UserTypes;
import com.viruchith.recruitmentpals.models.AdminUser;
import com.viruchith.recruitmentpals.models.PlacementCoordinator;
import com.viruchith.recruitmentpals.repos.AdminUserRepository;
import com.viruchith.recruitmentpals.repos.PlacementCoordinatorRepository;
import com.viruchith.recruitmentpals.services.AdminUserService;

@Service
public class AppUserDetailsService implements UserDetailsService {
	
	@Autowired
	private AdminUserRepository adminUserRepository;
	
	@Autowired
	private PlacementCoordinatorRepository placementCoordinatorRepository;
	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		String[] userAndType = username.split("\\|");
	    
		username = userAndType[0];
		
		String userType = userAndType[1];
		
		if(userType.equals(UserTypes.ADMIN)) {
			AdminUser adminUser = adminUserRepository.findFirstByUsername(username);
			
			if(adminUser == null) {
				throw new BadCredentialsException(String.format(StandardMessages.USER_NOT_FOUND, userType,username));
			}
			
			return new User(adminUser.getUsername(),adminUser.getPassword(), new ArrayList<>());
		
		}else if(userType.equals(UserTypes.COORDINATOR)) {
			PlacementCoordinator placementCoordinator = placementCoordinatorRepository.findFirstByEmail(username);
			
			if(placementCoordinator==null) {
				throw new BadCredentialsException(String.format(StandardMessages.USER_NOT_FOUND, userType,username));
			}
			
			return new User(placementCoordinator.getEmail(),placementCoordinator.getPassword(), new ArrayList<>());

		}
		
		return null;
	}
	
	public UserDetails loadUserByTypeAndUsername(String username,String Type) {
		return loadUserByUsername(username+"|"+Type);
	}

}
