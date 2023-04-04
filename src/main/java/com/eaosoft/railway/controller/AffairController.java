package com.eaosoft.railway.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.eaosoft.railway.entity.Affair;
import com.eaosoft.railway.entity.Station;
import com.eaosoft.railway.service.IAffairService;
import com.eaosoft.railway.service.IStationService;
import com.eaosoft.railway.utils.ReqValue;
import com.eaosoft.railway.utils.RespValue;
import com.github.pagehelper.PageInfo;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 事件管理 前端控制器
 * </p>
 *
 * @author zzs
 * @since 2023-04-04
 */
@RestController
@RequestMapping("/railway/affair")
public class AffairController {
    @Autowired
    private IAffairService affairService;

    @Autowired
    private IStationService stationService;

    /**
     * 创建事件
     *
     * @param reqValue
     * @return
     */

    @PostMapping("/addAffair.do")
    public RespValue addAffair(@RequestBody ReqValue reqValue) {
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        Affair affair = new Affair();
        if (StringUtils.isBlank(jsonObject.getString("stationUid"))) {
            return new RespValue(500, "The stationUid cannot empty", null);
        }
        if (StringUtils.isBlank(jsonObject.getString("stationExitUid"))) {
            return new RespValue(500, "The stationExitUid cannot empty", null);
        }
        if (StringUtils.isBlank(jsonObject.getString("routeName"))) {
            return new RespValue(500, "The routeName cannot empty", null);
        }
        if (StringUtils.isBlank(jsonObject.getString("userUid"))) {
            return new RespValue(500, "The userUid cannot empty", null);
        }
        if (StringUtils.isBlank(jsonObject.getString("equipSerial"))) {
            return new RespValue(500, "The equipSerial cannot empty", null);
        }

        if (!StringUtils.isBlank(jsonObject.getString("details"))) {
            affair.setDetails(jsonObject.getString("details"));
        }

        affair.setStationUid(jsonObject.getString("stationUid"));
        affair.setStationExitUid(jsonObject.getString("stationExitUid"));
        affair.setRouteName(jsonObject.getString("routeName"));
        affair.setUserUid(jsonObject.getString("userUid"));
        affair.setEquipSerial(jsonObject.getString("equipSerial"));

        affair.setCreatTime(LocalDateTime.now());
        int i = affairService.addAffair(affair);
        if (i != 0) {
            return new RespValue(200, "success", null);
        }
        return new RespValue(501, "Event creation failure", null);

    }


    /**
     * 分页查询事件，如有条件则根据条件查询
     *
     * @param reqValue
     * @return
     */

    @PostMapping("/findAffair.do")
    public RespValue findAffair(@RequestBody ReqValue reqValue) {
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        Integer pageSize = jsonObject.getInteger("pageSize");
        Integer currentPage = jsonObject.getInteger("currentPage");
        String stationName = jsonObject.getString("stationName");
        // 如果站点名不为空，查询出站点信息
        if (!StringUtils.isBlank(stationName)) {
            Station station = stationService.findStation(stationName);
            PageInfo<Affair> list =
                    affairService.findAffairByStationName(currentPage, pageSize, station.getUid());
            return new RespValue(200, "success", list);
        }

        // 如果站点名为空，则直接查询所有事件
        PageInfo<Affair> list = affairService.findAffair(currentPage, pageSize);
        return new RespValue(200, "success", list);
    }
}
