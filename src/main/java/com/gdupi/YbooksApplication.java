package com.gdupi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableAutoConfiguration
@SpringBootApplication(scanBasePackages = "com.gdupi")
@RestController
public class YbooksApplication {

	public static void main(String[] args) {
		SpringApplication.run(YbooksApplication.class, args);
	}

	@GetMapping("hello")
	public String hello(){
		return "hello,ocean";
	}
}
