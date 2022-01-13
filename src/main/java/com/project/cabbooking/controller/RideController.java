package com.project.cabbooking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.cabbooking.dto.DriverDTO;
import com.project.cabbooking.dto.RideDTO;
import com.project.cabbooking.service.UserService;

@RestController
@RequestMapping(path = "/ride")
public class RideController {

	@Autowired
	private UserService userService;

	@GetMapping("/{userId}")
	public ResponseEntity<List<DriverDTO>> findRide(@PathVariable("userId") int userId, @RequestBody RideDTO rideDTO) {
		List<DriverDTO> driverDTOs = userService.findRide(userId, rideDTO);
		return new ResponseEntity<List<DriverDTO>>(driverDTOs, HttpStatus.OK);
	}

	@GetMapping("/choose")
	public ResponseEntity<HttpStatus> chooseRide(@RequestParam("userName") String userName,
			@RequestParam("driverName") String driverName) {
		userService.chooseRide(userName, driverName);
		return new ResponseEntity<HttpStatus>(HttpStatus.OK);
	}

	@GetMapping("/bill")
	public ResponseEntity<String> calculateBill(@RequestParam("userName") String userName) {
		String totalBill = userService.calculateBill(userName);
		return new ResponseEntity<String>(totalBill, HttpStatus.OK);
	}

}
