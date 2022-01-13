package com.project.cabbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverDTO {

	private int driverId;

	private String driverName;

	private String emailId;

	private String contactNumber;

	private String licenseNumber;

	private String role;

	private boolean isAvailable;

	private VehicleDTO vehicle;

	private LocationDTO location;

}
