package com.viruchith.recruitmentpals.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viruchith.recruitmentpals.models.PlacementCoordinator;

public interface PlacementCoordinatorRepository extends JpaRepository<PlacementCoordinator, Long>{
	public PlacementCoordinator findFirstByEmail(String email);
}
