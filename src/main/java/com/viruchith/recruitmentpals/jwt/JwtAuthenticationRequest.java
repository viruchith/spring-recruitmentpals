package com.viruchith.recruitmentpals.jwt;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class JwtAuthenticationRequest implements Serializable {
	private static final long serialVersionUID = 6548937007411046743L;
	@NotNull
	@NotBlank
	@Size(min = 5, max = 15, message = "username must be between 5 and 10 characters in length !")
	@Pattern(regexp = "^[a-zA-z][a-zA-Z0-9_]{4,14}$", message = "Username can contain only alphabets, numbers and underscore only !")
	private String username;

	@NotNull
	@NotBlank
	@Size(min = 8, message = "Password must be greater than 8 characters in length !")
	private String password;
}
