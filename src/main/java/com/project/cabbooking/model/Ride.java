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
public class Ride {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ride_id")
	private int rideId;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private User user;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "driver_id")
	private Driver driver;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "location_id")
	private Location destinationLocation;
}
