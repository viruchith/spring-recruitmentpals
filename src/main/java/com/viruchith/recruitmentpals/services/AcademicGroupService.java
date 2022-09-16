package com.viruchith.recruitmentpals.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viruchith.recruitmentpals.models.AcademicGroup;
import com.viruchith.recruitmentpals.repos.AcademicGroupRepository;

@Service
public class AcademicGroupService {
	@Autowired
	private AcademicGroupRepository academicGroupRepository;

	public AcademicGroup saveAcademicGroup(AcademicGroup academicGroup) {
		return academicGroupRepository.save(academicGroup);
	}

	public List<AcademicGroup> getAllAcademicGroups() {
		return academicGroupRepository.findAll();
	}

	public Optional<AcademicGroup> findFirstById(long id) {
		return academicGroupRepository.findById(id);
	}

	public void deleteById(long id) {
		academicGroupRepository.deleteById(id);
	}
}
