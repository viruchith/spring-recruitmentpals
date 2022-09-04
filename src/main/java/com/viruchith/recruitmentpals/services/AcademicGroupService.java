package com.viruchith.recruitmentpals.services;

import java.util.List;

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
	
	public List<AcademicGroup> getAllAcademicGroups(){
		return academicGroupRepository.findAll();
	}
}
