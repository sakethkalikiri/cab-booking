package com.project.cabbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverEarningsDTO {

	private String driverName;

	private long totalEarning;

}
