package com.viruchith.recruitmentpals.models;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "recruitment_admin_users")
@Data
public class AdminUser {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NotNull
	@NotBlank
	@Column(unique = true)
	@Size(min = 5,max = 15,message = "username must be between 5 and 10 characters in length !")
	@Pattern(regexp = "^[a-zA-z][a-zA-Z0-9_]{4,14}$",message = "Username can contain only alphabets, numbers and underscore only !")
	private String username;
	
	@NotNull
	@NotBlank
	@Size(min = 8 , message = "Password must be greater than 8 characters in length !")
	private String password;
	
	@CreatedDate
	private Instant createdAt;
	
	@LastModifiedDate
	private Instant updateAt;
	
}
