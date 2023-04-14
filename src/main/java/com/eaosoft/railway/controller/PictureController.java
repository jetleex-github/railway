package com.eaosoft.railway.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.eaosoft.railway.entity.Picture;
import com.eaosoft.railway.service.IPictureService;
import com.eaosoft.railway.utils.RespValue;
import com.eaosoft.railway.utils.UploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


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
    private IPictureService pictureService;

    /**
     * 添加需要判图的图片
     *
     * @param file
     */
    @PostMapping("/insertPictures01.do")
    public void insertPictures01(@RequestParam(value = "file",required = false) MultipartFile[] file) {
        /*Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        System.out.println("jsonObject===>"+jsonObject.toString());
        String rightPicture = jsonObject.getString("rightPicture");

        System.out.println("rightPicture===>"+rightPicture);*/

        if (file!=null){
            System.out.println("========>");
        }
        Picture picture = new Picture();
        String url = UploadUtils.uploadPicture(file);
        picture.setRightPicture(url);
        System.out.println("url====>"+url);

       /* if (file != null) {
            if (file.length == 1) {
                String url = UploadUtils.uploadPicture(file[0]);
                picture.setRightPicture(url);
                System.out.println("url====>"+url);
            }
        }*/
    }


    /**
     /**
     * 接收单张照片并存储到服务器
     * @param request
     *
     * @return
     */
    @PostMapping("/insertPictures.do")
    @ResponseBody
    public void insertPictures(HttpServletRequest request,@RequestParam String stationUid ){

        Picture picture = new Picture();

        System.out.println("stationUid===>"+stationUid);
        picture.setStationUid(stationUid);
        // 将接受的文件上传到服务器
        String url = null;
        try {
            url= UploadUtils.upload(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!StringUtils.isBlank(url)){
            picture.setFrontPicture(url);
        }

        // 设置创建时间
        picture.setCreateTime(LocalDateTime.now());

        // 将图片信息存入数据库
        pictureService.insertPictures(picture);
    }


    /**
     * 每秒向前端发送一条消息
     *
     * @return
     */
    @RequestMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> sse() {
        List list = pictureService.findPicture();
        //return Flux.interval(Duration.ofSeconds(1)).map(sequence -> ServerSentEvent.builder("SSE - " + sequence).build());
        return Flux.interval(Duration.ofSeconds(1))
                .map(sequence -> ServerSentEvent.builder("SSE-" + list).build());

    }


    /**
     *
     */
   /* @PostMapping("/addInfo")
    public RespValue addInfo(@RequestBody ReqValue reqValue) {


        DataFormat dataFormat = (DataFormat) reqValue.getRequestDatas();

        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));

        Picture picture = new Picture();
        // 获取接收到的语音
        InputStream inputStream = (InputStream) jsonObject.get("voice");
        if (!StringUtils.isBlank(jsonObject.getString("voice"))) {
            try {
                byte[] bytes = IOUtils.toByteArray(inputStream);
                picture.setVoice(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }*/


    /**
     * 添加判图结果
     * @param multipartFile
     * @param request
     * @return
     */
    @PostMapping("/addResult.do")
    @ResponseBody
    public RespValue addResult( @RequestParam( value="file",required=false) MultipartFile multipartFile, HttpServletRequest request) {

        String voice = "";
        if (multipartFile != null ){
            // 如果有语音把语音存到服务器，并返回地址
             voice = UploadUtils.uploadVoice(multipartFile);
        }
        Picture picture = new Picture();
        picture.setLabel(request.getParameter("coordinate"));
        picture.setVoice(voice);
        picture.setUid(request.getParameter("uid"));
        // 如果存在文字备注则添加
        if (!StringUtils.isBlank(request.getParameter("remark"))){
            picture.setRemark(request.getParameter("remark"));
        }
        picture.setFlag(1);
        int i = pictureService.addResult(picture);
        if (i != 0){
            return new RespValue(200,"success",null);
        }
        return null;
    }

}
