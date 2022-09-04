package com.viruchith.recruitmentpals.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.viruchith.recruitmentpals.models.AdminUser;
import com.viruchith.recruitmentpals.models.PlacementCoordinator;
import com.viruchith.recruitmentpals.repos.PlacementCoordinatorRepository;

@Service
public class PlacementCoordiantorService {
	@Autowired
	private PlacementCoordinatorRepository placementCoordinatorRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	
	public PlacementCoordinator findFirstByEmail(String email) {
		return placementCoordinatorRepository.findFirstByEmail(email);
	}
	
	public PlacementCoordinator createPlacementCoordinator(PlacementCoordinator placementCoordinator) {
		placementCoordinator.setPassword(encodePassword(placementCoordinator.getPassword()));
		return placementCoordinatorRepository.save(placementCoordinator);
	}
	
	public PlacementCoordinator savePlacementCoordinator(PlacementCoordinator placementCoordinator) {
		return placementCoordinatorRepository.save(placementCoordinator);	
	}
	
	public boolean validatePassword(PlacementCoordinator placementCoordinator,String password) {
		return passwordEncoder.matches(password, placementCoordinator.getPassword());
	}
	
	public String encodePassword(String password) {
		return passwordEncoder.encode(password);
	}
	
	


}
