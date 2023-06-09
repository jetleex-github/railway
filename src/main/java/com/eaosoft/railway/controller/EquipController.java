package com.eaosoft.railway.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.eaosoft.railway.entity.Equip;
import com.eaosoft.railway.entity.Station;
import com.eaosoft.railway.service.IEquipService;
import com.eaosoft.railway.service.IStationService;
import com.eaosoft.railway.utils.ReqValue;
import com.eaosoft.railway.utils.RespValue;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zzs
 * @since 2023-03-27
 */
@RestController
@RequestMapping("/railway/equip")
public class EquipController {

    @Autowired
    private IEquipService equipService;

    @Autowired
    private IStationService stationService;

    /**
     * 根据站点名称查询设备,用于树形结构查看该站点绑定了哪些设备
     *
     * @param reqValue
     * @return
     */
    @PostMapping("/findEquip.do")
    public RespValue findEquip(@RequestBody ReqValue reqValue) {
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        String stationExitUid = jsonObject.getString("uid");
        List<Equip> list = equipService.findEquip(stationExitUid);
        return new RespValue(200, "success", list);
    }

    /**
     * 添加设备到仓库
     *
     * @param reqValue
     * @return
     */
    @PostMapping("/addEquip.do")
    public RespValue addEquip(@RequestBody ReqValue reqValue) {
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        Equip equip = new Equip();
        if (StringUtils.isBlank(jsonObject.getString("equipName"))) {
            return new RespValue(500, "The equipName cannot empty", null);
        }
        equip.setEquipName(jsonObject.getString("equipName"));

        if (!StringUtils.isBlank(jsonObject.getString("serialNo"))) {
            // 判断设备编号存在
            Equip equip1 = equipService.findEquipBySerialNo(jsonObject.getString("serialNo"));
            if (equip1 != null) {// 已存在则重新输入
                return new RespValue(500, "The SerialNo already exits", null);
            }
            equip.setSerialNo(jsonObject.getString("serialNo"));
        } else {
            return new RespValue(500, "The serialNo cannot empty", null);
        }

        equip.setProducer(jsonObject.getString("producer"));
        equip.setStationUid(jsonObject.getString("stationUid"));
        equip.setRouteName(jsonObject.getString("routeName"));
        equip.setCreateTime(LocalDateTime.now());
        equip.setUpdateTime(LocalDateTime.now());

        int i = equipService.addEquip(equip);

        if (i != 0) {
            return new RespValue(200, "success", equip);
        }
        return new RespValue(500, "error", null);
    }


    /**
     * 查找设备类型
     *
     * @param reqValue
     * @return 声音
     * 备注
     * 标注
     */
    @PostMapping("/findEquipName.do")
    public RespValue findEquipName(@RequestBody ReqValue reqValue) {
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        String routeName = jsonObject.getString("routeName");
        List list = equipService.findEquipName(routeName);
        if (list != null) {
            return new RespValue(200, "success", list);
        }
        return new RespValue(201, "There is no device name under this line", null);

    }

    /**
     * 查询未绑定设备,有条件则根据条件查询
     *
     * @param reqValue
     * @return storage
     */
    @PostMapping("/findUnboundEquip.do")
    public RespValue findUnboundEquip(@RequestBody ReqValue reqValue) {
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        String stationUid = jsonObject.getString("stationUid");
        Integer pageSize = jsonObject.getInteger("pageSize");
        Integer currentPage = jsonObject.getInteger("currentPage");
        String equipName = jsonObject.getString("equipName");
        PageInfo<Equip> list = equipService.findUnboundEquip(stationUid, equipName, currentPage, pageSize);
        return new RespValue(200, "success", list);
    }


    /**
     * 绑定设备
     *
     * @param reqValue
     * @return
     */
    @PostMapping("/boundEquip.do")
    public RespValue boundEquip(@RequestBody ReqValue reqValue) {
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        Equip equip = new Equip();

      // 设置站口
        if (StringUtils.isBlank(jsonObject.getString("stationExitUid"))) {
            return new RespValue(500, "The stationExit cannot empty", null);
        }
        equip.setStationExitUid(jsonObject.getString("stationExitUid"));

//        // 判断IP是否为空
//        if (!StringUtils.isBlank(jsonObject.getString("ipAddr"))) {
//            // 判断该IP是否存在
//            Equip e = equipService.findEquipByEquipUid(jsonObject.getString("equipUid"));
//            String ipAddr = jsonObject.getString("ipAddr");
//            Equip equip1 = equipService.findEquipByIp(ipAddr, e.getStationUid());
//            if (equip1 != null && !(equip1.getUid().equals(jsonObject.getString("equipUid")))) {
//                return new RespValue(500, "The ipAddr already exist", null);
//            }
//            equip.setIpAddr(jsonObject.getString("ipAddr"));
//        }


        // 获取设备uid
        if (StringUtils.isBlank(jsonObject.getString("equipUid"))) {
            return new RespValue(500, "The equipUid cannot empty", null);
        }
        equip.setUid(jsonObject.getString("equipUid"));
        // 修改绑定状态
        equip.setCode(1);

        int i = equipService.boundEquip(equip);
        if (i != 0) {
            return new RespValue(200, "success", null);
        }
        return new RespValue(500, "Bound equip is failure", null);
    }


