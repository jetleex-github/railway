package com.eaosoft.railway.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.eaosoft.railway.entity.Picture;
import com.eaosoft.railway.entity.User;
import com.eaosoft.railway.service.IPictureService;
import com.eaosoft.railway.service.IUserService;
import com.eaosoft.railway.service.impl.UserServiceImpl;
import com.eaosoft.railway.utils.ReqValue;
import com.eaosoft.railway.utils.RespValue;
import com.eaosoft.railway.utils.UploadUtils;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;


/**
 * <p>
 * 判图系统 前端控制器
 * </p>
 *
 * @author zzs
 * @since 2023-04-10
 */
@RestController
@RequestMapping("/railway/picture")
public class PictureController {

    @Autowired
    //UserServiceImpl userService;
    IUserService userService;

    private static Map<String, SseEmitter> sseCache = new ConcurrentHashMap<>();
    private static String fileSavePath;

    // 通过配置文件，映射图片上传到服务器的根路径
    @Value("${file.save.path}")
    public void setFileSavePath(String path) {
        PictureController.fileSavePath = path;
    }

    @Autowired
    private IPictureService pictureService;

    /**
     * 添加需要判图的图片
     *
     * @param rightFile
     * @param leftFile
     * @param frontFile
     */
    @PostMapping("/insertPictures.do")
    public void insertPictures01(
            @RequestParam(value = "rightFile", required = false) MultipartFile rightFile,
            @RequestParam(value = "leftFile", required = false) MultipartFile leftFile,
            @RequestParam(value = "frontFile", required = false) MultipartFile frontFile,
            String stationUid) {

        // 获取照片存储的地址
        String rightUrl = "";
        String leftUrl = "";
        String frontUrl = "";
        try {
            if (rightFile != null) {
                rightUrl = UploadUtils.upload(rightFile);
            }
            if (leftFile != null) {
                leftUrl = UploadUtils.upload(leftFile);
            }
            if (frontUrl != null) {
                frontUrl = UploadUtils.upload(frontFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Picture picture = new Picture();

        picture.setRightPicture(rightUrl);
        picture.setLeftPicture(leftUrl);
        picture.setFrontPicture(frontUrl);
        picture.setStationUid(stationUid);

        // 设置创建时间
        picture.setCreateTime(LocalDateTime.now());

        pictureService.insertPictures(picture);

        try {
            selectPictureByStationUid(stationUid);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("picture====>" + picture);


    }

    /**
     * 每秒向前端发送一条消息
     *
     * @return
     */
    @RequestMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> sse(@RequestBody ReqValue reqValue) {
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        String stationUid = jsonObject.getString("stationUid");
        List<Picture> pictureList = pictureService.selectPictureByStationUid(stationUid);
        // List list = pictureService.findPicture();

       /* while (true){
            List<Picture> pictureList = pictureService.selectPictureByStationUid(stationUid);
            return Flux.interval(Duration.ofSeconds(1))
                    .map(sequence -> )
                    //.map(sequence -> ServerSentEvent.builder("SSE-" + pictureList).build());
        }*/
        //return Flux.interval(Duration.ofSeconds(1)).map(sequence -> ServerSentEvent.builder("SSE - " + sequence).build());
        return Flux.interval(Duration.ofSeconds(1))
                .map(sequence -> ServerSentEvent
                        .builder("SSE - " + pictureService.selectPictureByStationUid(stationUid))
                        .build());

    }





    /**
     * 添加判图结果
     *
     * @param multipartFile
     * @param request
     * @return
     */
    @PostMapping("/addResult.do")
    @ResponseBody
    public RespValue add(@RequestParam(value = "file", required = false) MultipartFile multipartFile,
                         HttpServletRequest request) {
        String voiceUrl = "";
        // 判断是否有语音传入
        if (multipartFile != null) {
            // 如果有语音把语音存到服务器，并返回地址
            try {
                // 获取语音储存地址
                voiceUrl = UploadUtils.upload(multipartFile);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        Picture picture = new Picture();

        // 添加坐标
        if (StringUtils.isBlank(request.getParameter("coordinate"))) {
            return new RespValue(500, "error", "The coordinate cannot empty");
        }
        picture.setLabel(request.getParameter("coordinate"));
        picture.setVoice(voiceUrl);
        picture.setUid(request.getParameter("uid"));
        // 如果存在文字备注则添加
        if (!StringUtils.isBlank(request.getParameter("remark"))) {
            picture.setRemark(request.getParameter("remark"));
        }
        // 修改照片状态为以判图
        picture.setFlag(1);
        int i = pictureService.addResult(picture);
        if (i != 0) {
            return new RespValue(200, "success", null);
        }
        return null;
    }


    /**
     * 查询该站点需要判图的图片
     *
     * @param
     * @return
     */
    /* @PostMapping("/selectPictureByStationUid.do")
    public RespValue selectPictureByStationUid(@RequestBody ReqValue reqValue) {
        // 获取当前系统的分隔符 \ 或者是 /
        String SEPARATOR = File.separator;

        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        String stationUid = jsonObject.getString("stationUid");
        List<Picture> pictureList = pictureService.selectPictureByStationUid(stationUid);

        // 拼接上文件的根目录地址
        for (Picture picture : pictureList) {
            if (!StringUtils.isBlank(picture.getLeftPicture())) {
                picture.setLeftPicture(fileSavePath + SEPARATOR + picture.getLeftPicture());
            }
            if (!StringUtils.isBlank(picture.getFrontPicture())) {
                picture.setFrontPicture(fileSavePath + SEPARATOR + picture.getFrontPicture());
            }
            if (!StringUtils.isBlank(picture.getRightPicture())) {
                picture.setRightPicture(fileSavePath + SEPARATOR + picture.getRightPicture());
            }
        }
        return new RespValue(200, "success", pictureList);
    }*/

     // @PostMapping("/selectPictureByStationUid.do")
    public void selectPictureByStationUid(String stationUid) throws IOException {
        // 获取当前系统的分隔符 \ 或者是 /
        String SEPARATOR = File.separator;


       /* Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        String stationUid = jsonObject.getString("stationUid");*/
        List<Picture> pictureList = pictureService.selectPictureByStationUid(stationUid);

        // 拼接上文件的根目录地址
        for (Picture picture : pictureList) {
            if (!StringUtils.isBlank(picture.getLeftPicture())) {
                picture.setLeftPicture(fileSavePath + SEPARATOR + picture.getLeftPicture());
            }
            if (!StringUtils.isBlank(picture.getFrontPicture())) {
                picture.setFrontPicture(fileSavePath + SEPARATOR + picture.getFrontPicture());
            }
            if (!StringUtils.isBlank(picture.getRightPicture())) {
                picture.setRightPicture(fileSavePath + SEPARATOR + picture.getRightPicture());
            }
        }
        //将查询到的数据发送给前端
        push(stationUid,pictureList);
       // return new RespValue(200, "success", pictureList);
    }




    /**
     * 用于SSE链接
     * @param userUid
     * @return
     */
    // 获取
    @PostMapping(path = "subscribe")
    public SseEmitter push11(String userUid) {

        // 超时时间设置为1小时
        SseEmitter sseEmitter = new SseEmitter(3600_000L);


        // 根据userUid 查找stationUid
        String stationUid=userService.findStationUidByUserUid(userUid);

        // 将stationUid作为key放入map中，设置连接缓存
        sseCache.put(stationUid, sseEmitter);
        try {
            // 根据stationUid查询该站点需要进行判断的信息
            selectPictureByStationUid(stationUid);
        } catch (IOException e) {
            e.printStackTrace();
        }

        sseEmitter.onTimeout(() -> sseCache.remove(stationUid));
        sseEmitter.onCompletion(() -> System.out.println("完成！！！"));
        return sseEmitter;
    }



    public String push(String stationUid, List<Picture> content) throws IOException {
        SseEmitter sseEmitter = sseCache.get(stationUid);
        System.out.println("stationUid===>"+stationUid);
        System.out.println("content001===>"+content);

        System.out.println("seeEmitter===>"+sseEmitter);
        System.out.println();
        if (sseEmitter != null) {

            System.out.println("content===>"+content);
            sseEmitter.send(content);
        }
        return "over";
    }


}
