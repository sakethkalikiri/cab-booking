package com.project.cabbooking.service;

import java.util.List;

import com.project.cabbooking.dto.DriverDTO;
import com.project.cabbooking.dto.LocationDTO;
import com.project.cabbooking.dto.RideDTO;
import com.project.cabbooking.dto.UserDTO;
import com.project.cabbooking.model.User;

public interface UserService {

	public UserDTO getUser(int userId);

	public UserDTO getUserByName(String userName);

	public void createUser(UserDTO userDTO);

	public void updateUser(User user, UserDTO userDTO);

	public boolean validateUser(String emailId, String contactNumber);

	public void deleteUser(int userId);

	public List<DriverDTO> findRide(int userId, RideDTO rideDTO);

	public void updateUserLocation(int userId, LocationDTO locationDTO);

	public void chooseRide(String userName, String driverName);

	public String calculateBill(String userName);

}
