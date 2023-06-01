package com.eaosoft.railway.filter;


import com.alibaba.fastjson.JSON;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 *  权限 拦截策略
 */
public class URLPathMatchingFilter extends PathMatchingFilter {
    @Resource
    private StringRedisTemplate redisTemplate;

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {

//          if (loginService==null){
//              loginService= SpringContextUtil.getContext().getBean(LoginService.class);
//          }
        String token = ((HttpServletRequest) request).getHeader("token");
        //请求的url
        String requestURL = getPathWithinApplication(request);

        System.out.println("请求的url :" + requestURL);
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            // 如果没有登录, 直接返回true 进入登录流程
            return true;
        }
        //
        //是否携带请求token以及token是否有效
        if (StringUtils.hasText(token) && redisTemplate.hasKey(token)) {
            ValueOperations<String, String> forValue = redisTemplate.opsForValue();
            String json = forValue.get(token);
            Map o = JSON.parseObject(json, Map.class);
            //获取map中的权限
            List<String> permissions = (List<String>) o.get("permissions");
            if (permissions.contains(requestURL)) {
                return true;
            } else {
                return false;
            }
        }else {
            UnauthorizedException ex = new UnauthorizedException("当前用户没有访问路径" + requestURL + "的权限");
            subject.getSession().setAttribute("ex",ex);
            WebUtils.issueRedirect(request, response, "/unauthorized");
            return false;
        }
//        String username = TokenUtil.getUsername(token);
//        redisTemplate.opsForValue().
//        // String email = TokenManager.getEmail();
//
//        List<Upermission> permissions = loginService. upermissions(email);
//
//        boolean hasPermission = false;
//        for (Upermission url : permissions) {
//
//              if (url.getUrl().equals(requestURL)){
//                  hasPermission = true;
//                  break;
//              }
//        }
//        if (hasPermission){
//            return true;
//        }else {
//            UnauthorizedException ex = new UnauthorizedException("当前用户没有访问路径" + requestURL + "的权限");
//            subject.getSession().setAttribute("ex",ex);
//            WebUtils.issueRedirect(request, response, "/unauthorized");
//            return false;
//
//        }

    }
}