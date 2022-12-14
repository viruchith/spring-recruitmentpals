package com.viruchith.recruitmentpals.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viruchith.recruitmentpals.models.Degree;
import com.viruchith.recruitmentpals.repos.DegreeRepository;

@Service
public class DegreeService {
	@Autowired
	private DegreeRepository degreeRepository;

	public Degree saveDegree(Degree degree) {
		return degreeRepository.save(degree);
	}

	public List<Degree> getAllDegrees() {
		return degreeRepository.findAll();
	}

	public Optional<Degree> findFirstById(long id) {
		return degreeRepository.findById(id);
	}

	public void deleteById(long id) {
		degreeRepository.deleteById(id);
	}
}
