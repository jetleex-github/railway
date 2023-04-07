package com.eaosoft.railway.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.eaosoft.railway.entity.LoginLog;
import com.eaosoft.railway.entity.User;
import com.eaosoft.railway.service.ILoginLogService;
import com.eaosoft.railway.service.IUserService;
import com.eaosoft.railway.utils.MD5Utils;
import com.eaosoft.railway.utils.ReqValue;
import com.eaosoft.railway.utils.RespValue;
import com.github.pagehelper.PageInfo;

import java.time.LocalDateTime;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;


import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zzs
 * @since 2023-03-17
 */
@RestController
@RequestMapping("/railway/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ILoginLogService loginLogService;




    /*@PostMapping("/findUserNum")
    public RespValue findUserNum(){
        int num =userService.findUserNum();
        return new RespValue(200,"success",num);
    }*/


    /**
     * 登录
     * @param reqValue
     * @return
     */
/*
    @RequestMapping("/login.do")
    public RespValue login(@RequestBody ReqValue reqValue){
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        String s = MD5Utils.md5(password);
        User user = userService.login(username, s);
        if (user != null){
            if (user.getState() == 0){
                return new RespValue(500,"This account has been frozen",null);
            }
            user.setPassword(password);
            String token = userService.getToken(user);
            RespVo respVo = new RespVo();
            respVo.setData(user);
            respVo.setToken(token);
            return new RespValue(200,"success",respVo);
        }
        return new RespValue(500,"The account or password is incorrect",null);
    }
*/

    /**
     * addUser
     *
     * @param reqValue
     * @return
     */
    @RequestMapping("/addUser.do")
    public RespValue addUser(@RequestBody ReqValue reqValue) {
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        // 判断用户名是否为空
        String username = jsonObject.getString("username");
        if (StringUtils.isBlank(jsonObject.getString("username"))) {
            return new RespValue(500, "The username cannot be empty", null);
        }

        // 判断用户名是否存在
        User user = userService.selectByUsername(username);
        if (user != null) {
            return new RespValue(500, "The username already exists", null);
        }

        User user1 = new User();
        user1.setUsername(jsonObject.getString("username"));

        if (StringUtils.isBlank(jsonObject.getString("password"))) {
            return new RespValue(500, "The password cannot be empty", null);
        }
        // Encrypt the password with MD5
        user1.setPassword(MD5Utils.md5(jsonObject.getString("password")));

        // 判断身份是否合法
        if (jsonObject.getInteger("caption").equals(0) || jsonObject.getInteger("caption").equals(1)) {
            user1.setCaption(jsonObject.getInteger("caption"));

            // 判断添加的人员身份
            if (jsonObject.getInteger("caption").equals(0)) {
                // 添加管理员，只添加所在线路，不添加站点
                user1.setRouteName(jsonObject.getString("routeName"));
                user1.setPosition("管理员");

                //根据当前时间，设置用户编号
                Boolean a = true;
                while (a) {
                    String str = String.valueOf(System.currentTimeMillis());
                    // 取后四位为编号
                    String substring = str.substring(str.length() - 4);
                    user1.setSerialNo("G" + substring);
                    // 验证编号是否重复
                    User u = userService.findSerialNo(user1.getSerialNo());
                    if (u == null) {

                        a = false;
                    }
                }
            } else {
                // 添加普通员工，添加所在线路和站点名称
                user1.setRouteName(jsonObject.getString("routeName"));

                if (jsonObject.getString("station") == ""
                        || jsonObject.getString("station") == null) {
                    return new RespValue(500, "The station  cannot be empty", null);
                }
                user1.setStation(jsonObject.getString("station"));
                // 添加员工职位
                if (jsonObject.getString("position") == null || jsonObject.getString("position") == "") {
                    return new RespValue(500, "The position cannot be empty", null);
                }
                user1.setPosition(jsonObject.getString("position"));


                //根据当前时间，设置用户编号
                Boolean a = true;
                while (a) {
                    String str = String.valueOf(System.currentTimeMillis());
                    // 取后四位为编号
                    String substring = str.substring(str.length() - 4);
                    user1.setSerialNo("P" + substring);
                    // 验证编号是否重复
                    User u = userService.findBySerialNo(user1.getSerialNo());
                    if (u == null) {
                        a = false;
                    }
                }
            }
        } else {
            return new RespValue(500, "The identity selection error", null);
        }
        // 判断性别是否合法
        if (jsonObject.getInteger("gender").equals(0) || jsonObject.getInteger("gender").equals(1)) {
            user1.setGender(jsonObject.getInteger("gender"));
        } else {
            return new RespValue(500, "The gender selection error", null);
        }
        // 判断年龄是否设置
        if (!(jsonObject.getInteger("age") == null)) {
            // 判断年龄是否合法
            if (jsonObject.getInteger("age") < 18 || jsonObject.getInteger("age") > 99) {
                return new RespValue(500, "The age  error", null);
            }
            user1.setAge(jsonObject.getInteger("age"));
        }
        // 判断是否设置手机号
        if (!StringUtils.isBlank(jsonObject.getString("phone"))) {
            user1.setPhone(jsonObject.getString("phone"));
        }
        // 判断是否设置住址
        if (!StringUtils.isBlank(jsonObject.getString("address"))) {
            user1.setAddress(jsonObject.getString("address"));
        }
        if (!StringUtils.isBlank(jsonObject.getString("realName"))) {
            user1.setRealName(jsonObject.getString("realName"));
        }

        // 判断身份证号是否存在
        if (!StringUtils.isBlank(jsonObject.getString("idCard"))) {

            // 判断身份证号是否重复
            User user2 = userService.findByIdCard(jsonObject.getString("idCard"));
            if (user2 != null) {
                return new RespValue(500, "The idCard already exist", null);
            }
            user1.setIdCard(jsonObject.getString("idCard"));
        }

        // 判断邮箱是否存在
        if (!StringUtils.isBlank(jsonObject.getString("email"))) {
            User u = userService.findByEmail(jsonObject.getString("email"));
            if (u != null) {
                return new RespValue(500, "The email already exist", null);
            }
            user1.setEmail(jsonObject.getString("email"));
        }


        user1.setCreateTime(LocalDateTime.now());
        user1.setUpdateTime(LocalDateTime.now());

        int i = userService.addUser(user1);

        if (i != 0) {
            return new RespValue(200, "success", user1);
        }
        return new RespValue(500, "Adding a user fails", null);
    }


    /**
     * 根据用户名，使用模糊查询该用户是否存在
     *
     * @param reqValue
     * @return
     */
    @PostMapping("/findByUsername.do")
    public RespValue findByUsername(@RequestBody ReqValue reqValue) {
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        String username = jsonObject.getString("username");
        Integer pageSize = jsonObject.getInteger("pageSize");
        Integer currentPage = jsonObject.getInteger("currentPage");
        PageInfo<User> user = userService.findByUsername(currentPage, pageSize, username);
        if (user != null) {
            return new RespValue(200, "success", user);
        }
        return new RespValue(500, "Change the user does not exist", null);

    }

    /**
     * logout 退出
     *
     * @param reqValue
     * @return
     */
    @PostMapping("/logout.do")
    public RespValue logout(@RequestBody ReqValue reqValue) {
        String token = reqValue.getToken();
        /*// 根据用户token在redis中获取用户信息
        String s = redisTemplate.opsForValue().get(token);
        Map map = JSON.parseObject(s, Map.class);
        JSONObject user = (JSONObject) map.get("user");

        // 通过用户名获取用户信息
        User user1 = userService.selectByUsername((String) user.get("username"));

        // 设置退出登录信息到
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(user1.getUsername());
        loginLog.setCallerName(reqValue.getCallerName());
        loginLog.setCreateTime(LocalDateTime.now());
        loginLog.setIpAddr(user1.getAddress());
        loginLog.setPath("/logout.do");*/


        // 在redis中删除该信息
        redisTemplate.delete(token);
        return new RespValue(200, "success", null);
    }


    /**
     * 修改用户信息
     *
     * @param reqValue
     * @return
     */
    @RequestMapping("/modifyUserInfo.do")
    public RespValue modifyUserInfo(@RequestBody ReqValue reqValue) {
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        User user1 = new User();
        user1.setUid(jsonObject.getString("uid"));
        user1.setUsername(jsonObject.getString("username"));
        user1.setCaption(jsonObject.getInteger("caption"));
        user1.setGender(jsonObject.getInteger("gender"));
        user1.setAge(jsonObject.getInteger("age"));
        user1.setPhone(jsonObject.getString("phone"));
        user1.setAddress(jsonObject.getString("address"));
        user1.setUpdateTime(LocalDateTime.now());
        user1.setStation(jsonObject.getString("station"));
        int i = userService.modifyUserInfo(user1);
        if (i != 0) {
            return new RespValue(200, "success", user1);
        }
        return new RespValue(500, "error", null);
    }

    /**
     * 修改用户密码
     *
     * @param reqValue
     * @return
     */
    @PostMapping("/modifyPassword.do")
    public RespValue modifyPassword(@RequestBody ReqValue reqValue) {
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        User user = new User();
        user.setUid(jsonObject.getString("uid"));
        if (StringUtils.isBlank(jsonObject.getString("password"))) {
            return new RespValue(500, "The password cannot empty", null);
        }
        user.setPassword(MD5Utils.md5(jsonObject.getString("password")));
        int i = userService.modifyPassword(user);
        if (i != 0) {
            return new RespValue(200, "success", null);
        }
        return new RespValue(500, "error", null);
    }


    /**
     * 查询该站点下的所有用户
     *
     * @param reqValue
     * @return
     */
    @PostMapping("/findAllUser.do")
    public RespValue findAllUser(@RequestBody ReqValue reqValue) {
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        Integer pageSize = jsonObject.getInteger("pageSize");
        Integer currentPage = jsonObject.getInteger("currentPage");
        System.out.println("pageSize==>" + pageSize + ",currentPage==>" + currentPage);
        User user = new User();

        user.setRouteName(jsonObject.getString("routeName"));
        PageInfo<User> pageInfo = userService.findAll(currentPage, pageSize, user);
        // PageInfo<User> pageInfo = userService.findAllUser(currentPage,pageSize,user);

        return new RespValue(200, "success", pageInfo);
    }


    /**
     * 根据token获取用户信息
     *
     * @param reqValue
     * @return
     */
    @PostMapping("/findUserInfoByToken.do")
    public RespValue findUserInfoByToken(@RequestBody ReqValue reqValue, HttpServletRequest request) {
        String token = reqValue.getToken();
        // 根据用户token在redis中获取用户信息
        String s = redisTemplate.opsForValue().get(token);
        Map map = JSON.parseObject(s, Map.class);
        JSONObject user = (JSONObject) map.get("user");

        // 通过用户名获取用户信息
        User user1 = userService.selectByUsername((String) user.get("username"));
        if (user1 != null) {
            return new RespValue(200, "success", user1);
        }
        return new RespValue(500, "Token invalid ", null);
    }


    /**
     * 员工离职
     *
     * @param reqValue
     * @return
     */
    @PostMapping("/deleteUser.do")
    public RespValue deleteUser(@RequestBody ReqValue reqValue) {
        Object requestDatas = reqValue.getRequestDatas();
        int i = userService.batchDelete(requestDatas);
        if (i != 0) {
            return new RespValue(200, "success", null);
        }
        return new RespValue(500, "error", null);
    }


    /**
     * 改变账号状态，0-禁用，1-正常
     *
     * @param reqValue
     * @return
     */
    @PostMapping("/changeState.do")
    public RespValue changeState(@RequestBody ReqValue reqValue) {
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        User user = new User();
        user.setUid(jsonObject.getString("uid"));
        user.setState(jsonObject.getInteger("state"));
        int i = userService.changeState(user);
        if (i != 0) {
            return new RespValue(200, "success", null);
        }
        return new RespValue(500, "error", null);
    }


}


