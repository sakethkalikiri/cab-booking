package com.project.cabbooking.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.cabbooking.model.User;

@Transactional
public interface UserDAO extends JpaRepository<User, Integer> {

	@Query("select u from User u where u.userId = :userId")
	public User findByUserId(@Param("userId") int userId);

	@Query("select u from User u where u.userName = :userName")
	public User findByUserName(@Param("userName") String userName);

	@Query("select count(*) from User u where u.emailId = :emailId or u.contactNumber = :contactNumber")
	public int validateUser(@Param("emailId") String emailId, @Param("contactNumber") String contactNumber);
}
