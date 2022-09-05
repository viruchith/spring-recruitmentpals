package com.viruchith.recruitmentpals.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viruchith.recruitmentpals.models.PlacementCoordinator;
import com.viruchith.recruitmentpals.models.projections.NoPasswordPlacementCoordinator;

public interface PlacementCoordinatorRepository extends JpaRepository<PlacementCoordinator, Long>{
	public PlacementCoordinator findFirstByEmail(String email);
	public List<NoPasswordPlacementCoordinator> findBy();
	public NoPasswordPlacementCoordinator findFirstById(long id);
}
