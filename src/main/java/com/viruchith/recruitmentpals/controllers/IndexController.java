package com.viruchith.recruitmentpals.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viruchith.recruitmentpals.models.AdminUser;
import com.viruchith.recruitmentpals.services.AdminUserService;

@RestController
@RequestMapping(path = "/hello")
public class IndexController {
	
	@Autowired
	private AdminUserService adminUserService;
	
	@GetMapping("")
	public ResponseEntity<?> hello() {
		
//		AdminUser user = new AdminUser();
//		user.setUsername("admin1");
//		user.setPassword(adminUserService.encodePassword("admin123"));
//		
//		adminUserService.saveAdminUser(user);
		
//		AdminUser adminUser = adminUserService.findFirstByUsername("admin");
//		adminUser.setPassword(adminUserService.encodePassword("admin123"));
//		System.out.println(adminUser);
		Map<Object,Object> map = new HashMap<>();
		map.put("success", true);
		map.put("message", "Hello World !");
		return ResponseEntity.ok(map);
	}
}
