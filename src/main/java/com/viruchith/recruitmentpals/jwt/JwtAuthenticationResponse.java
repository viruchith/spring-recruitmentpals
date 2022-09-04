package com.viruchith.recruitmentpals.jwt;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtAuthenticationResponse implements Serializable {
	private static final long serialVersionUID = -1579674465367117343L;
	private boolean success;
	private String message;
	private String token;
}
