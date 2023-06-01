package com.eaosoft.railway.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eaosoft.railway.entity.Equip;
import com.eaosoft.railway.entity.LoginLog;
import com.eaosoft.railway.entity.User;
import com.eaosoft.railway.service.IEquipService;
import com.eaosoft.railway.service.ILoginLogService;
import com.eaosoft.railway.service.IUserService;
import com.eaosoft.railway.utils.*;
import com.eaosoft.railway.vo.RespVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/railway/login")
public class LoginController {
    @Autowired
    RedisUtil redisUtil;

    @Autowired
    IEquipService equipService;

    @Autowired
    private ILoginLogService loginLogService;

    @Autowired
    IUserService userService;

    @RequestMapping("/login.do")
    @ResponseBody
    public RespValue login(@RequestBody ReqValue reqValue, HttpServletResponse response, HttpServletRequest request) throws JsonProcessingException {
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");

        // 登录日志信息设置
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(username);
        loginLog.setCallerName(reqValue.getCallerName());
        loginLog.setCreateTime(LocalDateTime.now());
        loginLog.setPath("/login.do");

        // 获取登陆者的IP
        loginLog.setIpAddr(IPUtils.getIpAddr(request));

        //去数据库拿密码验证用户名密码
        User user = userService.login(username, MD5Utils.md5(password));
        // 判断该账户是否存在
        if (user != null) {

            // 判断该账户是否被冻结
            if (user.getState().equals(0)){
                // 登录失败设置登陆状态
                loginLog.setState("失败");
                // 将登录日志添加到数据库
                loginLogService.insertLoginLog(loginLog);

                return new RespValue(500,"The account has been frozen",null);
            }

            Long currentTimeMillis = System.currentTimeMillis();
            String token = TokenUtil.sign(username, password, currentTimeMillis);

            Map<String, Object> map = new HashMap<>();
            map.put("user", user);
            map.put("currentTimeMillis", currentTimeMillis);
            redisUtil.set(token, JSON.toJSON(map), TokenUtil.REFRESH_EXPIRE_TIME);
            //redisUtil.set(username, JSON.toJSON(map), TokenUtil.EXPIRE_TIME);
            // 设置token过期时间为12小时
            RedisUtil.set(username, JSON.toJSON(map), 12 * 60 * 60);

            // 登录成功设置登陆状态
            loginLog.setState("成功");
            // 将登录日志添加到数据库
            loginLogService.insertLoginLog(loginLog);

            // RedisUtil.set(username,JSON.toJSON(map),30);
            // redisTemplate.opsForValue().set(username, JSON.toJSONString(map),12,TimeUnit.HOURS);
            response.setHeader("Authorization", token);
            response.setHeader("Access-Control-Expose-Headers", "Authorization");
            RespVo respVo = new RespVo();
            respVo.setData(user);
            respVo.setToken(token);
            return new RespValue(200, "success", respVo);
        }

        loginLog.setState("失败");
        // 将登录日志添加到数据库
        loginLogService.insertLoginLog(loginLog);

        return new RespValue(500, "The account or password is incorrect ", null);
    }

    /**
     *
     * 设备登录
     *
     * @param reqValue
     * @param response
     * @param request
     * @return
     */
    @PostMapping("/equipLogin.do")
    public RespValue equipLogin(@RequestBody ReqValue reqValue, HttpServletResponse response, HttpServletRequest request) {
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        String username = jsonObject.getString("username");

        // 登录日志信息设置
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(username);
        loginLog.setCallerName(reqValue.getCallerName());
        loginLog.setCreateTime(LocalDateTime.now());
        loginLog.setPath("/equipLogin.do");



        Equip equip = equipService.equipLogin(username);
        if (equip != null){
            Long currentTimeMillis = System.currentTimeMillis();

            String token = TokenUtil.sign(username, equip.getUid(), currentTimeMillis);

            Map<String, Object> map = new HashMap<>();
            map.put("equip", equip);
            map.put("currentTimeMillis", currentTimeMillis);
            redisUtil.set(token, JSON.toJSON(map), TokenUtil.REFRESH_EXPIRE_TIME);
            //redisUtil.set(username, JSON.toJSON(map), TokenUtil.EXPIRE_TIME);
            // 设置token过期时间为12小时
            RedisUtil.set(username, JSON.toJSON(map), 12 * 60 * 60);


            // 登录成功设置登陆状态
            loginLog.setState("成功");
            // 将登录日志添加到数据库
            loginLogService.insertLoginLog(loginLog);


            response.setHeader("Authorization", token);
            response.setHeader("Access-Control-Expose-Headers", "Authorization");
            RespVo respVo = new RespVo();
            respVo.setData(equip);
            respVo.setToken(token);
            return new RespValue(200,"success",respVo);
        }
        // 登录失败设置登陆状态
        loginLog.setState("失败");
        // 将登录日志添加到数据库
        loginLogService.insertLoginLog(loginLog);

        return new RespValue(500,"The account or password is incorrect",null);
    }


}
