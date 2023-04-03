package com.eaosoft.JWT.handler;



import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.eaosoft.JWT.annotation.PassToken;
import com.eaosoft.JWT.annotation.UserLoginToken;
import com.eaosoft.JWT.util.TokenModel;
import com.eaosoft.exception.OneException;
import com.eaosoft.railway.entity.User;
import com.eaosoft.railway.service.AuthTokenService;
import com.eaosoft.railway.service.IUserService;
import com.eaosoft.railway.utils.IPUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.lang.reflect.Method;


/**
 * @author qiaoyn
 * @date 2019/06/14
 */

@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Resource
    IUserService userService;
    @Resource
    AuthTokenService authTokenService;

    public String verifyTokenRequestTocken(HttpServletRequest httpServletRequest) throws Exception {
        BufferedReader reader = httpServletRequest.getReader();
        StringBuilder builder = new StringBuilder();
        String line = reader.readLine();
        while(line != null){
            builder.append(line);
            line = reader.readLine();
        }
        reader.close();
        if(builder.toString()==null){

        }
         String ipAddr = IPUtils.getIpAddr(httpServletRequest);
        
        JSONObject pValueJSON = JSONObject.parseObject(builder.toString());
        String s = pValueJSON.getString("reqValue");
        /*String s = AES256.aesDecodeStr(reqValue, null);*/

        if(StringUtils.isBlank(s)){
            return null;
        }
        httpServletRequest.setAttribute("data",s);
        JSONObject jsonObject = null;
        try{
            jsonObject=JSONObject.parseObject(s);
        }catch (Exception e){
            throw new OneException(500,"json error");
        }
        if(jsonObject==null){
            return null;
        }
        String token = jsonObject.getString("token");
        if(StringUtils.isBlank(token)){
            return null;
        }
        return token;
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        if(!(object instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod=(HandlerMethod)object;
        Method method=handlerMethod.getMethod();
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()){
               String token=verifyTokenRequestTocken(httpServletRequest);
                if(StringUtils.isBlank(token)){
                    return false;
                }
                if (token == null) {
                    throw new OneException(401,"Not token，Please log in again");
                }
                TokenModel tokenModel = authTokenService.verifyToken(token);
                if(tokenModel==null||tokenModel.getToken() == null){
                    throw new OneException(401,"Token is invalid");
                }
                String userId;
                try {
                    if (JWT.decode(token).getAudience().size()==0) {
                        throw new OneException(401,"Token is invalid");
                    }
                    userId = JWT.decode(token).getAudience().get(0);
                } catch (JWTDecodeException j) {
                    throw new RuntimeException("401");
                }
                User user = userService.selectByUsername(userId);
                if (user == null) {
                    throw new OneException(500,"User is not found,Please log in again");
                }

                if(StringUtils.isBlank(user.getPassword())){
                    throw new OneException(500,"User`d passWord is found");
                }
                Algorithm algorithm = Algorithm.HMAC256(user.getPassword());
                JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                try {
                    jwtVerifier.verify(token);
                } catch (JWTVerificationException e) {
                    throw new OneException(401,"Token is invalid");
                }
                return true;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }
    
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

}

