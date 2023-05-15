package com.eaosoft.railway.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.eaosoft.railway.entity.AlarmManage;
import com.eaosoft.railway.entity.Equip;
import com.eaosoft.railway.service.IAlarmManageService;
import com.eaosoft.railway.service.IEquipService;
import com.eaosoft.railway.utils.ReqValue;
import com.eaosoft.railway.utils.RespValue;
import com.eaosoft.railway.vo.AlarmVo;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

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
        String serialNo = jsonObject.getString("serialNo");
        // System.out.println("serialNo======>"+serialNo);
        alarm.setEquipSerial(jsonObject.getString("serialNo"));
        // 通过设备序列号查找设备信息
        Equip equip = equipService.findEquipBySerialNo(alarm.getEquipSerial());
        alarm.setStationExitUid(equip.getStationExitUid());
        alarm.setStationUid(equip.getStationUid());
        alarm.setEquipName(equip.getEquipName());

        if (jsonObject.getInteger("grade") != null) {
            alarm.setGrade(jsonObject.getInteger("grade"));
        }
        if (!StringUtils.isBlank(jsonObject.getString("ipAddr"))) {
            alarm.setIpAddr(jsonObject.getString("ipAddr"));
           // return new RespValue(500, "The ipAddr cannot empty", null);
        }
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
    @PostMapping("/dealAlarm.do")
    public RespValue dealAlarm(@RequestBody ReqValue reqValue) {
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        AlarmManage alarmManage = new AlarmManage();
        alarmManage.setUid(jsonObject.getString("uid"));
        if (StringUtils.isBlank(jsonObject.getString("userUid"))) {
            return new RespValue(500, "The userUid cannot empty", null);
        }
        alarmManage.setUserUid(jsonObject.getString("userUid"));
        alarmManage.setDealTime(LocalDateTime.now());
        if (!StringUtils.isBlank(jsonObject.getString("solution"))) {
            alarmManage.setSolution(jsonObject.getString("solution"));
        }
        int i = alarmManageService.dealAlarm(alarmManage);
        if (i != 0) {
            return new RespValue(200, "success", null);
        }
        return new RespValue(500, "error", null);
    }

    /**
     * 安检门报警导出
     * @param response
     * @param stationUid
     */
    @GetMapping("/alarmInfoExport.do")
    //public void alarmInfoExport(HttpServletResponse response, @RequestBody ReqValue reqValue){
    public void alarmInfoExport(HttpServletResponse response,  String stationUid){
       /* Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        String stationUid = jsonObject.getString("stationUid");*/
        // 获取报警数据
        List<AlarmVo> list = alarmManageService.alarmInfoExport(stationUid);

        // 获取消息头
        response.setHeader("content-disposition","attachment;filename=alarmInfoExport_"+System.currentTimeMillis()+".xlsx");
        // 生成excel并导出
        try {
            EasyExcel.write(response.getOutputStream(), AlarmVo.class).sheet("智能识别设备报警数据导出").doWrite(list);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


  /*  @PostMapping("/invoke.do")
    public void invoke( @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        EasyExcel.read(file.getInputStream(), AlarmVo.class, new MemberExcelListener()).sheet().doRead();
    }*/






}
