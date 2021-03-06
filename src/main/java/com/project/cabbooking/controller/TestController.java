package com.project.cabbooking.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@GetMapping("/test")
	public ResponseEntity<String> testAPIcall() {
		return new ResponseEntity<String>("App Working...", HttpStatus.OK);
	}
}
