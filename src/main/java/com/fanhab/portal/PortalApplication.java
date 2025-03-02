package com.fanhab.portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class PortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(PortalApplication.class, args);
		LocalDate start = LocalDate.parse("1403-01-01");
		LocalDate end = LocalDate.parse("1403-01-30");


		List<LocalDate> allDays = start.datesUntil(end.plusDays(1)).collect(Collectors.toList());
		for (LocalDate day : allDays) {
			System.out.println(day);
		}
	}

}
