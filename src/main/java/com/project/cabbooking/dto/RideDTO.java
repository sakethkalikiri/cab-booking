package com.project.cabbooking.dto;

import lombok.Data;

@Data
public class RideDTO {

	private int rideId;

	private UserDTO userDTO;

	private DriverDTO driverDTO;

	private LocationDTO sourceLocation;

	private LocationDTO destinationLocation;
}
