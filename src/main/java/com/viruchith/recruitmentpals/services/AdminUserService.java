package com.viruchith.recruitmentpals.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.viruchith.recruitmentpals.models.AdminUser;
import com.viruchith.recruitmentpals.repos.AdminUserRepository;

@Service
public class AdminUserService {

	@Autowired
	private AdminUserRepository adminUserRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public AdminUser saveAdminUser(AdminUser adminUser) {
		return adminUserRepository.save(adminUser);
	}

	public AdminUser findFirstByUsername(String username) {
		return adminUserRepository.findFirstByUsername(username);
	}

	public boolean validatePassword(AdminUser adminUser, String password) {
		return passwordEncoder.matches(password, adminUser.getPassword());
	}

	public String encodePassword(String password) {
		return passwordEncoder.encode(password);
	}

	public void deleteAdminUser(AdminUser adminUser) {
		adminUserRepository.delete(adminUser);
	}

	public Optional<AdminUser> findFirstById(long id) {
		return adminUserRepository.findById(id);
	}
}
