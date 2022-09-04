package com.viruchith.recruitmentpals.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.viruchith.recruitmentpals.services.PlacementCoordiantorService;

@RestController
@RequestMapping("/coordinator")
public class PlacementCoordiantorController {
	
	@Autowired
	private PlacementCoordiantorService placementCoordiantorService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtil jwtUtil;
		
	
	@Autowired
	private AppUserDetailsService appUserDetailsService;
	
	
	@Autowired
	private AuthenticationHelper authenticationHelper;
	
	@PostMapping("")
	public ResponseEntity<StandardResponse> create(@RequestBody @Valid PlacementCoordinator placementCoordinator) {
		AuthenticationHelper authenticationHelper  = new AuthenticationHelper(SecurityContextHolder.getContext().getAuthentication());
		
		if(authenticationHelper.getUserType().equals(UserTypes.ADMIN)) {
			PlacementCoordinator coordiantor = placementCoordiantorService.findFirstByEmail(placementCoordinator.getEmail());
			
			if(coordiantor!=null) {
				return ResponseEntity.ok(new StandardResponse(false,String.format(StandardMessages.USER_ALREADY_EXISTS,UserTypes.COORDINATOR,"username",placementCoordinator.getEmail())));
			}
			
			StandardResponse response = new StandardResponse(false,String.format(StandardMessages.USER_CREATED_SUCCESSFULLY, UserTypes.COORDINATOR));
			response.addData("coordinator", placementCoordiantorService.createPlacementCoordinator(placementCoordinator));
			return ResponseEntity.ok(response);
		}
		
		return ResponseEntity.ok(new StandardResponse(false,StandardMessages.ADMIN_ONLY));
	}
	
	@PostMapping("/login")
	public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody JwtAuthenticationRequest jwtAuthenticationRequest){
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtAuthenticationRequest.getUsername()+"|"+UserTypes.COORDINATOR, jwtAuthenticationRequest.getPassword()));
		} catch (BadCredentialsException e) {
			return ResponseEntity.ok(new JwtAuthenticationResponse(false, e.getMessage(), null));
		}

		UserDetails userDetails = appUserDetailsService.loadUserByTypeAndUsername(jwtAuthenticationRequest.getUsername(),UserTypes.COORDINATOR);

		String token = jwtUtil.generateToken(userDetails,UserTypes.COORDINATOR);
		
		
		return ResponseEntity.ok(new JwtAuthenticationResponse(true, "Successful !", token));
	}
	
	
	@PostMapping("/password")
	public ResponseEntity<StandardResponse> changePassword(@RequestBody @Valid PasswordChangeRequest passwordChangeRequest){
		authenticationHelper.setAuthentication(SecurityContextHolder.getContext());
		
		if(authenticationHelper.getUserType().equals(UserTypes.ADMIN)) {
			PlacementCoordinator placementCoordinator = (PlacementCoordinator)authenticationHelper.getUser();

			if(placementCoordiantorService.validatePassword(placementCoordinator,passwordChangeRequest.getPassword())) {
				String encoded = placementCoordiantorService.encodePassword(passwordChangeRequest.getNewPassword());
				placementCoordinator.setPassword(encoded);				
				placementCoordiantorService.savePlacementCoordinator(placementCoordinator);
				return ResponseEntity.ok(new StandardResponse(true,"Password updated successfully !"));
			}else{
				return ResponseEntity.ok(new StandardResponse(false,"Incorrect Current Password !"));
			}
		}
		
		return ResponseEntity.ok(new StandardResponse(false,StandardMessages.ADMIN_ONLY));
	}
	
	@GetMapping("/hello")
	public ResponseEntity<?> hello() {
		AuthenticationHelper authenticationHelper  = new AuthenticationHelper(SecurityContextHolder.getContext().getAuthentication());
		
		PlacementCoordinator placementCoordinator = placementCoordiantorService.findFirstByEmail(authenticationHelper.getUsername());
		
		Map<Object,Object> map = new HashMap<>();
		map.put("success", true);
		map.put("message", "Hello World !");
		map.put("coordinator",placementCoordinator);
		return ResponseEntity.ok(map);
	}
}