    /**
     * 解除绑定
     *
     * @param reqValue
     * @return
     */
    @PostMapping("/removeBound.do")
    public RespValue removeBound(@RequestBody ReqValue reqValue) {
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        Equip equip = new Equip();
        String uid = jsonObject.getString("equipUid");
        if (StringUtils.isBlank(uid)) {
            return new RespValue(500, "The uid cannot empty", null);
        }
        equip.setUid(uid);
        equip.setStationExitUid("");
        // 修改在线状态为离线
        equip.setState(0);
        //修改绑定状态为未绑定
        equip.setCode(0);

        int i = equipService.removeBound(equip);
        if (i != 0) {
            return new RespValue(200, "success", null);
        }
        return new RespValue(500, "error", null);
    }

    /**
     * 设备报修
     *
     * @param reqValue
     * @return
     */
    @PostMapping("/equipRepair.do")
    public RespValue equipRepair(@RequestBody ReqValue reqValue) {
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        String equipUid = jsonObject.getString("equipUid");
        int i = equipService.equipRepair(equipUid);
        if (i != 0) {
            return new RespValue(200, "success", null);
        }
        return new RespValue(500, "error", null);
    }

    /**
     * 删除设备
     *
     * @param reqValue
     * @return
     */
    @PostMapping("/equipDel.do")
    public RespValue equipDel(@RequestBody ReqValue reqValue) {
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        String equipUid = jsonObject.getString("equipUid");
        int i = equipService.equipDel(equipUid);
        if (i != 0) {
            return new RespValue(200, "success", null);
        }
        return new RespValue(500, "error", null);
    }

    /**
     * 修改设备信息
     *
     * @param reqValue
     * @return
     */
    @PostMapping("/updateEquip.do")
    public RespValue updateEquip(@RequestBody ReqValue reqValue) {
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        Equip equip = new Equip();
        if (StringUtils.isBlank(jsonObject.getString("equipUid"))) {
            return new RespValue(500, "The uid cannot empty", null);
        }
        equip.setUid(jsonObject.getString("equipUid"));
        equip.setProducer(jsonObject.getString("producer"));

//        // 判断该IP是否存在
//        Equip e = equipService.findEquipByEquipUid(jsonObject.getString("equipUid"));
//        String ipAddr = jsonObject.getString("ipAddr");
//        Equip equip1 = equipService.findEquipByIp(ipAddr,e.getStationUid());
//        if (equip1 != null && !(equip1.getUid().equals(jsonObject.getString("equipUid")))){
//            return new RespValue(500,"The ipAddr already exist",null);
//        }
//        equip.setIpAddr(jsonObject.getString("ipAddr"));

        int i = equipService.updateEquip(equip);
        if (i != 0) {
            return new RespValue(200, "success", null);
        }
        return new RespValue(500, "error", null);
    }

    /**
     * 根据条件查询该站点下的设备
     *
     * @param reqValue
     * @return
     */
    @PostMapping("/selectEquipByCondition.do")
    public RespValue selectEquipByCondition(@RequestBody ReqValue reqValue) {
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        String stationName = jsonObject.getString("stationName");
        // 判断站点名称是否存在
        if (StringUtils.isBlank(stationName)) {
            return new RespValue(500, "The stationName cannot empty", null);
        }
        // 通过站点名称获取站点信息
        Station station = stationService.findStation(stationName);
        String stationUid = station.getUid();

        Equip equip = new Equip();
        equip.setStationUid(stationUid);
        equip.setEquipName(jsonObject.getString("equipName"));
        equip.setSerialNo(jsonObject.getString("serialNo"));
        equip.setState(jsonObject.getInteger("state"));
        Integer pageSize = jsonObject.getInteger("pageSize");
        Integer currentPage = jsonObject.getInteger("currentPage");
        PageInfo<Equip> pageInfo = equipService.selectEquipByCondition(equip, pageSize, currentPage);
        return new RespValue(200, "success", pageInfo);
    }

    /**
     * 查询该线路下所有设备信息
     *
     * @param reqValue
     * @return
     */
    @PostMapping("/findAllEquip.do")
    public RespValue findAllEquip(@RequestBody ReqValue reqValue) {
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        String routeName = jsonObject.getString("routeName");
        Integer pageSize = jsonObject.getInteger("pageSize");
        Integer currentPage = jsonObject.getInteger("currentPage");
        String serialNo = jsonObject.getString("serialNo");
        Integer state = jsonObject.getInteger("state");
        String equipName = jsonObject.getString("equipName");
        PageInfo<Equip> pageInfo = equipService.findAllEquip(pageSize, currentPage, routeName,serialNo,state,equipName);
        return new RespValue(200, "success", pageInfo);
    }
}
