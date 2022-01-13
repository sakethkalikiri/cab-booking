package com.project.cabbooking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.cabbooking.dao.DriverDAO;
import com.project.cabbooking.dao.WalletDAO;
import com.project.cabbooking.dto.DriverDTO;
import com.project.cabbooking.dto.DriverEarningsDTO;
import com.project.cabbooking.dto.LocationDTO;
import com.project.cabbooking.model.Driver;
import com.project.cabbooking.service.DriverService;

@RestController
@RequestMapping(path = "/driver")
public class DriverController {

	@Autowired
	private DriverService driverService;

	@Autowired
	private DriverDAO driverDAO;

	@Autowired
	private WalletDAO walletDAO;

	@GetMapping("/get/{driverId}")
	public ResponseEntity<DriverDTO> getDriverDetails(@PathVariable("driverId") int driverId) {

		DriverDTO driverDTO = driverService.getDriver(driverId);
		if (!ObjectUtils.isEmpty(driverDTO)) {
			return new ResponseEntity<DriverDTO>(driverDTO, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}

	@GetMapping("/get/name")
	public ResponseEntity<DriverDTO> getDriverDetailsByName(@RequestParam("driverName") String driverName) {

		DriverDTO driverDTO = driverService.getDriverByName(driverName);
		if (!ObjectUtils.isEmpty(driverDTO)) {
			return new ResponseEntity<DriverDTO>(driverDTO, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}

	@PostMapping("/create")
	public ResponseEntity<DriverDTO> createDriver(@RequestBody DriverDTO driverDTO) {
		driverService.createDriver(driverDTO);

		return new ResponseEntity<DriverDTO>(HttpStatus.CREATED);
	}

	@PutMapping("/update")
	public ResponseEntity<HttpStatus> updateDriver(@RequestBody DriverDTO driverDTO) {

		Driver driver = driverDAO.findByDriverId(driverDTO.getDriverId());

		boolean isValid = false;
		if (!ObjectUtils.isEmpty(driver)) {
			isValid = validateEmailAndPhoneNum(driver.getEmailId(), driverDTO.getEmailId(), driver.getContactNumber(),
					driverDTO.getContactNumber());

			if (!isValid) {
				driverService.updateDriver(driver, driverDTO);
				return new ResponseEntity<HttpStatus>(HttpStatus.OK);
			} else {
				return new ResponseEntity<HttpStatus>(HttpStatus.ALREADY_REPORTED);
			}
		} else {
			return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
		}
	}

	@DeleteMapping("/delete/{driverId}")
	public ResponseEntity<HttpStatus> deleteDriver(@PathVariable("driverId") int driverId) {

		driverService.deleteDriver(driverId);
		return new ResponseEntity<HttpStatus>(HttpStatus.ACCEPTED);

	}

	@PutMapping("/location/{driverId}")
	public ResponseEntity<HttpStatus> updateLocation(@PathVariable("driverId") int driverId,
			@RequestBody LocationDTO locationDTO) {

		driverService.updateLocation(driverId, locationDTO);
		return new ResponseEntity<HttpStatus>(HttpStatus.ACCEPTED);
	}

	@PutMapping("/status/{driverId}")
	public ResponseEntity<HttpStatus> updateStatus(@PathVariable("driverId") int driverId,
			@RequestParam("isAvailable") boolean isAvailable) {

		return new ResponseEntity<HttpStatus>(HttpStatus.ACCEPTED);
	}

	@GetMapping("/total-earnings")
	public ResponseEntity<List<DriverEarningsDTO>> findTotalEarnings() {

		List<DriverEarningsDTO> driverEarnings = walletDAO.findDriverEarnings();
		return new ResponseEntity<List<DriverEarningsDTO>>(driverEarnings, HttpStatus.OK);
	}

	private boolean validateEmailAndPhoneNum(String oldEmailId, String newEmailId, String oldPhoneNum,
			String newPhoneNum) {
		boolean isValid = false;
		if (oldEmailId.equals(newEmailId) && !oldPhoneNum.equals(newPhoneNum)) {
			isValid = driverService.validateDriver("", newPhoneNum);
		} else if (!oldEmailId.equals(newEmailId) && oldPhoneNum.equals(newPhoneNum)) {
			isValid = driverService.validateDriver(newPhoneNum, "");
		} else if (!oldEmailId.equals(newEmailId) && !oldPhoneNum.equals(newPhoneNum)) {
			isValid = driverService.validateDriver(newEmailId, newPhoneNum);
		}
		return isValid;
	}
}
