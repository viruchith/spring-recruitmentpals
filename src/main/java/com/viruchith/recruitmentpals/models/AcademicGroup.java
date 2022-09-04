package com.viruchith.recruitmentpals.models;

import java.time.Instant;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;
import lombok.Generated;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "recruitment_academicgroups",uniqueConstraints = {@UniqueConstraint(name = "acdemicgroup_unique_constraint" ,columnNames = {"batchStartYear","batchEndYear","degree_id","branch_id"})})
@Data
public class AcademicGroup {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NotNull
	@Min(1900)
	@Max(9999)
	private int batchStartYear;
	
	@NotNull
	@Min(1900)
	@Max(9999)
	private int batchEndYear;
	
	@NotNull
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "degree_id")
	private Degree degree;
	
	@NotNull
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "branch_id")
	private Branch branch;
	
	@CreatedDate
	private Instant createdAt;
	
	@LastModifiedDate
	private Instant updateAt;
	
}
