package com.project.cabbooking.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.cabbooking.model.Ride;

@Transactional
public interface RideDAO extends JpaRepository<Ride, Integer> {

	@Query("select r from Ride r where r.user.userId = :userId")
	public Ride findByUserId(@Param("userId") int userId);

}
