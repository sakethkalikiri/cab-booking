package com.project.cabbooking.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.cabbooking.model.Driver;

@Transactional
public interface DriverDAO extends JpaRepository<Driver, Integer> {

	@Query("select d from Driver d where d.driverId = :driverId")
	public Driver findByDriverId(@Param("driverId") int driverId);

	@Query("select count(*) from Driver d where d.emailId = :emailId or d.contactNumber = :contactNumber")
	public int validateDriver(@Param("emailId") String emailId, @Param("contactNumber") String contactNumber);

//	@Query(value = "select new com.project.cabbooking.dto.DriverDTO(d.driverId as driverId, d.driverName as driverName, d.emailId as emailId, d.contactNumber as contactNumber, d.licenseNumber as licenseNumber, d.role as role, com.project.cabbooking.dto.LocationDTO(d.location.xCoordinate as xCoordinate, d.location.yCoordinate as yCoordinate)) from Driver d where d.location.xCoordinate <= 5 or d.location.yCoordinate <= 5")
//	public List<DriverDTO> findDriversByDistance(@Param("xCoordinate") int xCoordinate,
//			@Param("yCoordinate") int yCoordinate);

	@Query("select d from Driver d where d.location.xCoordinate <= :xCoordinate or d.location.yCoordinate <= :yCoordinate")
	public List<Driver> findDriversByDistance(@Param("xCoordinate") int xCoordinate,
			@Param("yCoordinate") int yCoordinate);

	@Query("select d from Driver d where d.driverName = :driverName")
	public Driver findByDriverName(@Param("driverName") String driverName);
}