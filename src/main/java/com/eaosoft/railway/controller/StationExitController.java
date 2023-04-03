package com.eaosoft.railway.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.eaosoft.railway.entity.Station;
import com.eaosoft.railway.entity.StationExit;
import com.eaosoft.railway.service.IStationExitService;
import com.eaosoft.railway.service.IStationService;
import com.eaosoft.railway.utils.ReqValue;
import com.eaosoft.railway.utils.RespValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zzs
 * @since 2023-03-30
 */
@RestController
@RequestMapping("/railway/stationExit")
public class StationExitController {

    @Autowired
    private IStationExitService stationExitService;


    /**
     * 查询该站点下所有出口名
     * @param reqValue
     * @return
     */
    @PostMapping("/findStationExit.do")
    public RespValue findStationExit(@RequestBody ReqValue reqValue){
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));

        List<Station> list=stationExitService.findExit(jsonObject.getString("stationUid"));
        return new RespValue(200,"success",list);
    }

    /**
     * 添加出站口
     * @param reqValue
     * @return
     */
    @PostMapping("/addStationExit.do")
    public RespValue addStationExit(@RequestBody ReqValue reqValue){
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        if (StringUtils.isBlank(jsonObject.getString("stationUid"))){
            return new RespValue(500,"The stationUid cannot empty!",null);
        }

        StationExit stationExit = new StationExit();
        stationExit.setStationUid(jsonObject.getString("stationUid"));
        stationExit.setStationExit(jsonObject.getString("stationExit"));
        stationExit.setCreateTime(LocalDateTime.now());
        stationExit.setUpdateTime(LocalDateTime.now());
        int i =  stationExitService.addStationExit(stationExit);
        if (i!=0){
            return new RespValue(200,"success",null);
        }
        return new RespValue(500,"error",null);
    }


}
