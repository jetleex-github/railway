package com.eaosoft.railway;

import com.eaosoft.railway.mapper.UserMapper;
import com.eaosoft.railway.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class RealwayApplicationTests {

	@Test
	void contextLoads() {

	}

	@Resource
	UserMapper userMapper;

	@Resource
	UserServiceImpl userService;

/*	@Test
	public void login(){
		//User user = user.selectByUsernameAndPassword("user", "123456");
		CommonResult user = userService.login("user","123456");
		System.out.println(user);
		*//*if (user != null ){
			return new CommonResult(200,"success",user);
		}
		return new CommonResult(500,"账号或密码错误",null);*//*
	}*/

}
