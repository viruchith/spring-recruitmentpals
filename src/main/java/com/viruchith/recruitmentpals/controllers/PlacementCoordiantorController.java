package com.viruchith.recruitmentpals.controllers;

import java.util.HashMap;
import java.util.List;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viruchith.recruitmentpals.helpers.AuthenticationHelper;
import com.viruchith.recruitmentpals.helpers.PasswordChangeRequest;
import com.viruchith.recruitmentpals.helpers.StandardMessages;
import com.viruchith.recruitmentpals.helpers.StandardResponse;
import com.viruchith.recruitmentpals.helpers.UserTypes;
import com.viruchith.recruitmentpals.helpers.evaluators.ActionEvaluator;
import com.viruchith.recruitmentpals.jwt.AppUserDetailsService;
import com.viruchith.recruitmentpals.jwt.JwtAuthenticationRequest;
import com.viruchith.recruitmentpals.jwt.JwtAuthenticationResponse;
import com.viruchith.recruitmentpals.jwt.JwtUtil;
import com.viruchith.recruitmentpals.models.AcademicGroup;
import com.viruchith.recruitmentpals.models.PlacementCoordinator;
import com.viruchith.recruitmentpals.models.projections.NoPasswordPlacementCoordinator;
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
	
	@Autowired
	private ActionEvaluator actionEvaluator;
	
	@PostMapping("")
	public ResponseEntity<StandardResponse> create(@RequestBody @Valid PlacementCoordinator placementCoordinator) {
		authenticationHelper.setAuthentication(SecurityContextHolder.getContext());
		
		if(ActionEvaluator.isAdmin(authenticationHelper)) {
			PlacementCoordinator coordinator = placementCoordiantorService.findFirstByEmail(placementCoordinator.getEmail());
			
			if(coordinator!=null) {
				return ResponseEntity.ok(new StandardResponse(false,String.format(StandardMessages.USER_ALREADY_EXISTS,UserTypes.COORDINATOR,"username",placementCoordinator.getEmail())));
			}
			
			StandardResponse response = new StandardResponse(false,String.format(StandardMessages.USER_CREATED_SUCCESSFULLY, UserTypes.COORDINATOR));
			response.addData("coordinator", placementCoordiantorService.createPlacementCoordinator(placementCoordinator));
			return ResponseEntity.ok(response);
		}
		
		return ResponseEntity.ok(new StandardResponse(false,StandardMessages.ADMIN_ONLY));
	}
	
	
	
	@GetMapping("")
	public ResponseEntity<StandardResponse> fetchAllPlacementCoordinators(){
		authenticationHelper.setAuthentication(SecurityContextHolder.getContext());
		if(ActionEvaluator.isAdmin(authenticationHelper)){
			List<NoPasswordPlacementCoordinator> placementCoordinators = placementCoordiantorService.findAllNoPassword();
			StandardResponse response = new StandardResponse(true,"All "+UserTypes.COORDINATOR+" users fetched successfully !");
			response.addData("coordinators", placementCoordinators);
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.ok(new StandardResponse(false,StandardMessages.ADMIN_ONLY));
	}
	
	
	@GetMapping("{coordinatorId}")
	public ResponseEntity<StandardResponse> fetchPlacementCoordinatorById(@PathVariable long coordinatorId){
		authenticationHelper.setAuthentication(SecurityContextHolder.getContext());
		if(ActionEvaluator.isAdmin(authenticationHelper)){
			NoPasswordPlacementCoordinator placementCoordinator = placementCoordiantorService.findFirstByIdNoPassword(coordinatorId);
			StandardResponse response = new StandardResponse(true,"All "+UserTypes.COORDINATOR+" users fetched successfully !");
			response.addData("coordinator", placementCoordinator);
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
		
		PlacementCoordinator placementCoordinator = placementCoordiantorService.findFirstByEmail(jwtAuthenticationRequest.getUsername());
		
		String token = jwtUtil.generateToken(userDetails,UserTypes.COORDINATOR,placementCoordinator.getId());
		
		return ResponseEntity.ok(new JwtAuthenticationResponse(true, "Successful !", token));
	}
		
	
	@PostMapping("/{coordinatorId}/password")
	public ResponseEntity<StandardResponse> changePassword(@PathVariable long coordinatorId,@RequestBody @Valid PasswordChangeRequest passwordChangeRequest){
		//TODO abstract AdminPlacementCoordinatorStudentPerformsStudentAction
		
		authenticationHelper.setAuthentication(SecurityContextHolder.getContext());
		actionEvaluator.setAuthenticationHelper(authenticationHelper);
		
		if(actionEvaluator.isUserAuthorized(UserTypes.ADMIN,UserTypes.COORDINATOR)){
			if(actionEvaluator.canAdminOrCoordinatorPerformCoordinatorAction(coordinatorId)) {
				Optional<PlacementCoordinator> placementCoordinatorOptional = placementCoordiantorService.findFirstById(coordinatorId);
				if (placementCoordinatorOptional.isPresent()) {
					PlacementCoordinator placementCoordinator = placementCoordinatorOptional.get();
					if( ActionEvaluator.isCoordinator(authenticationHelper) && !placementCoordiantorService.validatePassword(placementCoordinator,passwordChangeRequest.getPassword())) {
						return ResponseEntity.ok(new StandardResponse(false,"Incorrect Current Password !"));
					}else{
						String encoded = placementCoordiantorService.encodePassword(passwordChangeRequest.getNewPassword());
						placementCoordinator.setPassword(encoded);				
						placementCoordiantorService.savePlacementCoordinator(placementCoordinator);
						return ResponseEntity.ok(new StandardResponse(true,"Password updated successfully !"));
					}
				}
			}
		}
		
		return ResponseEntity.ok(actionEvaluator.getErrorResponse());

	}
	
	
	//TODO Update Coordinator
	@DeleteMapping("/{coordinatorId}")
	public ResponseEntity<StandardResponse> deletePlacementCoordinator(@PathVariable long coordintorId){
		authenticationHelper.setAuthentication(SecurityContextHolder.getContext());
		
		if(ActionEvaluator.isAdmin(authenticationHelper)) {
			Optional<PlacementCoordinator> optional = placementCoordiantorService.findFirstById(coordintorId);
			if(!optional.isPresent()) {
				return ResponseEntity.ok(new StandardResponse(false,String.format("%S user with ID \"%d\" does not exist !",UserTypes.COORDINATOR,coordintorId)));
			}else {
				placementCoordiantorService.deletePlacementCoordinator(optional.get());
				return ResponseEntity.ok(new StandardResponse(true,String.format("%S user with ID \"%d\" was deleted successfully !",UserTypes.COORDINATOR,coordintorId)));				
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
