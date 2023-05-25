package com.eaosoft.railway.filter;

import com.alibaba.fastjson.JSON;
import com.eaosoft.railway.entity.User;
import com.eaosoft.railway.service.IUserService;
import com.eaosoft.railway.utils.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Set;
 
@Slf4j
public class UrlFilter extends PathMatchingFilter {

//    @Resource
//    private PermissionService permissionService;
    @Resource
    IUserService userService;

    @Resource
    StringRedisTemplate redisTemplate;
 
    @Override
    protected boolean onPreHandle(ServletRequest request,
                                  ServletResponse response,
                                  Object mappedValue) throws Exception {
//        if (permissionService == null) {
//            permissionService = ApplicationContextHolder.getContext().getBean(PermissionService.class);
//        }
 
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String uri = httpServletRequest.getRequestURI();
        String token = httpServletRequest.getHeader(HttpHeaders.COOKIE);
       /// String userIdStr = JwtUtil.getUserIdByToken(token);

        System.out.println("------------------");
        Subject subject = SecurityUtils.getSubject();

        // 判断是否时登录放行路径
        if (uri.equals("/railway/login/login.do")){
            return true;
        }

        // 不是登录放行路径，判断是否携带请求token以及token是否有效
        if (StringUtils.hasText(token) && redisTemplate.hasKey(token)) {
            ValueOperations<String, String> forValue = redisTemplate.opsForValue();
            String json = forValue.get(token);
            Map o = JSON.parseObject(json, Map.class);
            //获取map中的权限
            List<String> permissions = (List<String>) o.get("permissions");
            if (permissions.contains(uri)) {
                return true;
            } else {
                return false;
            }
        }else {
//            HttpServletResponse httpResponse = (HttpServletResponse) response;
////             ResponseUtil.jsonResponse(httpResponse, HttpStatus.FORBIDDEN.value(),
////                "用户(" + userId + ")无此url(" + uri + ")权限");
            UnauthorizedException ex = new UnauthorizedException("当前用户没有访问路径" + uri + "的权限");
            subject.getSession().setAttribute("ex",ex);
            WebUtils.issueRedirect(request, response, "/unauthorized");
            return false;
        }
 
//        if (permissionService == null) {
//            permissionService = ApplicationContextHolder.getContext().getBean(PermissionService.class);
//        }
//        Set<String> permissions = permissionService.getPermissionsByUserId(userId);

 
//        // 实际应该从数据库或者redis里通过userId获得拥有权限的url
//        if (permissions.contains(uri)) {
//            return true;
//        }
//
//        // 构造无权限时的response
//        HttpServletResponse httpResponse = (HttpServletResponse) response;
//        ResponseUtil.jsonResponse(httpResponse, HttpStatus.FORBIDDEN.value(),
//                "用户(" + userId + ")无此url(" + uri + ")权限");
//
//        return false;
    }
}