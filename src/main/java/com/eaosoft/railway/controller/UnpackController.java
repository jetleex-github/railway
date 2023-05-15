package com.eaosoft.railway.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.eaosoft.railway.entity.Unpack;
import com.eaosoft.railway.service.IStationService;
import com.eaosoft.railway.service.IUnpackService;
import com.eaosoft.railway.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 开包结果 前端控制器
 * </p>
 *
 * @author zzs
 * @since 2023-05-04
 */
@RestController
@RequestMapping("/railway/unpack")
public class UnpackController {

    @Autowired
    private IUnpackService unpackService;

    @Autowired
    private IStationService stationService;

    /**
     * 添加开包结果
     *
     * @param files
     * @param taskUid
     * @param username
     * @param cardId
     * @return
     */
    @PostMapping("/addUnpackInfo.do")
    public RespValue addUnpackInfo(MultipartFile[] files, String taskUid, String username, String cardId) {
        Unpack unpack = new Unpack();
        unpack.setTaskUid(taskUid);
        unpack.setUsername(username);
        unpack.setCardId(cardId);
        String image = "";
        for (MultipartFile file : files) {
            try {
                // 将开包的图片上传至服务器，并保存照片地址
                image = UploadUtils.upload(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 添加照片地址
            unpack.setImage(image);
            unpack.setCreateTime(LocalDateTime.now());
            // 保存开包信息
            int i = unpackService.addUnpackInfo(unpack);
        }
        return null;
    }

    /**
     * 查询开包记录
     *
     * @param
     * @return
     */
    @PostMapping("/findUnpackInfo.do")
    public RespValue findUnpackInfo(@RequestBody ReqValue reqValue){
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        Integer pageSize = jsonObject.getInteger("pageSize");
        Integer currentPage = jsonObject.getInteger("currentPage");
        String stationName = jsonObject.getString("stationName");
        String createTime = jsonObject.getString("createTime");
        if (StringUtils.isBlank(stationName)){

        }
        //Station station = stationService.findStation(stationName);

        // jsonObject.getDate()

        System.out.println("pageSize:"+pageSize+",currentPage"+currentPage+",stationName"+stationName+",createTime"+createTime);
        List<Unpack> list = unpackService.findUnpackInfo(pageSize,currentPage,stationName,createTime);
        System.out.println("list====>"+list);
        return new RespValue(200,"success",list);
    }

//    public RespValue findUnpackInfo(HttpServletRequest request) {
//        String data = (String) request.getAttribute("data");
//        JSONObject jsonObject = JSONObject.parseObject(data);
//        // Object requestDatas = reqValue.getRequestDatas();
//        // JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
//        Integer pageSize = jsonObject.getInteger("pageSize");
//        Integer currentPage = jsonObject.getInteger("currentPage");
//        String stationName = jsonObject.getString("stationName");
//        String createTime = jsonObject.getString("createTime");
//        if (StringUtils.isBlank(stationName)) {
//
//        }
//        //Station station = stationService.findStation(stationName);
//
//        // jsonObject.getDate()
//
//        System.out.println("pageSize:" + pageSize + ",currentPage" + currentPage + ",stationName" + stationName + ",createTime" + createTime);
//        List<Unpack> list = unpackService.findUnpackInfo(pageSize, currentPage, stationName, createTime);
//        System.out.println("list====>" + list);
//        return new RespValue(200, "success", list);
//    }


}
