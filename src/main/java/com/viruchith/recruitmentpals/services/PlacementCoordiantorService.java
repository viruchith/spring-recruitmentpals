package com.viruchith.recruitmentpals.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.viruchith.recruitmentpals.models.PlacementCoordinator;
import com.viruchith.recruitmentpals.models.projections.NoPasswordPlacementCoordinator;
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
		return savePlacementCoordinator(placementCoordinator);
	}
	
	public PlacementCoordinator savePlacementCoordinator(PlacementCoordinator placementCoordinator) {
		return placementCoordinatorRepository.save(placementCoordinator);	
	}
	
	
	public void deletePlacementCoordinator(PlacementCoordinator placementCoordinator) {
		placementCoordinatorRepository.delete(placementCoordinator);
	}
	
	public Optional<PlacementCoordinator> findFirstById(long id){
		return placementCoordinatorRepository.findById(id);
	}
	
	
	public NoPasswordPlacementCoordinator findFirstByIdNoPassword(long id){
		return placementCoordinatorRepository.findFirstById(id);
	}
	
	public boolean validatePassword(PlacementCoordinator placementCoordinator,String password) {
		return passwordEncoder.matches(password, placementCoordinator.getPassword());
	}
	
	public String encodePassword(String password) {
		return passwordEncoder.encode(password);
	}

	public List<PlacementCoordinator> findAll() {
		// TODO Auto-generated method stub
		return placementCoordinatorRepository.findAll();
	}

	public List<NoPasswordPlacementCoordinator> findAllNoPassword() {
		// TODO Auto-generated method stub
		return placementCoordinatorRepository.findBy();
	}
	
	public boolean isIDPresent(long id) {
		Optional<PlacementCoordinator> placementCoordinatorOptional = findFirstById(id);
		return placementCoordinatorOptional.isPresent();
	}
	
	
	
	


}
