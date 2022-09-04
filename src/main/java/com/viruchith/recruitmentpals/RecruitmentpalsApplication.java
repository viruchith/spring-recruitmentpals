package com.viruchith.recruitmentpals;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class RecruitmentpalsApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecruitmentpalsApplication.class, args);
	}

}
