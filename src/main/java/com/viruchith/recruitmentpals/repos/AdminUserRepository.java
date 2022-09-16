package com.viruchith.recruitmentpals.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viruchith.recruitmentpals.models.AdminUser;

public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {
	public AdminUser findFirstByUsername(String username);
}
