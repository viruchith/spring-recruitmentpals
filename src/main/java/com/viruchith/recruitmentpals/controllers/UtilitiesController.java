package com.viruchith.recruitmentpals.controllers;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viruchith.recruitmentpals.dtos.AcademicGroupUpdateDTO;
import com.viruchith.recruitmentpals.helpers.AuthenticationHelper;
import com.viruchith.recruitmentpals.helpers.StandardMessages;
import com.viruchith.recruitmentpals.helpers.StandardResponse;
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
	public ResponseEntity<StandardResponse> createAcademicGroup(@RequestBody @Valid AcademicGroup academicGroup) {
		AuthenticationHelper authenticationHelper = new AuthenticationHelper(
				SecurityContextHolder.getContext().getAuthentication());
		// ALLOW only ADMIN and COORDINATOR to add academicgroup
		if (ActionEvaluator.isAdmin(authenticationHelper) || ActionEvaluator.isCoordinator(authenticationHelper)) {

			Optional<Degree> degreeOptional = degreeService.findFirstById(academicGroup.getDegree().getId());

			Optional<Branch> branchOptional = branchService.findFirstById(academicGroup.getBranch().getId());

			if (!degreeOptional.isPresent()) {
				return ResponseEntity.ok(new StandardResponse(false,
						"Degree with id : " + academicGroup.getDegree().getId() + " not found !"));
			}

			if (!branchOptional.isPresent()) {
				return ResponseEntity.ok(new StandardResponse(false,
						"Branch with id : " + academicGroup.getBranch().getId() + " not found !"));
			}

			academicGroup.setDegree(degreeOptional.get());

			academicGroup.setBranch(branchOptional.get());

			try {
				academicGroup = academicGroupService.saveAcademicGroup(academicGroup);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				return ResponseEntity
						.ok(new StandardResponse(false, String.format(StandardMessages.ENTITY_ALREADY_EXISTS,
								"AcademicGroup", "attributes", academicGroup.toString())));
			}

			StandardResponse response = new StandardResponse(true, "Academic Group created successfully !");
			response.addData("academicGroup", academicGroup);

			return ResponseEntity.ok(response);
		}

		return ResponseEntity.ok(new StandardResponse(false, StandardMessages.ADMIN_AND_COORDINATOR_ONLY));

	}

	@GetMapping("/academicgroup")
	public ResponseEntity<StandardResponse> fetchAllAcademicGroups() {
		StandardResponse response = new StandardResponse(true, "Academic Groups fetched successfully !");
		response.addData("academicGroups", academicGroupService.getAllAcademicGroups());
		return ResponseEntity.ok(response);
	}

	@GetMapping("/academicgroup/{academicGroupId}")
	public ResponseEntity<StandardResponse> fetchAcademicGropuById(@PathVariable long academicGroupId) {
		StandardResponse response = new StandardResponse(true, "AcademicGroup fetched successfully !");
		Optional<AcademicGroup> academicGroupOptional = academicGroupService.findFirstById(academicGroupId);
		if (academicGroupOptional.isPresent()) {
			response.addData("academicGroup", academicGroupOptional.get());
		} else {
			response.setSuccess(false);
			response.setMessage("AcademicGroup with id : " + academicGroupId + " does not exist !");
			response.addData("academicGroup", null);
		}

		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/academicgroup/{academicGroupId}")
	public ResponseEntity<StandardResponse> deleteAcademicGroupById(@PathVariable long academicGroupId) {
		AuthenticationHelper authenticationHelper = new AuthenticationHelper(
				SecurityContextHolder.getContext().getAuthentication());

		if (ActionEvaluator.isAdmin(authenticationHelper)) {
			academicGroupService.deleteById(academicGroupId);
			return ResponseEntity.ok(new StandardResponse(true, "AcademicGroup deleted successfully !"));
		} else {
			return ResponseEntity.ok(new StandardResponse(false, StandardMessages.ADMIN_ONLY));
		}
	}

	@PutMapping("/academicgroup/{academicGroupId}")
	public ResponseEntity<StandardResponse> updateAcademicGroupById(@PathVariable long academicGroupId,
			@RequestBody @Valid AcademicGroupUpdateDTO academicGroupUpdateDTO) {
		AuthenticationHelper authenticationHelper = new AuthenticationHelper(
				SecurityContextHolder.getContext().getAuthentication());
		if (ActionEvaluator.isAdmin(authenticationHelper)) {

			Optional<AcademicGroup> academicGroupOptional = academicGroupService.findFirstById(academicGroupId);
			if (academicGroupOptional.isPresent()) {
				AcademicGroup academicGroupToUpdate = academicGroupOptional.get();

				Optional<Degree> degreeOptional = degreeService.findFirstById(academicGroupUpdateDTO.getDegreeId());

				Optional<Branch> branchOptional = branchService.findFirstById(academicGroupUpdateDTO.getBranchId());

				if (!degreeOptional.isPresent()) {
					return ResponseEntity.ok(new StandardResponse(false,
							"Degree with id : " + academicGroupUpdateDTO.getDegreeId() + " was not found !"));
				}

				if (!branchOptional.isPresent()) {
					return ResponseEntity.ok(new StandardResponse(false,
							"Branch with id : " + academicGroupUpdateDTO.getBranchId() + " was not found !"));
				}

				academicGroupToUpdate.setBatchStartYear(academicGroupToUpdate.getBatchStartYear());
				academicGroupToUpdate.setBatchEndYear(academicGroupToUpdate.getBatchEndYear());
				academicGroupToUpdate.setDegree(degreeOptional.get());
				academicGroupToUpdate.setBranch(branchOptional.get());

				AcademicGroup academicGroup = academicGroupService.saveAcademicGroup(academicGroupToUpdate);

				StandardResponse response = new StandardResponse(true, "AcademicGroup updated successfully !");
				response.addData("academicGroup", academicGroup);
				return ResponseEntity.ok(response);
			}
			return ResponseEntity
					.ok(new StandardResponse(false, "AcademicGroup with id : " + academicGroupId + " was not found !"));
		} else {
			return ResponseEntity.ok(new StandardResponse(false, StandardMessages.ADMIN_ONLY));
		}
	}

	@PostMapping("/degree")
	public ResponseEntity<StandardResponse> createDegree(@RequestBody @Valid Degree degree) {
		AuthenticationHelper authenticationHelper = new AuthenticationHelper(
				SecurityContextHolder.getContext().getAuthentication());
		// ALLOW only ADMIN and COORDINATOR to add degree
		if (ActionEvaluator.isAdmin(authenticationHelper) || ActionEvaluator.isCoordinator(authenticationHelper)) {
			try {
				degree = degreeService.saveDegree(degree);
			} catch (Exception e) {
				return ResponseEntity.ok(new StandardResponse(false,
						String.format(StandardMessages.ENTITY_ALREADY_EXISTS, "Degree", "title", degree.getTitle())));
			}

			StandardResponse response = new StandardResponse(true, "Degree created successfully !");
			response.addData("degree", degree);
			return ResponseEntity.ok(response);
		}

		return ResponseEntity.ok(new StandardResponse(false, StandardMessages.ADMIN_AND_COORDINATOR_ONLY));
	}

	@GetMapping("/degree")
	public ResponseEntity<StandardResponse> fetchAllDegrees() {
		StandardResponse response = new StandardResponse(true, "Degrees fetched successfully !");
		response.addData("degrees", degreeService.getAllDegrees());
		return ResponseEntity.ok(response);
	}

	@GetMapping("/degree/{degreeId}")
	public ResponseEntity<StandardResponse> fetchDegreeById(@PathVariable long degreeId) {
		StandardResponse response = new StandardResponse(true, "Degree fetched successfully !");
		Optional<Degree> degreeOptional = degreeService.findFirstById(degreeId);
		if (degreeOptional.isPresent()) {
			response.addData("degree", degreeOptional.get());
		} else {
			response.setSuccess(false);
			response.setMessage("Degree with id : " + degreeId + " does not exist !");
			response.addData("degree", null);
		}

		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/degree/{degreeId}")
	public ResponseEntity<StandardResponse> deleteDegreeById(@PathVariable long degreeId) {
		AuthenticationHelper authenticationHelper = new AuthenticationHelper(
				SecurityContextHolder.getContext().getAuthentication());

		if (ActionEvaluator.isAdmin(authenticationHelper)) {
			degreeService.deleteById(degreeId);
			return ResponseEntity.ok(new StandardResponse(true, "Degree deleted successfully !"));
		} else {
			return ResponseEntity.ok(new StandardResponse(false, StandardMessages.ADMIN_ONLY));
		}
	}

	@PutMapping("/degree/{degreeId}")
	public ResponseEntity<StandardResponse> updateDegreeById(@PathVariable long degreeId, @RequestBody Degree degree) {
		AuthenticationHelper authenticationHelper = new AuthenticationHelper(
				SecurityContextHolder.getContext().getAuthentication());
		if (ActionEvaluator.isAdmin(authenticationHelper)) {
			Optional<Degree> degreeOptional = degreeService.findFirstById(degreeId);
			if (degreeOptional.isPresent()) {
				Degree degreetoUpdate = degreeOptional.get();
				degreetoUpdate.setTitle(degree.getTitle());
				degree = degreeService.saveDegree(degree);
				StandardResponse response = new StandardResponse(true, "Degree updated successfully !");
				response.addData("degree", degree);
				return ResponseEntity.ok(response);
			}
			return ResponseEntity.ok(new StandardResponse(false, "Degree with id : " + degreeId + " was not found !"));
		} else {
			return ResponseEntity.ok(new StandardResponse(false, StandardMessages.ADMIN_ONLY));
		}
	}

	@PostMapping("/branch")
	public ResponseEntity<StandardResponse> createBranch(@RequestBody @Valid Branch branch) {
		AuthenticationHelper authenticationHelper = new AuthenticationHelper(
				SecurityContextHolder.getContext().getAuthentication());
		// ALLOW only ADMIN and COORDINATOR to add degree
		if (ActionEvaluator.isAdmin(authenticationHelper) || ActionEvaluator.isCoordinator(authenticationHelper)) {
			try {
				branch = branchService.saveBranch(branch);
			} catch (Exception e) {
				return ResponseEntity.ok(new StandardResponse(false,
						String.format(StandardMessages.ENTITY_ALREADY_EXISTS, "Branch", "title", branch.getTitle())));
			}

			StandardResponse response = new StandardResponse(true, "Branch created successfully !");
			response.addData("branch", branch);
			return ResponseEntity.ok(response);
		}

		return ResponseEntity.ok(new StandardResponse(false, StandardMessages.ADMIN_AND_COORDINATOR_ONLY));
	}

	@GetMapping("/branch")
	public ResponseEntity<StandardResponse> fetchAllBranches() {
		StandardResponse response = new StandardResponse(true, "Branches fetched successfully !");
		response.addData("branches", branchService.getAllBranches());
		return ResponseEntity.ok(response);
	}

	@GetMapping("/branch/{branchId}")
	public ResponseEntity<StandardResponse> fetchBranchById(@PathVariable long branchId) {
		StandardResponse response = new StandardResponse(true, "Branches fetched successfully !");
		Optional<Branch> branchOptional = branchService.findFirstById(branchId);
		if (branchOptional.isPresent()) {
			response.addData("branch", branchOptional.get());
		} else {
			response.setSuccess(false);
			response.setMessage("Branch with id : " + branchId + " does not exist !");
			response.addData("branch", null);
		}

		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/branch/{branchId}")
	public ResponseEntity<StandardResponse> deleteBranchById(@PathVariable long branchId) {
		AuthenticationHelper authenticationHelper = new AuthenticationHelper(
				SecurityContextHolder.getContext().getAuthentication());

		if (ActionEvaluator.isAdmin(authenticationHelper)) {
			branchService.deleteById(branchId);
			return ResponseEntity.ok(new StandardResponse(true, "Branch deleted successfully !"));
		} else {
			return ResponseEntity.ok(new StandardResponse(false, StandardMessages.ADMIN_ONLY));
		}
	}

	@PutMapping("/branch/{branchId}")
	public ResponseEntity<StandardResponse> updateBranchById(@PathVariable long branchId, @RequestBody Branch branch) {
		AuthenticationHelper authenticationHelper = new AuthenticationHelper(
				SecurityContextHolder.getContext().getAuthentication());
		if (ActionEvaluator.isAdmin(authenticationHelper)) {
			Optional<Branch> branchOptional = branchService.findFirstById(branchId);
			if (branchOptional.isPresent()) {
				Branch branchtoUpdate = branchOptional.get();
				branchtoUpdate.setTitle(branch.getTitle());
				branch = branchService.saveBranch(branch);
				StandardResponse response = new StandardResponse(true, "Branch updated successfully !");
				response.addData("branch", branch);
				return ResponseEntity.ok(response);
			}
			return ResponseEntity.ok(new StandardResponse(false, "Branch with id : " + branchId + " was not found !"));
		} else {
			return ResponseEntity.ok(new StandardResponse(false, StandardMessages.ADMIN_ONLY));
		}
	}

}
