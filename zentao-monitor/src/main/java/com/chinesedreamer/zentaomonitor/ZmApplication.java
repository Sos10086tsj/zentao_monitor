package com.chinesedreamer.zentaomonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.chinesedreamer.zentaomonitor" })
public class ZmApplication {
	public static void main(String[] args) {
		SpringApplication.run(ZmApplication.class, args);
	}
}
