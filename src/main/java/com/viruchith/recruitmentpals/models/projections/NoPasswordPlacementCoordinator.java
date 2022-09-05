package com.viruchith.recruitmentpals.models.projections;

import java.time.Instant;
import java.util.Set;

import com.viruchith.recruitmentpals.models.AcademicGroup;

public interface NoPasswordPlacementCoordinator {
	long getId();
	String getEmail();
	String getFirstName();
	String getLastName();
	Instant getCreatedAt();
	Instant getUpdateAt();
	Set<AcademicGroup> getAcademicGroups();
}
