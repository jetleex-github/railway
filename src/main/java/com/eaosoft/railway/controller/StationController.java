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
import com.eaosoft.railway.vo.StationExitVo;
import com.eaosoft.railway.vo.StationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zzs
 * @since 2023-03-24
 */
@RestController
@RequestMapping("/railway/station")
public class StationController {

    @Autowired
    private IStationService stationService;

    @Autowired
    private IStationExitService stationExitService;


    /**
     * 查询该线路下的信息，用于树形菜单查找站点和站口；
     * @param reqValue
     * @return
     */
    @PostMapping("/find.do")
    public RespValue find(@RequestBody ReqValue reqValue){
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        String routeName = jsonObject.getString("routeName");
        // 查询所有的站点信息
        List<StationVo> list = stationService.findAllStation(routeName);

        // 查询所有的站口信息
        List<StationExitVo> exit = stationExitService.findAllExit();
        for (int i = 0; i < list.size(); i++) {
        /*List<StationExitVO> child =new ArrayList<>();*/
            List<StationExitVo> child = new ArrayList<>();
            for (int j = 0; j < exit.size(); j++) {
                // 把站口信息放入对应的站点中
                if (list.get(i).getUid().equals(exit.get(j).getStationUid())){
                    child.add(exit.get(j));
                }
            }
            list.get(i).setChildren(child);
        }
        return new RespValue(200,"success",list);
    }


    /**
     * 查找该线路下的所有站点信息，
     *  用于添加用户时选择站点；
     * @param reqValue
     * @return
     */
    @PostMapping("/findAllStation.do")
    public RespValue findAllStation(@RequestBody ReqValue reqValue){
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        String routeName = jsonObject.getString("routeName");

        List list  = stationService.findAllStation(routeName);

        return new RespValue(200,"success",list);
    }


    /**
     * 添加站点
     * @param reqValue
     * @return
     */
    @PostMapping("/addStation.do")
    public RespValue addStation(@RequestBody ReqValue reqValue){
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        // 判断输入的站点名是否为空
        if (StringUtils.isBlank(jsonObject.getString("station"))){
            return new RespValue(500,"The station cannot empty",null);
        }
        if (StringUtils.isBlank(jsonObject.getString("routeName"))){
            return new RespValue(500,"The routeName cannot empty",null);
        }
        // 判断站点名是否已存在
        String stationName = jsonObject.getString("station");
        String routeName = jsonObject.getString("routeName");

        Station s =  stationService.findByName(stationName,routeName);
        if (s!=null){
            return new RespValue(500,"The station name already exist",null);
        }
        // 设置站点信息
        Station station = new Station();
        station.setStationName(jsonObject.getString("station"));
        station.setRouteName(jsonObject.getString("routeName"));
        station.setCreateTime(LocalDateTime.now());
        station.setUpdateTime(LocalDateTime.now());
        int i = stationService.addStation(station);
        if (i!=0){
            return new RespValue(200,"success",null);
        }
        return new RespValue(500,"error",null);
    }

    /**
     * 修改站点信息
     * @param reqValue
     * @return
     */
    @PostMapping("/modifyStationInfo.do")
    public RespValue modifyStationInfo(@RequestBody ReqValue reqValue){
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));

        String stationName = jsonObject.getString("station");
        String routeName = jsonObject.getString("routeName");
        // 判断站点名是否为空
        if (StringUtils.isBlank(stationName)){
            return new RespValue(500,"The station name cannot empty",null);
        }
        // 判断线路名是否为空
        if (StringUtils.isBlank(routeName)){
            return new RespValue(500,"The routeName cannot empty",null);
        }
        // 判断站点名是否存在
        Station s =  stationService.findByName(stationName,routeName);
        if (s!=null){
            return new RespValue(500,"The station name already exist",null);
        }

        Station station = new Station();
        station.setStationName(stationName);
        station.setUid(jsonObject.getString("uid"));
        int i = stationService.modifyStationInfo(station);
        if (i!=0){
            return new RespValue(200,"success",null);
        }
        return new RespValue(500,"error",null);
    }

    /**
     * 删除站点
     * @param reqValue
     * @return
     */
    @PostMapping("/deleteStation.do")
    public RespValue deleteStation(@RequestBody ReqValue reqValue){
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        String uid = jsonObject.getString("uid");
        int i = stationService.deleteStation(uid);
        if (i!=0){
            return new RespValue(200,"success",null);
        }
        return new RespValue(500,"error",null);
    }


}
