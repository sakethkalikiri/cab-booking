package com.project.cabbooking.dto;

import lombok.Data;

@Data
public class UserDTO {

	private int userId;

	private String userName;

	private String contactNumber;

	private String emailId;

	private String role;

	private LocationDTO location;
}
