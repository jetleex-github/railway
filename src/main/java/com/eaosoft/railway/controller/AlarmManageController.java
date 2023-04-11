package com.eaosoft.railway.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.eaosoft.railway.entity.AlarmManage;
import com.eaosoft.railway.entity.Equip;
import com.eaosoft.railway.service.IAlarmManageService;
import com.eaosoft.railway.service.IEquipService;
import com.eaosoft.railway.utils.ReqValue;
import com.eaosoft.railway.utils.RespValue;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zzs
 * @since 2023-04-07
 */
@RestController
@RequestMapping("/railway/alarmManage")
public class AlarmManageController {

    @Autowired
    private IAlarmManageService alarmManageService;

    @Autowired
    private IEquipService equipService;

    /**
     * 添加警报记录
     *
     * @param reqValue
     * @return
     */
    @PostMapping("/insertAlarm.do")
    public RespValue insertAlarm(@RequestBody ReqValue reqValue) {
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        AlarmManage alarm = new AlarmManage();
        alarm.setEquipSerial(jsonObject.getString("serialNo"));
        // 通过设备序列号查找设备信息
        Equip equip = equipService.findEquipBySerialNo(alarm.getSerialNo());
        alarm.setStationExitUid(equip.getStationExitUid());
        alarm.setStationUid(equip.getStationUid());
        alarm.setEquipName(equip.getEquipName());
        if (jsonObject.getInteger("grade") != null) {
            alarm.setGrade(jsonObject.getInteger("grade"));
        }
        if (StringUtils.isBlank(jsonObject.getString("ipAddr"))) {
            return new RespValue(500, "The ipAddr cannot empty", null);
        }
        alarm.setIpAddr(jsonObject.getString("ipAddr"));
        if (!StringUtils.isBlank(jsonObject.getString("content"))) {
            alarm.setContent(jsonObject.getString("content"));
        }
        alarm.setCreateTime(LocalDateTime.now());
        int i = alarmManageService.insertAlarm(alarm);

        return null;

    }

    /**
     * 删除警报记录
     *
     * @param reqValue
     * @return
     */
    @PostMapping("/deleteAlarm.do")
    public RespValue deleteAlarm(@RequestBody ReqValue reqValue) {
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        String uid = jsonObject.getString("uid");
        int i = alarmManageService.deleteAlarm(uid);
        if (i != 0) {
            return new RespValue(200, "success", null);
        }
        return new RespValue(500, "error", null);
    }


    /**
     * 查询报警记录,
     * 若有设备名，则根据设备名查询
     *
     * @param reqValue
     * @return
     */
    @PostMapping("/selectAlarm.do")
    public RespValue selectAlarm(@RequestBody ReqValue reqValue) {
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        Integer pageSize = jsonObject.getInteger("pageSize");
        Integer currentPage = jsonObject.getInteger("currentPage");
        String equipName = jsonObject.getString("equipName");
        PageInfo<AlarmManage> pageInfo = alarmManageService.selectAlarm(currentPage, pageSize, equipName);
        return new RespValue(200, "success", pageInfo);
    }

    /**
     * 查询未处理的警报
     *
     * @param reqValue
     * @return
     */
    @PostMapping("/selectAlarmByState.do")
    public RespValue selectAlarmByState(@RequestBody ReqValue reqValue) {
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        Integer pageSize = jsonObject.getInteger("pageSize");
        Integer currentPage = jsonObject.getInteger("currentPage");
        PageInfo<AlarmManage> pageInfo = alarmManageService.selectAlarmByState(currentPage, pageSize);
        return new RespValue(200, "success", pageInfo);
    }


    /**
     * 处理警报
     *
     * @param reqValue
     * @return
     */
    @PostMapping("/dealAlarm")
    public RespValue dealAlarm(@RequestBody ReqValue reqValue) {
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));


        return null;
    }


}
