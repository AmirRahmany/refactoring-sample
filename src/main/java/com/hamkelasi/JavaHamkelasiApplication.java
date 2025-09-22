package com.hamkelasi;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class JavaHamkelasiApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaHamkelasiApplication.class, args);
	}
}
