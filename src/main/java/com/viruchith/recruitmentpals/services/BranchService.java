package com.viruchith.recruitmentpals.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viruchith.recruitmentpals.models.Branch;
import com.viruchith.recruitmentpals.models.Degree;
import com.viruchith.recruitmentpals.repos.BranchRepository;


@Service
public class BranchService {
	
	@Autowired
	private BranchRepository branchRepository;
	
	public Branch saveBranch(Branch branch){
		return branchRepository.save(branch);
	}
	
	public List<Branch> getAllBranches(){
		return branchRepository.findAll();
	}
	
	public Branch findFirstById(long id){
		return branchRepository.findById(id).get();
	}
}
