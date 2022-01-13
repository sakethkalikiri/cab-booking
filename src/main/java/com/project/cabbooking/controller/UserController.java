package com.project.cabbooking.controller;

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

import com.project.cabbooking.dao.UserDAO;
import com.project.cabbooking.dto.UserDTO;
import com.project.cabbooking.model.User;
import com.project.cabbooking.service.UserService;

@RestController
@RequestMapping(path = "/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserDAO userDAO;

	@GetMapping("/get/{userId}")
	public ResponseEntity<UserDTO> getUserDetails(@PathVariable("userId") int userId) {

		UserDTO userDTO = userService.getUser(userId);
		if (!ObjectUtils.isEmpty(userDTO)) {
			return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}

	@GetMapping("/get/name")
	public ResponseEntity<UserDTO> getUserDetailsByName(@RequestParam("userName") String userName) {

		UserDTO userDTO = userService.getUserByName(userName);
		if (!ObjectUtils.isEmpty(userDTO)) {
			return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}

	@PostMapping("/create")
	public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
		userService.createUser(userDTO);

		return new ResponseEntity<UserDTO>(HttpStatus.CREATED);
	}

	@PutMapping("/update")
	public ResponseEntity<HttpStatus> updateUser(@RequestBody UserDTO userDTO) {

		User user = userDAO.findByUserId(userDTO.getUserId());

		boolean isValid = false;
		if (!ObjectUtils.isEmpty(user)) {
			isValid = validateEmailAndPhoneNum(user.getEmailId(), userDTO.getEmailId(), user.getContactNumber(),
					userDTO.getContactNumber());

			if (!isValid) {
				userService.updateUser(user, userDTO);
				return new ResponseEntity<HttpStatus>(HttpStatus.OK);
			} else {
				return new ResponseEntity<HttpStatus>(HttpStatus.ALREADY_REPORTED);
			}
		} else {
			return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
		}
	}

	@DeleteMapping("/delete/{userId}")
	public ResponseEntity<HttpStatus> deleteUser(@PathVariable("userId") int userId) {

		userService.deleteUser(userId);
		return new ResponseEntity<HttpStatus>(HttpStatus.ACCEPTED);

	}

	private boolean validateEmailAndPhoneNum(String oldEmailId, String newEmailId, String oldPhoneNum,
			String newPhoneNum) {
		boolean isValid = false;
		if (oldEmailId.equals(newEmailId) && !oldPhoneNum.equals(newPhoneNum)) {
			isValid = userService.validateUser("", newPhoneNum);
		} else if (!oldEmailId.equals(newEmailId) && oldPhoneNum.equals(newPhoneNum)) {
			isValid = userService.validateUser(newPhoneNum, "");
		} else if (!oldEmailId.equals(newEmailId) && !oldPhoneNum.equals(newPhoneNum)) {
			isValid = userService.validateUser(newEmailId, newPhoneNum);
		}
		return isValid;
	}
}
