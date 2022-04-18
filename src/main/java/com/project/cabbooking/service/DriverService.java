package com.project.cabbooking.service;

import com.project.cabbooking.dto.DriverDTO;
import com.project.cabbooking.model.Driver;

public interface DriverService {

	public DriverDTO getDriver(int driverId);

	public DriverDTO getDriverByName(String driverName);

	public void createDriver(DriverDTO driverDTO);

	public void updateDriver(Driver driver, DriverDTO driverDTO);

	public boolean validateDriver(String emailId, String contactNumber);

	public void deleteDriver(int driverId);

	public void updateStatus(int driverId, boolean status);

}
