package com.viruchith.recruitmentpals.models;

import java.time.Instant;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

@Entity
@Table(name = "recruitment_placement_coordiantors")
@EntityListeners(AuditingEntityListener.class)
@Data
public class PlacementCoordinator {
	@javax.persistence.Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@NotNull
	@NotBlank
	@Email
	@Column(unique = true)
	private String email;

	@NotNull
	@NotBlank
	@Size(min = 8, max = 200)
	private String password;

	@NotNull
	@NotBlank
	@Size(min = 2, max = 256)
	private String firstName;

	@NotNull
	@NotBlank
	@Size(min = 2, max = 256)
	private String lastName;

	@ManyToMany(cascade = CascadeType.ALL)
	private Set<AcademicGroup> academicGroups;

	@CreatedDate
	private Instant createdAt;

	@LastModifiedDate
	private Instant updateAt;

}
