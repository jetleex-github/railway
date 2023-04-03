package com.eaosoft.JWT.util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

public class JwtUtil {
	// 签名不应该泄露给他人非常危险 这一点要非常注意
	private static String secret = "!@#$%lijihao!@#$%+%$#@!yangding%$#@!+lijihaoyangding";

	/**
	 * 
	 * 生成token
	 * 
	 * @param hashmap
	 */
	public static String InserToken(Map<String, String> hashmap) {

		Calendar insCalendar = Calendar.getInstance(); // 提供日期格式的处理

		// insCalendar.add(Calendar.DATE, 7); // 单位为天 默认7天后过期
		insCalendar.add(Calendar.MINUTE,30);

		JWTCreator.Builder builder = JWT.create(); // 创建JWT builder

		hashmap.forEach((k, v) -> { // 将传入的值循环插入
			builder.withClaim(k, v);
		});

		String token = builder.withExpiresAt(insCalendar.getTime()) // 指定指令令牌过期时间 上面指定时间单位为天 单位可以自行更改

				.sign(Algorithm.HMAC256(secret)); // 传入上面定义的secret

		return token; // 最后将生成的token返回
	}

	/**
	 * 验证token合法性
	 *
	 */
	public static DecodedJWT SelectToken(String token) { // 验证合法性此时报错就是没有通过验证
		// 为你列出常见的报错
		/**
		 * - SignatureVerificationException : 签名不一致异常 - TokenExpiredException: 令牌过期异常 -
		 * AlgorithmMismatchException: 算法不匹配异常 - InvalidclaimException: 失效的payload异常
		 * 
		 */
		JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secret)).build(); // 传入签名

		DecodedJWT verify = jwtVerifier.verify(token);// 传入token

		return verify; // 最后将生成的verify对象返回
	}
}

