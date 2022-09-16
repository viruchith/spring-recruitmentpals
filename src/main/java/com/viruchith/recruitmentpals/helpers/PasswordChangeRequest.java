package com.viruchith.recruitmentpals.helpers;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeRequest {
	@NotNull
	@NotBlank
	@Size(min = 8, message = "Password must be greater than 8 characters in length !")
	private String password;

	@NotNull
	@NotBlank
	@Size(min = 8, message = "New Password must be greater than 8 characters in length !")
	private String newPassword;

	private long userId;
}
