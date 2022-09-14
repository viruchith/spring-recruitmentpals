package com.viruchith.recruitmentpals.controllers;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viruchith.recruitmentpals.helpers.AuthenticationHelper;
import com.viruchith.recruitmentpals.helpers.StandardMessages;
import com.viruchith.recruitmentpals.helpers.StandardResponse;
import com.viruchith.recruitmentpals.helpers.UserTypes;
import com.viruchith.recruitmentpals.helpers.evaluators.ActionEvaluator;
import com.viruchith.recruitmentpals.jwt.AppUserDetailsService;
import com.viruchith.recruitmentpals.models.AcademicGroup;
import com.viruchith.recruitmentpals.models.Branch;
import com.viruchith.recruitmentpals.models.Degree;
import com.viruchith.recruitmentpals.services.AcademicGroupService;
import com.viruchith.recruitmentpals.services.BranchService;
import com.viruchith.recruitmentpals.services.DegreeService;

@RestController
@RequestMapping("/util")
public class UtilitiesController {
		
	@Autowired
	private AppUserDetailsService appUserDetailsService;
	
	@Autowired
	private DegreeService degreeService;
	
	@Autowired
	private BranchService branchService;
	
	@Autowired
	private AcademicGroupService academicGroupService;
	
	
	@PostMapping("/academicgroup")
	public ResponseEntity<StandardResponse> createAcademicGroup(@RequestBody @Valid AcademicGroup academicGroup){
		AuthenticationHelper authenticationHelper = new AuthenticationHelper(SecurityContextHolder.getContext().getAuthentication());
		//ALLOW only ADMIN and COORDINATOR to add academicgroup
		if(ActionEvaluator.isAdmin(authenticationHelper) || ActionEvaluator.isCoordinator(authenticationHelper)) {
			
			Optional<Degree> degreeOptional = degreeService.findFirstById(academicGroup.getDegree().getId());
			
			Optional<Branch> branchOptional = branchService.findFirstById(academicGroup.getBranch().getId());
			
			if(!degreeOptional.isPresent()) {
				return ResponseEntity.ok(new StandardResponse(false,"Degree with id : "+academicGroup.getDegree().getId()+" not found !"));
			}
			
			if(!branchOptional.isPresent()){
				return ResponseEntity.ok(new StandardResponse(false,"Branch with id : "+academicGroup.getBranch().getId()+" not found !"));
			}
			
			academicGroup.setDegree(degreeOptional.get());
			
			academicGroup.setBranch(branchOptional.get());
			
			try {
				academicGroup = academicGroupService.saveAcademicGroup(academicGroup);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				return ResponseEntity.ok(new StandardResponse(false,String.format(StandardMessages.ENTITY_ALREADY_EXISTS,"AcademicGroup","attributes",academicGroup.toString())));
			}
			
			StandardResponse response = new StandardResponse(true,"Academic Group created successfully !");
			response.addData("academicGroup", academicGroup);
			
			return ResponseEntity.ok(response);
		}
		
		return ResponseEntity.ok(new StandardResponse(false,StandardMessages.ADMIN_AND_COORDINATOR_ONLY));

	}
	
	@GetMapping("/academicgroup")
	public ResponseEntity<StandardResponse> fetchAllAcademicGroups() {
		StandardResponse response = new StandardResponse(true,"Academic Groups fetched successfully !");
		response.addData("academicGroups", academicGroupService.getAllAcademicGroups());
		return ResponseEntity.ok(response);
	}
	
	
	
	
	@PostMapping("/degree")
	public ResponseEntity<StandardResponse> createDegree(@RequestBody @Valid Degree degree){
		AuthenticationHelper authenticationHelper = new AuthenticationHelper(SecurityContextHolder.getContext().getAuthentication());
		//ALLOW only ADMIN and COORDINATOR to add degree
		if(ActionEvaluator.isAdmin(authenticationHelper) || ActionEvaluator.isCoordinator(authenticationHelper)) {
			try {
				degree = degreeService.saveDegree(degree);
			} catch (Exception e) {
				return ResponseEntity.ok(new StandardResponse(false,String.format(StandardMessages.ENTITY_ALREADY_EXISTS,"Degree","title",degree.getTitle())));
			}
			
			StandardResponse response =  new StandardResponse(true,"Degree created successfully !");
			response.addData("degree", degree);
			return ResponseEntity.ok(response);
		}
		
		return ResponseEntity.ok(new StandardResponse(false,StandardMessages.ADMIN_AND_COORDINATOR_ONLY));
	}
	
	@GetMapping("/degree")
	public ResponseEntity<StandardResponse> fetchAllDegrees() {
		StandardResponse response = new StandardResponse(true,"Degrees fetched successfully !");
		response.addData("degrees", degreeService.getAllDegrees());
		return ResponseEntity.ok(response);
	}
	
	
	@PostMapping("/branch")
	public ResponseEntity<StandardResponse> createBranch(@RequestBody @Valid Branch branch){
		AuthenticationHelper authenticationHelper = new AuthenticationHelper(SecurityContextHolder.getContext().getAuthentication());
		//ALLOW only ADMIN and COORDINATOR to add degree
		if(authenticationHelper.getUserType().equals(UserTypes.ADMIN) || authenticationHelper.getUserType().equals(UserTypes.COORDINATOR)) {
			try {
				branch = branchService.saveBranch(branch);
			} catch (Exception e) {
				return ResponseEntity.ok(new StandardResponse(false,String.format(StandardMessages.ENTITY_ALREADY_EXISTS,"Branch","title",branch.getTitle())));
			}
			
			StandardResponse response =  new StandardResponse(true,"Branch created successfully !");
			response.addData("branch", branch);
			return ResponseEntity.ok(response);
		}
		
		return ResponseEntity.ok(new StandardResponse(false,StandardMessages.ADMIN_AND_COORDINATOR_ONLY));
	}
	
	@GetMapping("/branch")
	public ResponseEntity<StandardResponse> fetchAllBranches() {
		StandardResponse response = new StandardResponse(true,"Branches fetched successfully !");
		response.addData("branches", branchService.getAllBranches());
		return ResponseEntity.ok(response);
	}

	
}
