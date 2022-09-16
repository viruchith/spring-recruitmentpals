package com.viruchith.recruitmentpals.dtos;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AcademicGroupUpdateDTO {

	@NotNull
	@Min(1)
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
	@Min(1)
	private int degreeId;

	@NotNull
	@Min(1)
	private int branchId;

	@AssertTrue(message = "batchStartYear Must Be Less than batchEndYear")
	public boolean isStartYearLessThanEndYear() {
		return batchStartYear < batchEndYear;
	}

}
