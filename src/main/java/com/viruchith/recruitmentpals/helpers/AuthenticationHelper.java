package com.viruchith.recruitmentpals.helpers;

import java.util.Map;

import javax.xml.soap.Detail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.viruchith.recruitmentpals.models.AdminUser;
import com.viruchith.recruitmentpals.services.AdminUserService;
import com.viruchith.recruitmentpals.services.PlacementCoordiantorService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Service
public class AuthenticationHelper {
	
	@Autowired
	private AdminUserService adminUserService;
	
	@Autowired
	private PlacementCoordiantorService placementCoordiantorService;
	
	
	private Map<String,Object> details;
	private Authentication authentication;

	public AuthenticationHelper(Authentication authentication) {
		super();
		this.authentication = authentication;
		this.details = (Map<String, Object>) authentication.getDetails();
	}
	
	public String getUserType() {
		return details.get("userType")+"";
	}
	
	public String getUsername(){
		return authentication.getName();
	}
	
	public Object getUser(){
		String userType = details.get("userType")+"";
		if(userType.equals(UserTypes.ADMIN)) {
			return adminUserService.findFirstByUsername(getUsername());
		}else if(userType.equals(UserTypes.COORDINATOR)) {
			return placementCoordiantorService.findFirstByEmail(getUsername());
		}
		
		return null;
	}
	
	public void setAuthentication(SecurityContext securityContext) {
		this.authentication = securityContext.getAuthentication();
		this.details = (Map<String, Object>) authentication.getDetails();
	}
}
