package com.viruchith.recruitmentpals.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viruchith.recruitmentpals.helpers.AuthenticationHelper;
import com.viruchith.recruitmentpals.helpers.PasswordChangeRequest;
import com.viruchith.recruitmentpals.helpers.StandardMessages;
import com.viruchith.recruitmentpals.helpers.StandardResponse;
import com.viruchith.recruitmentpals.helpers.UserTypes;
import com.viruchith.recruitmentpals.jwt.AppUserDetailsService;
import com.viruchith.recruitmentpals.jwt.JwtAuthenticationRequest;
import com.viruchith.recruitmentpals.jwt.JwtAuthenticationResponse;
import com.viruchith.recruitmentpals.jwt.JwtUtil;
import com.viruchith.recruitmentpals.models.AdminUser;
import com.viruchith.recruitmentpals.models.PlacementCoordinator;
import com.viruchith.recruitmentpals.services.AdminUserService;

@RestController
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private AdminUserService adminUserService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private AppUserDetailsService appUserDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private AuthenticationHelper authenticationHelper;
			
	@PostMapping("/login")
	public JwtAuthenticationResponse login(@RequestBody @Valid JwtAuthenticationRequest jwtAuthenticationRequest) {
		
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtAuthenticationRequest.getUsername()+"|"+UserTypes.ADMIN, jwtAuthenticationRequest.getPassword()));
		} catch (BadCredentialsException e) {
			return new JwtAuthenticationResponse(false, e.getMessage(), null);
		}
		
		UserDetails userDetails = appUserDetailsService.loadUserByTypeAndUsername(jwtAuthenticationRequest.getUsername(),UserTypes.ADMIN);
		
		String token = jwtUtil.generateToken(userDetails,UserTypes.ADMIN);
		
		return new JwtAuthenticationResponse(true, "Successful !", token);
	}
	
	@PostMapping("/password")
	public ResponseEntity<StandardResponse> changePassword(@RequestBody @Valid PasswordChangeRequest passwordChangeRequest){
		authenticationHelper.setAuthentication(SecurityContextHolder.getContext());
		
		if(authenticationHelper.getUserType().equals(UserTypes.ADMIN)) {
			AdminUser adminUser = (AdminUser)authenticationHelper.getUser();

			if(adminUserService.validatePassword(adminUser,passwordChangeRequest.getPassword())) {
				String encoded = adminUserService.encodePassword(passwordChangeRequest.getNewPassword());
				adminUser.setPassword(encoded);				
				adminUserService.saveAdminUser(adminUser);
				return ResponseEntity.ok(new StandardResponse(true,"Password updated successfully !"));
			}else{
				return ResponseEntity.ok(new StandardResponse(false,"Incorrect Current Password !"));
			}
		}
		
		return ResponseEntity.ok(new StandardResponse(false,StandardMessages.ADMIN_ONLY));
	}
	
	
	@DeleteMapping("/{id}")
	public ResponseEntity<StandardResponse> deletePlacementCoordinator(@PathVariable long id){
		authenticationHelper.setAuthentication(SecurityContextHolder.getContext());
		
		if(authenticationHelper.getUserType().equals(UserTypes.ADMIN)) {
			Optional<AdminUser> optional = adminUserService.findFirstById(id);
			if(!optional.isPresent()) {
				return ResponseEntity.ok(new StandardResponse(false,String.format("%S user with ID \"%d\" does not exist !",UserTypes.ADMIN,id)));
			}else {
				AdminUser adminUser = optional.get();
				if(adminUser.getUsername().equals(authenticationHelper.getUsername())){
					return ResponseEntity.ok(new StandardResponse(false,"You cannot delete yourself !"));
				}
				adminUserService.deleteAdminUser(adminUser);
				return ResponseEntity.ok(new StandardResponse(false,String.format("%S user with ID \"%d\" was deleted successfully !",UserTypes.ADMIN,id)));				
			}
		}
		
		return ResponseEntity.ok(new StandardResponse(false,StandardMessages.ADMIN_ONLY));
	}

	
	
	@GetMapping("/hello")
	public ResponseEntity<?> hello() {
//		
//		adminUserService.deleteById(2);
//		
//		AdminUser user = new AdminUser();
//		user.setUsername("admin");
//		user.setPassword("admin123");
		
		authenticationHelper.setAuthentication(SecurityContextHolder.getContext());
		AdminUser user = (AdminUser)authenticationHelper.getUser();
		
//		AdminUser user = adminUserService.findFirstByUsername(authenticationHelper.getUsername());

		Map<Object,Object> map = new HashMap<>();
		map.put("success", true);
		map.put("message", "Hello World from admin !");
		map.put("user", user);
//		map.put("admin", adminUserService.saveAdminUser(user));
		return ResponseEntity.ok(map);
	}
}
