package com.eaosoft.railway;

import com.eaosoft.railway.mapper.UserMapper;
import com.eaosoft.railway.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Random;

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
	@Test
	public void test01(){
		Calendar calendar = Calendar.getInstance();
		String month = "";
		int a = calendar.get(Calendar.MONTH) + 1;
		if (a<10){
			 month = "0"+a;
		}
		System.out.println(month);

	}

	@Test
	public void test02(){
		String imagePath = "aiofile2023041921681886637206.png";
		String path = imagePath.substring(7,11)
				+"\\"+imagePath.substring(11,13)
				+"\\"+imagePath.substring(13,15)
				+"\\"+imagePath.substring(15,17)
				+"\\"+imagePath;
		System.out.println(path);
	}


}
