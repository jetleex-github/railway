package com.eaosoft.railway.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.eaosoft.railway.entity.Unpack;
import com.eaosoft.railway.service.IStationService;
import com.eaosoft.railway.service.IUnpackService;
import com.eaosoft.railway.utils.*;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public RespValue addUnpackInfo(@RequestParam("file") MultipartFile[] files, String taskUid, String username, String cardId) {
        for (MultipartFile file : files) {
            Unpack unpack = new Unpack();
            unpack.setTaskUid(taskUid);
            unpack.setUsername(username);
            unpack.setCardId(cardId);
            String image = "";
            try {
                // 将开包的图片上传至服务器，并保存照片地址
                image = UploadUtils.upload(file);
                // 添加照片地址
                unpack.setImage(image);

                unpack.setCreateTime(LocalDateTime.now());
                unpack.setUpdateTime(LocalDateTime.now());
                // 保存开包信息
                unpackService.addUnpackInfo(unpack);
            } catch (Exception e) {
                e.printStackTrace();
                return new RespValue(500, "error", null);
            }
        }
        return new RespValue(200, "success", null);
    }

    /**
     * 查询开包记录
     *
     * @param
     * @return
     */
    @PostMapping("/findUnpackInfo.do")
    public RespValue findUnpackInfo(@RequestBody ReqValue reqValue) {
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        Integer pageSize = jsonObject.getInteger("pageSize");
        Integer currentPage = jsonObject.getInteger("currentPage");
        String stationName = jsonObject.getString("stationName");
        String createTime = jsonObject.getString("createTime");

        // System.out.println("pageSize:" + pageSize + ",currentPage" + currentPage + ",stationName" + stationName + ",createTime" + createTime);
        PageInfo<Unpack> list = unpackService.findUnpackInfo(pageSize, currentPage, stationName, createTime);
        return new RespValue(200, "success", list);
    }

    /**
     * 根据开包任务uid查询上传的图片
     *
     * @param reqValue
     * @return
     */
    @PostMapping("/findPictureByTaskUid.do")
    public RespValue findPictureByTaskUid(@RequestBody ReqValue reqValue) {
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        List<Unpack> list = unpackService.findPictureByTaskUid(jsonObject.getString("taskUid"));
        // 判断是否存在该任务
        if (list == null){
            return new RespValue(500,"The Incorrect task uid ",null);
        }

        Map<Integer,String> map = new HashMap();
        // 存在该任务
        for (int i = 0; i < list.size(); i++) {
            // 判断是否存在照片
            if (!StringUtils.isBlank(list.get(i).getImage())){
                try {
                    String localImage = UploadUtils.getLocalImage(list.get(i).getImage());
                    map.put(i,localImage);
                } catch (IOException e) {
                    return new RespValue(500,"error",null);
                }
            }
        }
        return new RespValue(200,"success",map);
    }


}
