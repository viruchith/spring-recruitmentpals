package com.viruchith.recruitmentpals.helpers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StandardResponse {
	private boolean success;
	private String message;
	private Map<String,Object> data = new HashMap<>();
	
	
	
	public void addData(String key,Object value) {
		data.put(key, value);
	}



	public StandardResponse(boolean success, String message) {
		super();
		this.success = success;
		this.message = message;
	}
	
	
	
}
