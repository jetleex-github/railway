package com.eaosoft.shiro.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eaosoft.railway.entity.LoginLog;
import com.eaosoft.railway.entity.User;

import com.eaosoft.railway.service.ILoginLogService;
import com.eaosoft.railway.service.IUserService;

import com.eaosoft.railway.utils.IPUtils;
import com.eaosoft.railway.utils.ReqValue;
import com.eaosoft.railway.utils.RespValue;
import com.eaosoft.railway.vo.RespVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/railway/login")
public class LoginController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Resource
    private IUserService userService;

    @Autowired
    private ILoginLogService loginLogService;


    @RequestMapping("/login.do")
    public RespValue login(@RequestBody ReqValue reqValue, HttpServletRequest request) {
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));

        // 登录日志信息设置
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(jsonObject.getString("username"));
        loginLog.setCallerName(reqValue.getCallerName());
        loginLog.setCreateTime(LocalDateTime.now());
        loginLog.setPath("/login.do");

        // 获取登陆者的IP
        loginLog.setIpAddr(IPUtils.getIpAddr(request));


        Subject subject = SecurityUtils.getSubject();
        try {
            // 设置token
            UsernamePasswordToken token = new UsernamePasswordToken(jsonObject.getString("username"), jsonObject.getString("password"));
            //交于shiro自定义realm中的认证方法
            subject.login(token);

            //生成token. (1)UUID生成一个唯一id并把该值保存到redis中 [2]通过JWT来生成token.[(1)头信息 (2)载体 (3)密钥]
            String uuid = UUID.randomUUID().toString().replace("-", "");

            //登录成功后的用户信息
            User user = (User) subject.getPrincipal();

            // 登录成功设置登陆状态
            loginLog.setState("登录成功");
            // 将登录日志添加到数据库
            loginLogService.insertLoginLog(loginLog);

           /*  user.setPassword(null);
           //根据用户id查询该用户具有的权限。
            List<Permission> permissions = permissionOpenFeign.findPermissionByUserid(user.getUserid());
            List<String> path = new ArrayList<>();
            for (Permission permission : permissions) {
                if (permission != null && permission.getPath() != null & permission.getPath() != "") {
                    path.add(permission.getPath());
                }
                if (permission != null && permission.getPasspath() != null & permission.getPasspath() != "") {
                    path.add(permission.getPasspath());
                }
            }
            //getData():LinkedHashMap
            Map<String, Object> map = new HashMap<>();
            map.put("user", user);
            map.put("permissions", path);
            */
            Map<String, Object> map = new HashMap<>();
            map.put("user", user);
            // 保存在redis中 value应该为包含用户信息以及该用户具有的权限
            redisTemplate.opsForValue().set(uuid, JSON.toJSONString(map), 24, TimeUnit.HOURS);
            RespVo respVo = new RespVo();
            respVo.setData(user);
            respVo.setToken(uuid);
            return new RespValue(200, "登录成功", respVo);
        } catch (Exception e) {
            // 登录失败设置登陆状态
            loginLog.setState("登录失败");
            // 将登录日志添加到数据库
            loginLogService.insertLoginLog(loginLog);
            e.printStackTrace();
            return new RespValue(500, "账号或密码错误", null);
        }
    }


}


