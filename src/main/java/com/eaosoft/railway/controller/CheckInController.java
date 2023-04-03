package com.eaosoft.railway.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.eaosoft.railway.entity.CheckIn;
import com.eaosoft.railway.service.ICheckInService;
import com.eaosoft.railway.service.IUserService;
import com.eaosoft.railway.utils.ReqValue;
import com.eaosoft.railway.utils.RespValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zzs
 * @since 2023-03-31
 */
@RestController
@RequestMapping("/railway/checkIn")
public class CheckInController {

    @Autowired
    private ICheckInService checkInService;

    @Autowired
    private IUserService userService;


    /**
     * 上班打卡
     * @param reqValue
     * @return
     */
    @PostMapping("/addCheckIn.do")
    public RespValue addCheckIn(@RequestBody ReqValue reqValue){
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        CheckIn checkIn = new CheckIn();
        checkIn.setUserUid(jsonObject.getString("uid"));
        checkIn.setSerialNo(jsonObject.getString("serialNo"));
        checkIn.setWorkday(LocalDateTime.now());
        // 判断今日是否已打卡
        CheckIn checkIn1= checkInService.findCheckIn(checkIn);
        if (checkIn1 != null){// 已打卡，不可重复打卡
            return new RespValue(500,"Don't clock in repeatedly",null);
        }

        // 未打卡，添加打卡记录
        checkIn.setOnTime(LocalDateTime.now());

        int i =checkInService.addCheckIn(checkIn);
        if (i != 0){
            return new RespValue(200,"success",null);
        }
        return new RespValue(500,"error",null);
    }


    /**
     * 下班打卡
     * @param reqValue
     * @return
     */
    @PostMapping("/addCheckOut")
    private RespValue addCheckOut(@RequestBody ReqValue reqValue){
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        CheckIn checkIn = new CheckIn();
        checkIn.setUserUid(jsonObject.getString("uid"));
        checkIn.setSerialNo(jsonObject.getString("serialNo"));
        checkIn.setOffTime(LocalDateTime.now());
        checkIn.setWorkday(LocalDateTime.now());
        CheckIn check = checkInService.findCheckIn(checkIn);
        // 判断是否重复下班打卡
        if (check.getOffTime() != null){
            return new RespValue(500,"Don't clock in repeatedly",null);
        }
        // 添加下班打卡记录
        int i =checkInService.addCheckOut(checkIn);
        if (i != 0){
            return new RespValue(200,"success",null);
        }
        return new RespValue(500,"error",null);
    }


     @PostMapping("/findAttendance")
    public RespValue findAttendance(){
        // 查询需要打卡的人数
        userService.findUserNum();


         // checkInService.findNotCheckIn()

         return null;
     }



}
