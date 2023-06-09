package com.eaosoft.railway.filter;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.eaosoft.railway.entity.JWTToken;
import com.eaosoft.railway.entity.Result;
import com.eaosoft.railway.entity.User;
import com.eaosoft.railway.utils.RedisUtil;
import com.eaosoft.railway.utils.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;



/**
 * @ Program       :  com.ljnt.blog.filter.JWTFilter
 * @ Description   :  自定义jwt过滤器，对token进行处理
 * @ Author        :  lj
 * @ CreateDate    :  2020-2-4 17:28
 */
public class JWTFilter extends BasicHttpAuthenticationFilter {



    /**
     * 判断是否允许通过
     *
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
       // System.out.println("isAccessAllowed方法");
        try {
            return executeLogin(request, response);
        } catch (Exception e) {
            System.out.println("错误" + e);
//            throw new ShiroException(e.getMessage());
            responseError(response, "shiro fail");
            return false;
        }
    }

    /**
     * 是否进行登录请求
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
      //  System.out.println("isLoginAttempt方法");
        String token = ((HttpServletRequest) request).getHeader("token");
        if (token != null) {
            return true;
        }
        return false;
    }

    /**
     * 创建shiro token
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        //System.out.println("createToken方法");
        String jwtToken = ((HttpServletRequest) request).getHeader("token");
        if (jwtToken != null)
            return new JWTToken(jwtToken);

        return null;
    }


    /**
     * isAccessAllowed为false时调用，验证失败
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        //System.out.println("onAccessDenied");
        this.sendChallenge(request, response);
        responseError(response, "token verify fail");
        return false;
    }


    /**
     * shiro验证成功调用
     *
     * @param token
     * @param subject
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) {
       // System.out.println("onLoginSuccess：");
        String jwttoken = (String) token.getPrincipal();
        if (jwttoken != null) {
            try {
                if (TokenUtil.verify(jwttoken)) {
                    //判断Redis是否存在所对应的RefreshToken
                    String account = TokenUtil.getUsername(jwttoken);
                    Long currentTime = TokenUtil.getCurrentTime(jwttoken);
                    if (RedisUtil.hasKey(account)) {
                        JSONObject jsonObject = (JSONObject) RedisUtil.get(account);
                        Long currentTimeMillisRedis = (Long) jsonObject.get("currentTimeMillis");
                        if (currentTimeMillisRedis.equals(currentTime)) {
                            return true;
                        }
                    }
                }
                return false;
            } catch (Exception e) {
                System.out.println("token验证：" + e.getClass());
                if (e instanceof TokenExpiredException) {
                    System.out.println("TokenExpiredException");
                    if (refreshToken(request, response)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
        return true;
    }
// protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
//        System.out.println("onLoginSuccess：");
//        String jwttoken = (String) token.getPrincipal();
//
//        if (jwttoken != null) {
//            try {
//                if (TokenUtil.verify(jwttoken)) {
//                    //判断Redis是否存在所对应的RefreshToken
//                    String account = TokenUtil.getUsername(jwttoken);
//                    Long currentTime = TokenUtil.getCurrentTime(jwttoken);
//
//                    if (RedisUtil.hasKey(account)) {
//                      //  Map jsonObject = redisTemplate.opsForValue().get(account);
//
//
//                        JSONObject jsonObject =(JSONObject) RedisUtil.get(account);
//                        //System.out.println("jsonObject===>"+jsonObject);
//
//                        Long currentTimeMillisRedis =(Long) jsonObject.get("currentTimeMillis");
//                       // Long currentTimeMillisRedis = (Long) RedisUtil.get(account);
//
//                        System.out.println("currentTimeMillisRedis======"+currentTimeMillisRedis);
//                        System.out.println("currentTime==="+currentTime);
//                        boolean equals = currentTimeMillisRedis.equals(currentTime);
//                        System.out.println("equals===="+equals);
//                        if (currentTimeMillisRedis.equals(currentTime)) {
//                            return true;
//                        }
//                    }else {
//                        if (refreshToken(request, response)) {
//                            return true;
//                        }
//                    }
//                }
//                return false;
//            } catch (Exception e) {
//                Throwable throwable = e.getCause();
//                System.out.println("token验证：" + e.getClass());
//                if (e instanceof TokenExpiredException) {
//                    System.out.println("TokenExpiredException");
//                    if (refreshToken(request, response)) {
//                        return true;
//                    } else {
//                        return false;
//                    }
//                }
//            }
//        }
//        return true;
//    }


    /**
     * 拦截器的前置方法，此处进行跨域处理
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-Control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Resquest-Headers"));
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
        }

        //如果不带token，不去验证shiro
        if (!isLoginAttempt(request, response)) {
            responseError(httpServletResponse, "no token");
            return false;
        }
        return super.preHandle(request, response);

    }


    /**
     * 刷新AccessToken，进行判断RefreshToken是否过期，未过期就返回新的AccessToken且继续正常访问
     *
     * @param request
     * @param response
     * @return
     */
    private boolean refreshToken(ServletRequest request, ServletResponse response) {
       // System.out.println("refreshToken方法");
        String token = ((HttpServletRequest) request).getHeader("token");
        String username = TokenUtil.getUsername(token);
        String password = TokenUtil.getPassword(token);
        Long currentTime = TokenUtil.getCurrentTime(token);

        // 判断Redis中RefreshToken是否存在
        if (RedisUtil.hasKey(token)) {
            JSONObject jSONObject =(JSONObject) RedisUtil.get(token);
            Long currentTimeMillisRedis =jSONObject.getLong("currentTimeMillis");
            // 获取当前AccessToken中的时间戳，与RefreshToken的时间戳对比，如果当前时间戳一致，进行AccessToken刷新
            if (currentTimeMillisRedis.equals(currentTime)) {
                Long currentTimeMillis = System.currentTimeMillis();
                User user = jSONObject.getObject("user", User.class);
                Map<String, Object> map = new HashMap<>();
                map.put("user", user);
                map.put("currentTimeMillis",currentTimeMillis);

                String username1 = user.getUsername();

                RedisUtil.set(username1,JSON.toJSON(map),12*60*60);
                // 刷新AccessToken，设置时间戳为当前最新时间戳
                String token1 = TokenUtil.sign(user.getUsername(), user.getPassword(), currentTimeMillis);
                RedisUtil.set(token1, JSON.toJSON(map),TokenUtil.REFRESH_EXPIRE_TIME);
                RedisUtil.del(token);
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                httpServletResponse.setHeader("Authorization", token1);
                httpServletResponse.setHeader("Access-Control-Expose-Headers", "Authorization");
                return true;
            }
        }
        return false;
   }
//    private boolean refreshToken(ServletRequest request, ServletResponse response) {
//        String token = ((HttpServletRequest) request).getHeader("token");
//        String username = TokenUtil.getUsername(token);
//
//        String password = TokenUtil.getPassword(token);
//
//
//        Long currentTime = TokenUtil.getCurrentTime(token);
//
//        // 判断Redis中RefreshToken是否存在
//        if (RedisUtil.hasKey(token)) {
//            // Redis中RefreshToken还存在，获取RefreshToken的时间戳
//
//            // = RedisUtil.get(token);
//            //System.out.println("jsonObject===>"+jsonObject);
//
//            Long currentTimeMillisRedis =(Long) RedisUtil.get(token);
//           // Long currentTimeMillisRedis = (Long) RedisUtil.get(token);
//            //JSONObject jsonObject =(JSONObject) RedisUtil.get(username);
//           // String currentTimeMillisRedis = jsonObject.getString("currentTimeMillis");
//            System.out.println("currentTimeMillisRedis==>"+currentTimeMillisRedis);
//            System.out.println("currentTime<><>"+currentTime);
//            boolean equals = currentTimeMillisRedis.equals(currentTime);
//            System.out.println("equals=="+equals);
//            // 获取当前AccessToken中的时间戳，与RefreshToken的时间戳对比，如果当前时间戳一致，进行AccessToken刷新
//            if (currentTimeMillisRedis.equals(currentTime)) {
//                System.out.println("equals=="+equals);
//                // 获取当前最新时间戳
//                Long currentTimeMillis = System.currentTimeMillis();
//                System.out.println("currentTimeMillis....."+currentTimeMillis);
//                System.out.println("username~~~~"+username);
//               // User user = userService.selectByUsername(username);
//                User user = userMapper.selectByUsername(username);
//                System.out.println("user===<><>"+user);
//                Map<String, Object> map = new HashMap<>();
//                map.put("user", user);
//                redisTemplate.opsForValue().set(username,JSON.toJSONString(map),12, TimeUnit.HOURS);
//                // 刷新AccessToken，设置时间戳为当前最新时间戳
//                token = TokenUtil.sign(user.getUsername(), user.getPassword(), currentTimeMillis);
//                RedisUtil.set(token, currentTimeMillis,TokenUtil.REFRESH_EXPIRE_TIME);
//                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//                httpServletResponse.setHeader("Authorization", token);
//                httpServletResponse.setHeader("Access-Control-Expose-Headers", "Authorization");
//                return true;
//            }
//        }
//        return false;
//    }

    private void responseError(ServletResponse response, String msg) {

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(401);
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json;charset=UTF-8");
        try {
            String rj = new ObjectMapper().writeValueAsString(new Result(401, msg));
            httpResponse.getWriter().append(rj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
