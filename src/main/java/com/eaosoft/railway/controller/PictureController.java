package com.eaosoft.railway.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.eaosoft.railway.entity.Picture;

import com.eaosoft.railway.service.IPictureService;
import com.eaosoft.railway.service.IUserService;

import com.eaosoft.railway.utils.ReqValue;
import com.eaosoft.railway.utils.RespValue;
import com.eaosoft.railway.utils.UploadUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;

import com.eaosoft.railway.vo.PictureVo;
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

    // 获取当前系统的分隔符 \ 或者是 /
    private final String SEPARATOR = File.separator;

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
     * @param rightFile 右侧照片
     * @param leftFile  左侧照片
     * @param frontFile  正面照片
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
            if (frontFile != null) {
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

        // 查询该站点下是否还存在需要判断的图片
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
   /* @RequestMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> sse(@RequestBody ReqValue reqValue) {
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        String stationUid = jsonObject.getString("stationUid");
       // List<Picture> pictureList = pictureService.selectPictureByStationUid(stationUid);
        // List list = pictureService.findPicture();

        while (true){
            List<Picture> pictureList = pictureService.selectPictureByStationUid(stationUid);
            return Flux.interval(Duration.ofSeconds(1))
                    .map(sequence -> ServerSentEvent.builder("SSE-" + pictureList).build());
        }
        return Flux.interval(Duration.ofSeconds(1))
                .map(sequence -> ServerSentEvent
                        .builder("SSE - " + pictureService.selectPictureByStationUid(stationUid))
                        .build());
    }*/





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
        picture.setUserUid(request.getParameter("userUid"));
        // 修改照片状态为以判图
        picture.setFlag(1);
        int i = pictureService.addResult(picture);

        String stationUid = request.getParameter("stationUid");

        System.out.println("stationUid===>"+stationUid);
        try {
            selectPictureByStationUid(stationUid);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (i != 0) {
            return new RespValue(200, "success", null);
        }
        return null;
    }


    /**
     * 查询该站点等待时间最长的需要判图的图片
     *
     * @param stationUid
     * @return
     */
    public void selectPictureByStationUid(String stationUid) throws IOException {


        // 查询出最先需要判图的照片信息
        Picture picture =  pictureService.selectOneByStationUid(stationUid);

        PictureVo pictureVo = new PictureVo();

        if (picture != null) {
            pictureVo.setStationUid(picture.getStationUid());
            pictureVo.setUid(picture.getUid());
            if (!StringUtils.isBlank(picture.getLeftPicture())) {
                // 拼接完整路径
                //picture.setLeftPicture(fileSavePath + SEPARATOR + picture.getLeftPicture());
                // 获取照片
                File file = new File(picture.getLeftPicture());
                // 转换成base64的字符流
                String leftPicture = getLocalImage(String.valueOf(file));

                pictureVo.setLeftPicture(leftPicture);
            }
            if (!StringUtils.isBlank(picture.getFrontPicture())) {
                //picture.setFrontPicture(fileSavePath + SEPARATOR + picture.getFrontPicture());
                File file = new File(picture.getFrontPicture());
                String frontPicture = getLocalImage(String.valueOf(file));
                pictureVo.setFrontPicture(frontPicture);
            }
            if (!StringUtils.isBlank(picture.getRightPicture())) {
                //picture.setRightPicture(fileSavePath + SEPARATOR + picture.getRightPicture());
                File file = new File(picture.getRightPicture());
                String rightPicture = getLocalImage(String.valueOf(file));
                pictureVo.setRightPicture(rightPicture);
            }
        }
        //将查询到的数据发送给前端
        push(stationUid, pictureVo);
    }

    /**
     * 将文件转换成base64格式
     * @param
     * @param imagePath
     * @return
     * @throws IOException
     */
    public String getLocalImage(String imagePath) throws IOException {
        System.out.println("imagePath===>"+imagePath);
        // 拼接获取文件所在路径
        String path =fileSavePath + SEPARATOR
                + imagePath.substring(0,7) +SEPARATOR
                + imagePath.substring(7,11) +SEPARATOR
                + imagePath.substring(11,13) +SEPARATOR
                +imagePath.substring(13,15) + SEPARATOR
                +imagePath.substring(15,17) +SEPARATOR
                +imagePath;
        // 获取图片
        File imageFile = new File(path);
        // 获取文件名
        String name = imageFile.getName();
        // 获取文件后缀名
        String substring = name.substring(name.lastIndexOf("."));
        // 将文件转换成byte流
        FileInputStream in = new FileInputStream(imageFile);
        byte[] imageBytes = new byte[(int) imageFile.length()];
        in.read(imageBytes);
        in.close();

        // byte[] imageBytes = new byte[(int) imageFile.length()];
        // 添加base64字节头，拼接成base64的完整格式
        String base64Image = "data:image/" + substring + " ;base64," + Base64.getEncoder().encodeToString(imageBytes);
        return base64Image;

    }




    /**
     * 用于SSE链接，和服务器链接保持通信，当有新的图片需要判断时，可以实时获取
     * @param userUid
     * @return
     */
    // 获取
    @GetMapping(path = "/drawingJudgment.do")
    public SseEmitter drawingJudgment(String userUid) {

        // 超时时间设置为1小时
        SseEmitter sseEmitter = new SseEmitter(3600_000L);


        // 根据userUid 查找stationUid
        String stationUid = userService.findStationUidByUserUid(userUid);

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



    // 将查询到的信息发送给前端


    public String push(String stationUid, PictureVo pictureVo) throws IOException {

        SseEmitter sseEmitter = sseCache.get(stationUid);
        if (sseEmitter != null) {
            sseEmitter.send(pictureVo);
        }
        return "over";
    }

    @PostMapping(path = "over")
    public String over(String id) {
        SseEmitter sseEmitter = sseCache.get(id);
        if (sseEmitter != null) {
            sseEmitter.complete();
            sseCache.remove(id);
        }
        return "over";
    }

    /**
     * 通过安检，无需开包
     * @param reqValue
     * @return
     */
    @PostMapping("/passCheck.do")
    public RespValue passCheck(@RequestBody ReqValue reqValue) {
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        Picture picture = new Picture();
        if (StringUtils.isBlank(jsonObject.getString("uid"))){
            return new RespValue(500,"The uid cannot empty",null);
        }
        picture.setUid(jsonObject.getString("uid"));
        if (StringUtils.isBlank(jsonObject.getString("userUid"))){
            return new RespValue(500,"The userUid cannot empty",null);
        }
        picture.setUserUid(jsonObject.getString("userUid"));
        picture.setRemark("通过安检");
        // 修改图片状态
        picture.setFlag(0);
        int i = pictureService.passCheck(picture);
        // 查询该站点是否还有未判图的图片
        try {
            selectPictureByStationUid(jsonObject.getString("stationUid"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (i!=0){
            return new RespValue(200,"success",null);
        }
        return new RespValue(500,"Failed to modify information ",null);

    }
}
