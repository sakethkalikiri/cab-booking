package com.project.cabbooking.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Data;

@Data
@Entity
public class Driver {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "driver_id")
	private int driverId;

	@Column(name = "driver_name")
	private String driverName;

	@Column(name = "email_id")
	private String emailId;

	@Column(name = "contact_number")
	private String contactNumber;

	@Column(name = "license_number")
	private String licenseNumber;

	private String role;

	@Column(name = "is_available")
	private boolean isAvailable;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "vehicle_id")
	private Vehicle vehicle;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "location_id")
	private Location location;

}
