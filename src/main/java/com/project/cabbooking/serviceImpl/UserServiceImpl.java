package com.project.cabbooking.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.project.cabbooking.dao.DriverDAO;
import com.project.cabbooking.dao.RideDAO;
import com.project.cabbooking.dao.UserDAO;
import com.project.cabbooking.dao.WalletDAO;
import com.project.cabbooking.dto.DriverDTO;
import com.project.cabbooking.dto.LocationDTO;
import com.project.cabbooking.dto.RideDTO;
import com.project.cabbooking.dto.UserDTO;
import com.project.cabbooking.exception.DriverNotFoundException;
import com.project.cabbooking.exception.UserAlreadyExistsException;
import com.project.cabbooking.exception.UserNotFoundException;
import com.project.cabbooking.model.Driver;
import com.project.cabbooking.model.Location;
import com.project.cabbooking.model.Ride;
import com.project.cabbooking.model.User;
import com.project.cabbooking.model.Wallet;
import com.project.cabbooking.service.UserService;
import com.project.cabbooking.util.AppConstants;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	UserDAO userDAO;

	@Autowired
	RideDAO rideDAO;

	@Autowired
	DriverDAO driverDAO;

	@Autowired
	WalletDAO walletDAO;

	@Override
	public UserDTO getUser(int userId) {

		User user = userDAO.findByUserId(userId);

		if (user != null) {
			UserDTO userDTO = modelMapper.map(user, UserDTO.class);
			return userDTO;
		} else {
			throw new UserNotFoundException("User with given ID not found");
		}
	}

	@Override
	public void createUser(UserDTO userDTO) {
		if (userDAO.validateUser(userDTO.getEmailId(), userDTO.getContactNumber()) > 0) {
			throw new UserAlreadyExistsException("Email/Phone Number already exists. Try again");
		} else {
			ModelMapper modelMapper = new ModelMapper();
			modelMapper.getConfiguration().setFieldMatchingEnabled(true).setMatchingStrategy(MatchingStrategies.STRICT);

			User user = modelMapper.map(userDTO, User.class);

			user.setRole(AppConstants.ROLE.USER.toString());

			if (!ObjectUtils.isEmpty(userDTO.getLocation())) {

				Location location = new Location();
				location.setXCoordinate(userDTO.getLocation().getXCoordinate());
				location.setYCoordinate(userDTO.getLocation().getYCoordinate());
				user.setLocation(location);
			} else {
				Location location = new Location();
				location.setXCoordinate(-1);
				location.setYCoordinate(-1);
				user.setLocation(location);
			}

			userDAO.save(user);
		}
	}

	@Override
	public void updateUser(User user, UserDTO userDTO) {

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.createTypeMap(UserDTO.class, User.class);
		modelMapper.map(userDTO, user);

		userDAO.save(user);
	}

	@Override
	public boolean validateUser(String emailId, String contactNumber) {
		if (userDAO.validateUser(emailId, contactNumber) > 0)
			return true;
		else
			return false;
	}

	@Override
	public void deleteUser(int userId) {

		User user = userDAO.findByUserId(userId);
		if (!ObjectUtils.isEmpty(user)) {
			userDAO.deleteById(user.getUserId());
		} else {
			throw new UserNotFoundException("User with given ID not found");
		}
	}

	@Override
	public List<DriverDTO> findRide(int userId, RideDTO rideDTO) {

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setFieldMatchingEnabled(true).setMatchingStrategy(MatchingStrategies.STRICT);
		modelMapper.createTypeMap(LocationDTO.class, Location.class).addMappings(mapper -> {
			mapper.map(src -> src.getXCoordinate(), Location::setXCoordinate);
			mapper.map(src -> src.getYCoordinate(), Location::setYCoordinate);
		});

		User user = userDAO.findByUserId(userId);
		Ride ride = null;
		if (!ObjectUtils.isEmpty(rideDAO.findByUserId(userId))) {
			ride = rideDAO.findByUserId(userId);
			ride.setUser(user);
			updateUserLocation(userId, rideDTO.getSourceLocation());
			modelMapper.map(rideDTO.getDestinationLocation(), ride.getDestinationLocation());
			rideDAO.save(ride);
		} else {
			ride = new Ride();
			ride.setUser(user);
			updateUserLocation(userId, rideDTO.getSourceLocation());
			Location location = modelMapper.map(rideDTO.getDestinationLocation(), Location.class);
			ride.setDestinationLocation(location);
			ride.setDestinationLocation(null);
			rideDAO.save(ride);
		}

		List<Driver> drivers = driverDAO.findDriversByDistance(user.getLocation().getXCoordinate(),
				user.getLocation().getYCoordinate());
		List<DriverDTO> driverDTOList = new ArrayList<>();
		for (Driver driver : drivers) {
			DriverDTO driverDTO = modelMapper.map(driver, DriverDTO.class);
			if (driverDTO.isAvailable()) {
				driverDTOList.add(driverDTO);
			}
		}

		return driverDTOList;
	}

	@Override
	public void updateUserLocation(int userId, LocationDTO locationDTO) {

		User user = userDAO.findByUserId(userId);

		if (!ObjectUtils.isEmpty(user)) {
			user.getLocation().setXCoordinate(locationDTO.getXCoordinate());
			user.getLocation().setYCoordinate(locationDTO.getYCoordinate());
			userDAO.save(user);
		} else {
			throw new DriverNotFoundException("Driver with ID: " + userId + " not found");
		}
	}

	@Override
	public UserDTO getUserByName(String userName) {
		User user = userDAO.findByUserName(userName);

		if (user != null) {
			UserDTO userDTO = modelMapper.map(user, UserDTO.class);
			return userDTO;
		} else {
			throw new UserNotFoundException("User with given name " + userName + " not found");
		}
	}

	@Override
	public void chooseRide(String userName, String driverName) {

		User user = userDAO.findByUserName(userName);

		Driver driver = driverDAO.findByDriverName(driverName);

		Ride ride = rideDAO.findByUserId(user.getUserId());

		ride.setDriver(driver);

		rideDAO.save(ride);
	}

	@Override
	public String calculateBill(String userName) {
		User user = userDAO.findByUserName(userName);

		Ride ride = rideDAO.findByUserId(user.getUserId());

		int srcXcoordinate = user.getLocation().getXCoordinate();
		int srcYcoordinate = user.getLocation().getYCoordinate();

		int destXcoordinate = ride.getDestinationLocation().getXCoordinate();
		int destYcoordinate = ride.getDestinationLocation().getYCoordinate();

		long total = srcXcoordinate + destYcoordinate + srcYcoordinate + destXcoordinate;

		total = total * 5;

		user.setLocation(ride.getDestinationLocation());

		userDAO.save(user);

		Driver driver = ride.getDriver();
		driver.setLocation(ride.getDestinationLocation());
		driverDAO.save(driver);

		Wallet wallet = new Wallet();

		wallet.setDriver(driver);
		wallet.setBill(total);

		walletDAO.save(wallet);

		rideDAO.deleteById(ride.getRideId());

		return Long.toString(total);
	}

}
