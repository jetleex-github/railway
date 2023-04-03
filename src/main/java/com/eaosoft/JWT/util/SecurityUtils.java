package com.eaosoft.JWT.util;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.eaosoft.railway.entity.User;
import com.eaosoft.railway.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author ZhouWenTao
 * @Date 2022/7/15 11:11
 * @Version 1.0
 */
@Component
public class SecurityUtils {
    @Autowired
    private UserMapper userMapper;
    private static SecurityUtils securityUtils;
    @PostConstruct
    public void init(){
        securityUtils = this;
        securityUtils.userMapper = this.userMapper;
    }
    public User getLoginUser(){
        HttpServletRequest httpServletRequest = HttpContextUtils.getHttpServletRequest();
        String data =(String) httpServletRequest.getAttribute("data");
        JSONObject jsonObject = JSONObject.parseObject(data);
        String token = jsonObject.getString("token");
        String userId = JWT.decode(token).getAudience().get(0);
        User user = userMapper.selectById(userId);
        return user;
    }
    public String getUserId(){
        HttpServletRequest httpServletRequest = HttpContextUtils.getHttpServletRequest();
        String data =(String) httpServletRequest.getAttribute("data");
        JSONObject jsonObject = JSONObject.parseObject(data);
        String token = jsonObject.getString("token");
        String userId = JWT.decode(token).getAudience().get(0);
        return userId;
    }
}
