package com.isotrol.redmine5.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
( 
	basePackages = { "com.isotrol.redmine5.principal", 
						"com.isotrol.redmine5.informes", 
						"com.isotrol.redmine5.comun", 
						"com.isotrol.redmine5.recursos" } 
)
public class DemoApplication 
{
	public static void main(String[] args) 
	{
		SpringApplication.run(DemoApplication.class, args);
	}
}
