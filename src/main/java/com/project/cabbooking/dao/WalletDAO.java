package com.project.cabbooking.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.cabbooking.dto.DriverEarningsDTO;
import com.project.cabbooking.model.Wallet;

@Transactional
public interface WalletDAO extends JpaRepository<Wallet, Integer> {

	@Query("select new com.project.cabbooking.dto.DriverEarningsDTO(w.driver.driverName as driverName, sum(w.bill) as totalEarning) from Wallet w GROUP BY w.driver")
	public List<DriverEarningsDTO> findDriverEarnings();
}
