package com.project.cabbooking.serviceImpl;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.project.cabbooking.dao.DriverDAO;
import com.project.cabbooking.dto.DriverDTO;
import com.project.cabbooking.exception.DriverAlreadyExistsException;
import com.project.cabbooking.exception.DriverNotFoundException;
import com.project.cabbooking.model.Driver;
import com.project.cabbooking.model.Location;
import com.project.cabbooking.service.DriverService;
import com.project.cabbooking.util.AppConstants;

@Service
public class DriverServiceImpl implements DriverService {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	DriverDAO driverDAO;

	@Override
	public DriverDTO getDriver(int driverId) {

		Driver driver = driverDAO.findByDriverId(driverId);

		if (driver != null) {
			DriverDTO driverDTO = modelMapper.map(driver, DriverDTO.class);
			return driverDTO;
		} else {
			throw new DriverNotFoundException("Driver with given ID not found");
		}
	}

	@Override
	public void createDriver(DriverDTO driverDTO) {
		if (driverDAO.validateDriver(driverDTO.getEmailId(), driverDTO.getContactNumber()) > 0) {
			throw new DriverAlreadyExistsException("Email/Phone Number already exists. Try again");
		} else {
			ModelMapper modelMapper = new ModelMapper();
			modelMapper.getConfiguration().setFieldMatchingEnabled(true).setMatchingStrategy(MatchingStrategies.STRICT);

			Driver driver = modelMapper.map(driverDTO, Driver.class);

			driver.setRole(AppConstants.ROLE.DRIVER.toString());

			if (!ObjectUtils.isEmpty(driverDTO.getLocation())) {
				Location location = modelMapper.map(driverDTO.getLocation(), Location.class);
				driver.setLocation(location);
			} else {
				Location location = new Location();
				location.setXCoordinate(-1);
				location.setYCoordinate(-1);
				driver.setLocation(location);
			}

			driverDAO.save(driver);
		}
	}

	@Override
	public void updateDriver(Driver driver, DriverDTO driverDTO) {

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setFieldMatchingEnabled(true).setMatchingStrategy(MatchingStrategies.STRICT);
		modelMapper.createTypeMap(DriverDTO.class, Driver.class).addMappings(mapper -> {
			mapper.map(src -> src.getVehicle(), Driver::setVehicle);
			mapper.map(src -> src.getLocation(), Driver::setLocation);
		});
		modelMapper.map(driverDTO, driver);

		driverDAO.save(driver);

	}

	@Override
	public boolean validateDriver(String emailId, String contactNumber) {
		if (driverDAO.validateDriver(emailId, contactNumber) > 0)
			return true;
		else
			return false;
	}

	@Override
	public void deleteDriver(int driverId) {

		Driver driver = driverDAO.findByDriverId(driverId);
		if (!ObjectUtils.isEmpty(driver)) {
			driverDAO.deleteById(driver.getDriverId());
		} else {
			throw new DriverNotFoundException("Driver with given ID not found");
		}
	}

	@Override
	public void updateStatus(int driverId, boolean status) {

		Driver driver = driverDAO.findByDriverId(driverId);

		if (!ObjectUtils.isEmpty(driver)) {
			driver.setAvailable(status);
			driverDAO.save(driver);
		} else {
			throw new DriverNotFoundException("Driver with ID: " + driverId + " not found");
		}
	}

	@Override
	public DriverDTO getDriverByName(String driverName) {
		Driver driver = driverDAO.findByDriverName(driverName);

		if (driver != null) {
			DriverDTO driverDTO = modelMapper.map(driver, DriverDTO.class);
			return driverDTO;
		} else {
			throw new DriverNotFoundException("Driver with given " + driverName + " not found");
		}
	}

}
