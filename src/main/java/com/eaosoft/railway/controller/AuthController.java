package com.eaosoft.railway.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.eaosoft.JWT.annotation.UserLoginToken;
import com.eaosoft.JWT.util.TokenModel;
import com.eaosoft.exception.OneException;
import com.eaosoft.railway.entity.User;
import com.eaosoft.railway.service.AuthTokenService;
import com.eaosoft.railway.service.IUserService;
import com.eaosoft.railway.utils.ReqValue;
import com.eaosoft.railway.utils.RespValue;
import com.eaosoft.railway.utils.Result;
import com.eaosoft.railway.utils.SignUpModel;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author ZhouWenTao
 * @Date 2022/7/14 13:16
 * @Version 1.0
 */

@RequestMapping("/auth")
@RestController
@Slf4j
public class AuthController {
    @Resource
    private IUserService userService;
    @Resource
    private AuthTokenService authTokenService;
    private final String PW_PATTERN  = "^(?![A-Za-z0-9]+$)(?![a-z0-9\\W]+$)(?![A-Za-z\\W]+$)(?![A-Z0-9\\W]+$)[a-zA-Z0-9\\W]{8,}$";

/**
     * 插件登录接口，如果用户名没有注册，则返回303错误
     * @param reqValue
     * @return
     * @throws Exception
     *//*

    @PostMapping("/sginIn")
    @ResponseBody
    public RespValue sginIn(@RequestBody ReqValue reqValue) throws Exception {
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
       */
/* String s = AES256.aesDecodeStr(reqValue.getReqValue(), null);
        JSONObject jsonObject = JSONObject.parseObject(s);
        SignUpModel requestDatas = jsonObject.getObject("requestDatas", SignUpModel.class);*//*

        String userName = jsonObject.getString("username");
       */
/* if(userName.length() < 6){
            respValue.setRespValue(AES256.reloadResult(Result.error("The user name is invalid, the user name require is minimum 6 English or numbers or characters")));
            return respValue;
        }*//*

        User userByUserName = userService.selectByUsername(userName);
        if(userByUserName==null){
            //用户名不存在，报303错误
            throw new OneException(303,"You haven't registered yet DigiEnter, Please complete the registration first");
        }
        //判断密码不为null
        if(StringUtils.isBlank(jsonObject.getString("password"))){
            // respValue.setRespValue(AES256.reloadResult(Result.error("Please input a password")));
            return new RespValue(500,"Please input a password",null);
        }*/
/*else{
            boolean matches = requestDatas.getPassWord().matches(PW_PATTERN);
            if(!matches){
                respValue.setRespValue( AES256.reloadResult(Result.error("The password is invalid, the password require is 8-24 contains at least one lowercase letter, one uppercase letter, one digit, and one special letter.")));
                return respValue;
            }
        }*//*

        if (!userByUserName.getPassword().equals(jsonObject.getString("password"))) {
            //respValue.setRespValue( AES256.reloadResult(Result.error("Wrong account or password, please re-enter")));
            return new RespValue(500,"Password is error",null);
        }
        */
/*if(userByUserName.getUserName().equals("admin") || (StringUtils.isNotBlank(userByUserName.getIsAdmin()) && userByUserName.getIsAdmin().equals("1"))){
            respValue.setRespValue(AES256.reloadResult(Result.error("The administrator account cannot log in to the plug-in")));
            return respValue;
        }*//*

        User user = new User();
        BeanUtils.copyProperties(userByUserName,user);
        String token = userService.getToken(user);
        Result result = new Result();
        JSONObject jsonObject1 = new JSONObject();
       */
/* jsonObject1.put("firstName",user.getFirstName());
        jsonObject1.put("lastName",user.getLastName());*//*

        jsonObject1.put("token",token);
        result.setData(jsonObject1.toString());
        result.setSuccess(true);
        */
/*respValue.setRespValue(result);
        return respValue;*//*

        return new RespValue(500,"success",result);
    }

    */
/**
     * 后台登录接口
     * @param reqValue
     * @return
     * @throws Exception
     *//*

   */
/* @PostMapping("/signInBack")
    @ResponseBody
    public RespValue sginInBack(@RequestBody ReqValue reqValue) throws Exception {
        RespValue respValue=new RespValue();
        String s = AES256.aesDecodeStr(reqValue.getReqValue(), null);
        JSONObject jsonObject = JSONObject.parseObject(s);
        SignUpModel requestDatas = jsonObject.getObject("requestDatas", SignUpModel.class);
        String userName = requestDatas.getUserName();
        User userByUserName = userService.findUserByUserName(userName);
        if(userByUserName==null){
//            respValue.setRespValue(AES256.reloadResult(Result.error("userName error")));
            respValue.setRespValue(AES256.reloadResult(Result.error("You haven't registered yet DigiEnter, Please complete the registration first")));
            return respValue;
        }
        if(!userByUserName.getUserName().equals("admin")){
            if(StringUtils.isBlank(userByUserName.getIsAdmin()) || userByUserName.getIsAdmin().equals("0")){
                respValue.setRespValue(AES256.reloadResult(Result.error("This account does not have background permission")));
                return respValue;
            }
        }
        if (!userByUserName.getPassWord().equals(requestDatas.getPassWord())) {
//            respValue.setRespValue( AES256.reloadResult(Result.error("passWord error")));
            respValue.setRespValue( AES256.reloadResult(Result.error("Incorrect login or password")));
            return respValue;
        }
        User user = new User();
        BeanUtils.copyProperties(userByUserName,user);
        String token = userService.getToken(user);
        Result result = new Result();
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("firstName",user.getFirstName());
        jsonObject1.put("lastName",user.getLastName());
        jsonObject1.put("token",token);
        result.setData(jsonObject1.toString());
        respValue.setRespValue(AES256.reloadResult(result));
        return respValue;
    }*//*


    */
/**
     * 注册接口
     * signUp
     * @param reqValue
     * @return
     *//*

   */
/* @PostMapping("/signUp")
    @ResponseBody
    @SysLog("signUp")
    public RespValue signUp(@RequestBody ReqValue reqValue) throws Exception {
        RespValue respValue=new RespValue();
        String s = AES256.aesDecodeStr(reqValue.getReqValue(), null);
        JSONObject jsonObject = JSONObject.parseObject(s);

        SignUpModel requestDatas = jsonObject.getObject("requestDatas", SignUpModel.class);

        if (StringUtils.isBlank(requestDatas.getUserName())){
            respValue.setRespValue(AES256.reloadResult(Result.error("UserName can`t blank")));
            return respValue;
        }
        if (StringUtils.isBlank(requestDatas.getPassWord())){
            respValue.setRespValue(AES256.reloadResult(Result.error("PassWord can`t blank")));
            return respValue;
        }
        if (StringUtils.isBlank(requestDatas.getFirstName())){
            respValue.setRespValue(AES256.reloadResult(Result.error("FirstName can`t blank")));
            return respValue;
        }
        if (StringUtils.isBlank(requestDatas.getLastName())){
            respValue.setRespValue(AES256.reloadResult(Result.error("LastName can`t blank")));
            return respValue;
        }
        if (StringUtils.isBlank(requestDatas.getEmail())){
            respValue.setRespValue(AES256.reloadResult(Result.error("Email can`t blank")));
            return respValue;
        }

        User user = userService.signUp(requestDatas.getUserName(),
                requestDatas.getPassWord(),requestDatas.getFirstName(),
                requestDatas.getLastName(),requestDatas.getEmail(),"0");
        respValue.setRespValue(AES256.reloadResult(Result.OK()));
        return respValue;
    }*//*


    */
/**
     * 退出登录
     * @param reqValue
     * @return
     * @throws Exception
     */

@PostMapping("/logout")
    public RespValue logout(@RequestBody ReqValue reqValue) throws Exception {
        /*RespValue respValue=new RespValue();
       // String s = AES256.aesDecodeStr(reqValue.getReqValue(), null);
        JSONObject jsonObject = JSONObject.parseObject(s);*/

        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        String token = jsonObject.getString("token");
        authTokenService.deleteToken(token);
       // respValue.setRespValue(Result.OK());
        return new RespValue(200,"success",null);
    }

/**
     * 验证token
     * @param reqValue
     * @return
     * @throws Exception
     *//*

    @PostMapping("/verifyToken")
    @ResponseBody
    public RespValue verifyToken(@RequestBody ReqValue reqValue) throws Exception{
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        String token = jsonObject.getString("token");
        if(StringUtils.isNotBlank(token)){
            TokenModel tokenModel = authTokenService.verifyToken(token);
            respValue.setRespValue(AES256.reloadResult(Result.OK(tokenModel)));
            return new RespValue(200,"success",null);
        }else{TokenModel tokenModel = new TokenModel();
            tokenModel.setAlive(false);
            tokenModel.setToken(null);
            respValue.setRespValue(Result.OK(tokenModel));
            return respValue;
        }
    }

    */
/**
     * 创建用户
     * @param reqValue
     * @return
     *//*

    @PostMapping("/createUser")
    @ResponseBody
    @SysLog("createUser")
    public RespValue createUser(@RequestBody ReqValue reqValue) throws Exception {
        RespValue respValue=new RespValue();
        String s = AES256.aesDecodeStr(reqValue.getReqValue(), null);
        JSONObject jsonObject = JSONObject.parseObject(s);
        SignUpModel requestDatas = jsonObject.getObject("requestDatas", SignUpModel.class);

        if (StringUtils.isBlank(requestDatas.getUserName())){
            respValue.setRespValue(AES256.reloadResult(Result.error("UserName can`t blank")));
            return respValue;
        }
        if (StringUtils.isBlank(requestDatas.getPassWord())){
            respValue.setRespValue(AES256.reloadResult(Result.error("PassWord can`t blank")));
            return respValue;
        }
        if (StringUtils.isBlank(requestDatas.getFirstName())){
            respValue.setRespValue(AES256.reloadResult(Result.error("FirstName can`t blank")));
            return respValue;
        }
        if (StringUtils.isBlank(requestDatas.getLastName())){
            respValue.setRespValue(AES256.reloadResult(Result.error("LastName can`t blank")));
            return respValue;
        }
        if (StringUtils.isBlank(requestDatas.getEmail())){
            respValue.setRespValue(AES256.reloadResult(Result.error("Email can`t blank")));
            return respValue;
        }
        User user = userService.signUp(requestDatas.getUserName(),
                requestDatas.getPassWord(),requestDatas.getFirstName(),
                requestDatas.getLastName(),requestDatas.getEmail(),"0");
        respValue.setRespValue(AES256.reloadResult(Result.OK()));
        return respValue;
    }

    */
/**
     * 修改密码
     * @param request
     * @return
     *//*

    @PostMapping("/updatePwd")
    @ResponseBody
    @UserLoginToken
    @SysLog("updatePwd")
    public RespValue updatePwd(HttpServletRequest request){
        RespValue respValue=new RespValue();
        String data =(String) request.getAttribute("data");
        JSONObject jsonObject = JSONObject.parseObject(data);
        String token = jsonObject.getString("token");
        String userId = JWT.decode(token).getAudience().get(0);
        JSONObject requestDatas = JSONObject.parseObject(jsonObject.getString("requestDatas"));
        String oldPassWord = requestDatas.getString("oldPassword");
        String passWord = requestDatas.getString("password");
        String newPassWord = requestDatas.getString("newPassword");
        if(!passWord.equals(newPassWord)){
            respValue.setRespValue(AES256.reloadResult(Result.error("The passwords were inconsistent twice")));
            return respValue;
        }
        if(StringUtils.isBlank(userId)){
            respValue.setRespValue(AES256.reloadResult(Result.error("Userinfo parse error")));
            return respValue;
        }
        if(StringUtils.isBlank(passWord)){
            respValue.setRespValue(AES256.reloadResult(Result.error("Newpassword can`t blank")));
            return respValue;
        }
        userService.updatePwd(userId,oldPassWord,passWord);
        respValue.setRespValue(AES256.reloadResult(Result.OK()));
        return respValue;
    }

    */
/**
     * 修改后台user密码
     * @param request
     * @return
     *//*

    @PostMapping("/updateUserPwd")
    @ResponseBody
    @UserLoginToken
    @SysLog("updateUserPwd")
    public RespValue updateUserPwd(HttpServletRequest request){
        RespValue respValue=new RespValue();
        String data =(String) request.getAttribute("data");
        JSONObject jsonObject = JSONObject.parseObject(data);
        String token = jsonObject.getString("token");
//        String userId = JWT.decode(token).getAudience().get(0);
        JSONObject requestDatas = JSONObject.parseObject(jsonObject.getString("requestDatas"));
        String userId = requestDatas.getString("userId");
        if(StringUtils.isBlank(userId)){
            respValue.setRespValue(AES256.reloadResult(Result.error("Userinfo parse error")));
            return respValue;
        }
        String randomPwd = userService.updateUserPwd(userId);
        respValue.setRespValue(AES256.reloadResult(Result.OK(randomPwd)));
        return respValue;
    }

    */
/**
     * 忘记密码
     * @param reqValue
     * @return
     * @throws Exception
     *//*

    @PostMapping("/forgotPassword")
    @ResponseBody
    @SysLog("forgotPassword")
    public RespValue forgotPassword(@RequestBody ReqValue reqValue) throws Exception{
        RespValue respValue=new RespValue();
        String s = AES256.aesDecodeStr(reqValue.getReqValue(), null);
        JSONObject jsonObject = JSONObject.parseObject(s);
        JSONObject requestDatas = JSONObject.parseObject(jsonObject.getString("requestDatas"));
        String userName = requestDatas.getString("userName");
        String email = requestDatas.getString("email");
        String isBack = requestDatas.getString("isBack");
        if(StringUtils.isBlank(userName)){
            respValue.setRespValue(AES256.reloadResult(Result.error("UserName can`t blank")));
            return respValue;
        }
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserName,AES256.aesEncryptStr(userName, null));
        User user = userService.getOne(lambdaQueryWrapper);
        if(user == null){
            respValue.setRespValue(AES256.reloadResult(Result.error("Account number or email error")));
            return respValue;
        }
        //判断是否是后台调用
        if (isBack != null){
            if (StringUtils.isBlank(user.getIsAdmin()) || !user.getIsAdmin().equals("1")){
                if (!userName.equals("admin")){
                    respValue.setRespValue(AES256.reloadResult(Result.error("Account does not have permission")));
                    return respValue;
                }
            }
        }
        if(StringUtils.isBlank(email)){
            respValue.setRespValue(AES256.reloadResult(Result.error("Email can`t blank")));
            return respValue;
        }
        if(!user.getEmail().equals(email)){
            respValue.setRespValue(AES256.reloadResult(Result.error("Account number or email error")));
            return respValue;
        }
        String message = userService.sendEmail(userName,email);
        respValue.setRespValue(AES256.reloadResult(Result.OK(message)));
        return respValue;
    }

    @PostMapping("/createAdminUser")
    @UserLoginToken
    @ResponseBody
    public RespValue createAdminUser(@RequestBody ReqValue reqValue){
        RespValue respValue=new RespValue();
        String s = AES256.aesDecodeStr(reqValue.getReqValue(), null);
        JSONObject jsonObject = JSONObject.parseObject(s);

        SignUpModel requestDatas = jsonObject.getObject("requestDatas", SignUpModel.class);

        if (StringUtils.isBlank(requestDatas.getUserName())){
            respValue.setRespValue(AES256.reloadResult(Result.error("UserName can`t blank")));
            return respValue;
        }
        if (StringUtils.isBlank(requestDatas.getPassWord())){
            respValue.setRespValue(AES256.reloadResult(Result.error("PassWord can`t blank")));
            return respValue;
        }
        if (StringUtils.isBlank(requestDatas.getFirstName())){
            respValue.setRespValue(AES256.reloadResult(Result.error("FirstName can`t blank")));
            return respValue;
        }
        if (StringUtils.isBlank(requestDatas.getLastName())){
            respValue.setRespValue(AES256.reloadResult(Result.error("LastName can`t blank")));
            return respValue;
        }
        if (StringUtils.isBlank(requestDatas.getEmail())){
            respValue.setRespValue(AES256.reloadResult(Result.error("Email can`t blank")));
            return respValue;
        }

        User user = userService.signUp(requestDatas.getUserName(),
                requestDatas.getPassWord(),requestDatas.getFirstName(),
                requestDatas.getLastName(),requestDatas.getEmail(),"1");
        respValue.setRespValue(AES256.reloadResult(Result.OK()));
        return respValue;
    }

    */
/**
     * 修改admin密码
     * @param request
     * @return
     *//*

    @PostMapping("/updateAdminUserPwd")
    @UserLoginToken
    @ResponseBody
    public RespValue updateAdminUser(HttpServletRequest request){
        RespValue respValue=new RespValue();
        String data =(String) request.getAttribute("data");
        JSONObject jsonObject = JSONObject.parseObject(data);
        String token = jsonObject.getString("token");
        String userId = JWT.decode(token).getAudience().get(0);
        JSONObject requestDatas = JSONObject.parseObject(jsonObject.getString("requestDatas"));
        String oldPassWord = requestDatas.getString("oldPassword");
        String passWord = requestDatas.getString("password");
        String newPassWord = requestDatas.getString("newPassword");
        if(!passWord.equals(newPassWord)){
            respValue.setRespValue(AES256.reloadResult(Result.error("The passwords were inconsistent twice")));
            return respValue;
        }
        if(StringUtils.isBlank(userId)){
            respValue.setRespValue(AES256.reloadResult(Result.error("Userinfo parse error")));
            return respValue;
        }
        if(StringUtils.isBlank(passWord)){
            respValue.setRespValue(AES256.reloadResult(Result.error("Newpassword can`t blank")));
            return respValue;
        }
        userService.updatePwd(userId,oldPassWord,passWord);
        respValue.setRespValue(AES256.reloadResult(Result.OK()));
        return respValue;
    }

    */
/**
     * 生成随机密码
     * @param request
     * @return
     *//*

    @PostMapping("/autoCreatePassword")
    @UserLoginToken
    @ResponseBody
    public RespValue autoCreatePassword(HttpServletRequest request){
        RespValue respValue=new RespValue();
        String data =(String) request.getAttribute("data");
        JSONObject jsonObject = JSONObject.parseObject(data);
        String token = jsonObject.getString("token");
        String userId = JWT.decode(token).getAudience().get(0);
        String randomPwd = userService.getRandomPwd(8);
        respValue.setRespValue(AES256.reloadResult(Result.OK(randomPwd)));
        return respValue;
    }

    */
/**
     * 删除admin用户
     * @param request
     * @return
     *//*

    @PostMapping("/deleteAdminUser")
    @UserLoginToken
    @ResponseBody
    public RespValue deleteAdminUser(HttpServletRequest request){
        RespValue respValue=new RespValue();
        String data=(String)request.getAttribute("data");
        JSONObject jsonObject = JSONObject.parseObject(data);
        JSONObject requestDatas = jsonObject.getJSONObject("requestDatas");
        String id = requestDatas.getString("userId");
        userService.remove(new LambdaQueryWrapper<User>().eq(User::getUserId,AES256.aesEncryptStr(id,null)));
        respValue.setRespValue(AES256.reloadResult(Result.OK()));
        return respValue;
    }

    */
/**
     * 删除用户
     * @param request
     * @return
     *//*

    @PostMapping("/deleteUser")
    @UserLoginToken
    @ResponseBody
    public RespValue deleteUser(HttpServletRequest request) {
        RespValue respValue = new RespValue();
        String data = (String) request.getAttribute("data");
        JSONObject jsonObject = JSONObject.parseObject(data);
        JSONObject requestDatas = jsonObject.getJSONObject("requestDatas");
        String id = requestDatas.getString("userId");
        userService.remove(new LambdaQueryWrapper<User>().eq(User::getUserId, AES256.aesEncryptStr(id, null)));
        respValue.setRespValue(AES256.reloadResult(Result.OK()));
        return respValue;
    }

    */
/**
     * 清除User_Data_Updated
     * @param request
     * @return
     *//*

    @PostMapping("resetUserDataUpdated")
    @UserLoginToken
    @ResponseBody
    public RespValue resetUserDataUpdated(HttpServletRequest request){
        RespValue respValue=new RespValue();
//        String data =(String) request.getAttribute("data");
//        JSONObject jsonObject = JSONObject.parseObject(data);
//        String token = jsonObject.getString("token");
//        String userId = JWT.decode(token).getAudience().get(0);
//        String s = userService.resetUserDataUpdated(userId);
//        respValue.setRespValue(AES256.reloadResult(Result.OK()));

        String data = (String) request.getAttribute("data");
        JSONObject jsonObject = JSONObject.parseObject(data);
        JSONObject requestDatas = jsonObject.getJSONObject("requestDatas");
        String id = requestDatas.getString("userId");
        String s = userService.resetUserDataUpdated(id);
        respValue.setRespValue(AES256.reloadResult(Result.OK()));
        return respValue;
    }

    */
/**
     * 设置User_Data_Updated
     * @param request
     * @return
     *//*

    @PostMapping("setUserDataUpdated")
    @UserLoginToken
    @ResponseBody
    public RespValue setUserDataUpdated(HttpServletRequest request){
        RespValue respValue=new RespValue();
//        String data =(String) request.getAttribute("data");
//        JSONObject jsonObject = JSONObject.parseObject(data);
//        String token = jsonObject.getString("token");
//        String userId = JWT.decode(token).getAudience().get(0);
//        String s = userService.setUserDataUpdated(userId);
//        respValue.setRespValue(AES256.reloadResult(Result.OK()));

        String data = (String) request.getAttribute("data");
        JSONObject jsonObject = JSONObject.parseObject(data);
        JSONObject requestDatas = jsonObject.getJSONObject("requestDatas");
        String id = requestDatas.getString("userId");
        String s = userService.setUserDataUpdated(id);
        respValue.setRespValue(AES256.reloadResult(Result.OK()));
        return respValue;
    }

    */
/**
     * 根据账户获取User_Data_Updated
     * @param reqValue
     * @return
     *//*

    @PostMapping("getUserDataUpdated")
    @ResponseBody
    public RespValue getUserDataUpdatedf(ReqValue reqValue){
        RespValue respValue=new RespValue();
        String s = AES256.aesDecodeStr(reqValue.getReqValue(), null);
        JSONObject jsonObject = JSONObject.parseObject(s);
        JSONObject requestDatas = JSONObject.parseObject(jsonObject.getString("requestDatas"));
        String userName = requestDatas.getString("userName");
        User userByUserName = userService.findUserByUserName(userName);
        respValue.setRespValue(AES256.reloadResult(Result.OK(userByUserName.getUserDataUpdated())));
        return respValue;
    }*/
}

