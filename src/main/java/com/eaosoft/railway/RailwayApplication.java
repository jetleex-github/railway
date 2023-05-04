package com.eaosoft.railway;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@MapperScan("com.eaosoft.railway.mapper")
@ComponentScan(basePackages = {"com.eaosoft.shiro"})
@ComponentScan(basePackages = {"com.eaosoft.railway"})
@ComponentScan(basePackages = {"com.eaosoft.mqtt"})
public class RailwayApplication {

	public static void main(String[] args) {
		SpringApplication.run(RailwayApplication.class, args);
	}

}
