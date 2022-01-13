package com.project.cabbooking.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ComponentScan({ "com.project.cabbooking" })
@EntityScan("com.project.cabbooking.model")
@EnableJpaRepositories("com.project.cabbooking.dao")
@EnableAutoConfiguration
@EnableTransactionManagement
public class CabBookingApplication {

	@Bean
	public ModelMapper getModelMapper() {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
		return mapper;
	}

	public static void main(String[] args) {
		SpringApplication.run(CabBookingApplication.class, args);
	}

}
