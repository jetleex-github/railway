package com.eaosoft.railway;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@MapperScan("com.eaosoft.railway.mapper")
public class RailwayApplication {

	public static void main(String[] args) {
		SpringApplication.run(RailwayApplication.class, args);
	}

}
