package com.eaosoft.railway.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.eaosoft.railway.entity.Picture;
import com.eaosoft.railway.service.IPictureService;
import com.eaosoft.railway.utils.ReqValue;
import com.eaosoft.railway.utils.RespValue;

import com.eaosoft.railway.utils.UploadUtils;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import sun.misc.BASE64Encoder;
import sun.text.resources.FormatData;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.time.Duration;
import java.util.Collection;
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
     *
     */
    @PostMapping("/selectPicture.do")
    @ResponseBody
    public RespValue selectPicture(HttpServletRequest request) throws ServletException, IOException{

        // 获取文件名称和字节流
        String fileName = "";
        InputStream stream = null;
        Collection<Part> parts = request.getParts();
        for (Part part : parts) {
            if (part.getName().equalsIgnoreCase("file")) {
                // 获取文件名
                String contentDisp = part.getHeader("content-disposition");
                String[] items = contentDisp.split(";");
                for (String item : items) {
                    if (item.trim().startsWith("filename")) {
                        fileName = item.substring(item.indexOf('=') + 2, item.length() - 1);
                        break;
                    }
                }
                // 获取输入流
                stream = part.getInputStream();
                break;
            }
        }

        // 将文件保存到服务器上指定的路径
        String savePath = "C:\\install\\新建文件夹" + fileName;
        FileOutputStream out = new FileOutputStream(savePath);
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = stream.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
        out.close();


    /*    System.out.println("进入================>");
        *//*DataFormat dataFormat = (DataFormat) reqValue.getRequestDatas();
        byte[] bytes = dataFormat.toString().getBytes();*//*
       *//* DataInputStream dataInputStream = (DataInputStream) reqValue.getRequestDatas();
        try {
            IOUtils.toByteArray(dataInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*//*
        String url = UploadUtils.uploadPicture(file);
        System.out.println("url====>"+url);*/

        return null;
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
    @PostMapping("/addInfo")
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
    }

}
