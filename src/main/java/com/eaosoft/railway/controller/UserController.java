package com.eaosoft.railway.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.eaosoft.railway.entity.Role;
import com.eaosoft.railway.entity.Station;
import com.eaosoft.railway.entity.User;
import com.eaosoft.railway.service.IRoleService;
import com.eaosoft.railway.service.IStationService;
import com.eaosoft.railway.service.IUserService;
import com.eaosoft.railway.utils.MD5Utils;
import com.eaosoft.railway.utils.ReqValue;
import com.eaosoft.railway.utils.RespValue;
import com.eaosoft.railway.utils.TokenUtil;
import com.eaosoft.railway.vo.UserVo;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
    private IStationService stationService;

    @Autowired
    private IRoleService roleService;


    /**
     * 随机生成用户编号
     * @return
     */
    public String getUserSerial() {
        Boolean a = true;
        String substring = "";
        while (a) {
            String str = String.valueOf(System.currentTimeMillis());
            // 取后四位为编号
            substring = str.substring(str.length() - 4);
            // 验证编号是否重复
            int i = userService.findSerialNo(substring);
            if (i == 0) {
                a = false;
            }
        }
        return substring;
    }

    /**
     * 添加用户
     *
     * @param reqValue
     * @return
     */
    @RequestMapping("/addUser.do")

    public RespValue addUser(@RequestBody ReqValue reqValue) {
        User user1 = new User();
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        // 判断用户名是否为空
        String username = jsonObject.getString("username");

        if (StringUtils.isBlank(jsonObject.getString("username"))) {
            return new RespValue(500, "The username cannot empty", null);
        }else {
            // 用户名不为空，判断用户名是否存在
            User user = userService.selectByUsername(username);
            if (user != null) {
                return new RespValue(500, "The username already exists", null);
            }
            user1.setUsername(username);
        }

        // 判断密码是否为空
        if (StringUtils.isBlank(jsonObject.getString("password"))){
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
                user1.setPosition("999");
                String userSerial = getUserSerial();
                user1.setSerialNo("G" + userSerial);
            } else {
                // 添加普通员工，添加所在线路和站点名称
                user1.setRouteName(jsonObject.getString("routeName"));

                if (jsonObject.getString("station") == ""
                        || jsonObject.getString("station") == null) {
                    return new RespValue(500, "The station name cannot be empty", null);
                }

                // 判断该站点名称是否存在
                Station station = stationService.findStation(jsonObject.getString("station"));
                if (station == null){
                    return new RespValue(500,"The station name does not exist!",null);
                }
                user1.setStationUid(station.getUid());

                // 添加员工职位
                if (jsonObject.getString("position") == null || jsonObject.getString("position") == "") {
                    return new RespValue(500, "The position cannot be empty", null);
                }
                Role role = roleService.findRoleByPosition(jsonObject.getString("position"), "五号线");
                user1.setPosition(String.valueOf(role.getCaption()));

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
            List<User> user2 = userService.findByIdCard(jsonObject.getString("idCard"));
           // System.out.println("user2========>"+user2);
            if (user2.size() != 0) {
                return new RespValue(500, "The idCard already exist", null);
            }
            user1.setIdCard(jsonObject.getString("idCard"));
        }

        // 判断邮箱是否存在
        if (!StringUtils.isBlank(jsonObject.getString("email"))) {
            List<User> u = userService.findByEmail(jsonObject.getString("email"));
            if (u.size() != 0) {
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
   // @RequiresPermissions("findByUsername")
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
     * @param request
     * @return
     */
    @PostMapping("/logout.do")
    public RespValue logout(HttpServletRequest request) {
        String token = request.getHeader("token");
        String username = TokenUtil.getUsername(token);
        // 在redis中删除该信息
        redisTemplate.delete(username);
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
        // 判断用户名是否为空
        if (StringUtils.isBlank(jsonObject.getString("username"))){
            return new RespValue(500,"The username cannot empty!",null);
        }
        // 判断用户名是否存在
        User user = userService.selectByUsername(jsonObject.getString("username"));
        if (!user.getUid().equals(jsonObject.getString("uid"))){
            return new RespValue(500,"The username already exist",null);
        }
        user1.setUsername(jsonObject.getString("username"));
        Role role = roleService.findRoleByPosition(jsonObject.getString("position"), "五号线");
        // jsonObject.getInteger("position")
        user1.setPosition(String.valueOf(role.getCaption()));
        user1.setGender(jsonObject.getInteger("gender"));
        user1.setAge(jsonObject.getInteger("age"));
        user1.setPhone(jsonObject.getString("phone"));
        user1.setAddress(jsonObject.getString("address"));
        user1.setUpdateTime(LocalDateTime.now());

        // 查询该站点是否存在
        Station station = stationService.findStation(jsonObject.getString("station"));
        if (station == null){
            return new RespValue(500,"The station name does not exist!",null);
        }
        user1.setStationUid(station.getUid());

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
       
        User user = new User();

        user.setRouteName(jsonObject.getString("routeName"));
        //user.setStationUid(jsonObject.getString("stationUid"));
     //   System.out.println("routeName==>"+user.getRouteName()+",stationUid==>"+user.getStationUid());
        PageInfo<User> users = userService.findAllUserByRouteNameAndStationUid(pageSize,currentPage,user);
//        PageHelper.startPage(currentPage,pageSize);
//        List<User> users = userService.findAll(jsonObject.getString("routeName"));
//        for (int i = 0; i < users.size(); i++) {
//
//        }
      //  PageInfo pageInfo = new PageInfo(users);
//        for (int i = 0; i < pageInfo.getPageSize(); i++) {
//            stationService.find(pageInfo.)
//        }

    //    System.out.println("users---->"+users);
        return new RespValue(200, "success", users);
    }


    /**
     * 根据token获取用户信息
     *
     * @param
     * @return
     */
    @PostMapping("/findUserInfoByToken.do")
    public RespValue findUserInfoByToken( HttpServletRequest request) {
        String token = request.getHeader("token");
        //String token = ((HttpServletRequest) request).getHeader("token");
        // 根据用户token在redis中获取用户信息
        String username = TokenUtil.getUsername(token);
        // 根据用户token在redis中获取用户信息
        String s = redisTemplate.opsForValue().get(username);
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


    /**
     * 批量导入用户信息模板导出
     *
     * @param response
     */
    @GetMapping("/exportModel.do")
    public void exportModel(HttpServletResponse response) {
        List<User> list = userService.exportModel("1638081879957639170");

        // 将站点uid换成站点名称
        for (int i = 0; i < list.size(); i++) {
            String stationUid = list.get(i).getStationUid();
            Station station = stationService.findStationByUid(stationUid);
            list.get(i).setStationUid(station.getStationName());
        }
        // 获取消息头
        response.setHeader("content-disposition", "attachment;filename=userModel_" + System.currentTimeMillis() + ".xlsx");
        // 生成excel并导出
        try {
            EasyExcel.write(response.getOutputStream(), User.class).sheet("用户导入模板").doWrite(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 批量人员信息后，将信息再导出
     *
     * @param file
     * @throws IOException
     */
    @PostMapping("/invokeUser.do")
    public void invokeAdmin(MultipartFile file,
                            HttpServletResponse response) {
        // 通过监听的方式，将人员信息写入数据库
        userService.importUser(file, userService,stationService);

        // 查询今天创建的人员信息
        List<UserVo> userVos = userService.exportUser();
        for (UserVo userVo : userVos) {
            userVo.setPassword("123456");
        }
        // 将站点uid换成站点名称
        for (int i = 0; i < userVos.size(); i++) {
            String stationUid = userVos.get(i).getStationUid();
            if (!StringUtils.isBlank(stationUid)){
                Station station = stationService.findStationByUid(stationUid);
                userVos.get(i).setStationUid(station.getStationName());
            }
        }
//        for (int i = 0; i < userVos.size(); i++) {
//            System.out.println(userVos.get(i));
//        }
        // 获取消息头
        response.setHeader("content-disposition", "attachment;filename=importUser_" + System.currentTimeMillis() + ".xlsx");
        // 生成excel并导出
        try {

            EasyExcel.write(response.getOutputStream(), UserVo.class).sheet("用户信息").doWrite(userVos);

        } catch (IOException e) {

            e.printStackTrace();
        }

    }


    /**
     * 修改身份，将普通人员设为管理员
     * @param reqValue
     * @return
     */
    @PostMapping("/changeIdentity.do")
    public RespValue changeIdentity(@RequestBody ReqValue reqValue) {
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));

        User user = new User();
        user.setUid(jsonObject.getString("userUid"));
        // 修改用户身份状态
        user.setCaption(0);
        user.setPosition("999");
        user.setStationUid("");
        int i = userService.changeIdentity(user);
        if (i != 0) {
            return new RespValue(200,"success",null);
        }
        return new RespValue(500,"error",null);
    }

}


