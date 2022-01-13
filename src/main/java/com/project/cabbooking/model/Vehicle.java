package com.project.cabbooking.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Vehicle {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "vehicle_id")
	private int vehicleId;

	@Column(name = "vehicle_name")
	private String vehicleName;

	@Column(name = "vehicle_number")
	private String vehicleNumber;

}
