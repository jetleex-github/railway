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
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpServletRequest;
import com.eaosoft.railway.vo.PictureVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;



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


    // 用于存储SSE的连接
    private static Map<String, SseEmitter> sseCache = new ConcurrentHashMap<>();

    // 文件保存在服务器的根路径
    private static String fileSavePath;

    // 获取当前系统的分隔符 \ 或者是 /
    private static String SEPARATOR = File.separator;

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
     * @param rightFile  右侧图片
     * @param leftFile   左侧图片
     * @param frontFile  正面图片
     * @param stationUid 站点uid
     */
    @PostMapping("/insertPictures.do")
    public RespValue insertPictures01(
            @RequestParam(value = "rightFile", required = false) MultipartFile rightFile,
            @RequestParam(value = "leftFile", required = false) MultipartFile leftFile,
            @RequestParam(value = "frontFile", required = false) MultipartFile frontFile,
            String stationUid) {

        // 正面照片不能为空
        if (frontFile == null) {
            return new RespValue(500, "The frontPicture cannot empty", null);
        }

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

        // 通知前端有新的照片传入
        try {
            selectPictureByStationUid(stationUid);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 通过照片地址查询该条信息的uid，并返回
        Picture p = pictureService.selectUidByUrl(frontUrl);
        return new RespValue(200, "success", p.getUid());
    }



    /**
     * 添加需要开包的判图结果
     *
     * @param multipartFile 语音文件
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

        // 获取站点uid
       /*  String stationUid = picture.getStationUid();
        // 查询该站点下是否还存在需要判断的图片
       try {
            selectPictureByStationUid(stationUid);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (i != 0) {
            return new RespValue(200, "success", null);
        }*/
        return null;
    }



    /*
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

    /**
     * 查询该站点等待时间最长的需要判图的图片
     *
     * @param
     * @return
     */
    public void selectPictureByStationUid(String stationUid) throws IOException {


       /* Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        String stationUid = jsonObject.getString("stationUid");*/
        // List<Picture> pictureList = pictureService.selectPictureByStationUid(stationUid);

        // 查询出最先需要判图的照片信息
        Picture picture = pictureService.selectOneByStationUid(stationUid);

        PictureVo pictureVo = new PictureVo();
        if (picture != null) {
            // 返回数据的uid
            pictureVo.setUid(picture.getUid());
            // 站点uid
            pictureVo.setStationUid(picture.getStationUid());

            if (!StringUtils.isBlank(picture.getLeftPicture())) {

                // 通过文件名获取文件地址，并将文件转换成base64的字符流
                String leftPicture = getLocalImage(picture.getLeftPicture());

                pictureVo.setLeftPicture(leftPicture);
            }
            if (!StringUtils.isBlank(picture.getFrontPicture())) {
                //picture.setFrontPicture(fileSavePath + SEPARATOR + picture.getFrontPicture());

                String frontPicture = getLocalImage(picture.getFrontPicture());
                pictureVo.setFrontPicture(frontPicture);
            }
            if (!StringUtils.isBlank(picture.getRightPicture())) {
                //picture.setRightPicture(fileSavePath + SEPARATOR + picture.getRightPicture());

                String rightPicture = getLocalImage(picture.getRightPicture());
                pictureVo.setRightPicture(rightPicture);
            }
        }
        //将查询到的数据发送给前端
        push(stationUid,pictureVo);

        // List<PictureVo> imageData = new ArrayList<>();

        //PictureVo pictureVo = new PictureVo();

        // 拼接上文件的根目录地址
//        for (Picture picture : pictureList) {
//            pictureVo.setUid(picture.getUid());
//            if (!StringUtils.isBlank(picture.getLeftPicture())) {
//                picture.setLeftPicture(fileSavePath + SEPARATOR + picture.getLeftPicture());
//                // System.out.println("picture.getLeftPicture()===>"+picture.getLeftPicture());
//                /*BufferedImage img = UploadUtils.getImg(fileSavePath + SEPARATOR + picture.getLeftPicture());
//                ;*/
//                /*byte[] bytes = UploadUtils.getPic(Paths.get(picture.getLeftPicture()));
//
//                MultipartFile file = new MockMultipartFile("__init__.py","__init__.py","application/octet-stream" ,bytes); //
//                MultipartFile multipartFiles = {file}; //此处因为我方法需要转成数组，没看到有构造器方法，故采用这种方式*/
//                File file = new File(picture.getLeftPicture());
//                //JSONObject leftPicture = getLocalImage("leftPicture", String.valueOf(file));
//                String leftPicture = getLocalImage(String.valueOf(file));
//                // imageData.add(leftPicture);
//                pictureVo.setLeftPicture(leftPicture);
//                //imageData.add(getImageBytes(String.valueOf(file)));
//                // System.out.println("leftPictureFile====>"+file);
//              /*  InputStream inputStream = new FileInputStream(file);
//                //借助的工具同样引入spring-mock包，pom文件在上边有
//                MultipartFile multipartFile = new MockMultipartFile("leftPicture", inputStream);
//*/
//
//
//            }
//            if (!StringUtils.isBlank(picture.getFrontPicture())) {
//                picture.setFrontPicture(fileSavePath + SEPARATOR + picture.getFrontPicture());
//
//                //  System.out.println("picture.getLeftPicture()===>"+picture.getFrontPicture());
//                // BufferedImage img = UploadUtils.getImg(fileSavePath + SEPARATOR + picture.getFrontPicture());
//                //map.put("leftPicture",img);
//                File file = new File(picture.getFrontPicture());
//                //JSONObject frontPicture = getLocalImage("frontPicture", String.valueOf(file));
//                String frontPicture = getLocalImage(String.valueOf(file));
//                //imageData.add(frontPicture);
//                pictureVo.setFrontPicture(frontPicture);
//                //imageData.add(getImageBytes(String.valueOf(file)));
//               /* InputStream inputStream = new FileInputStream(file);
//                //借助的工具同样引入spring-mock包，pom文件在上边有
//                MultipartFile multipartFile = new MockMultipartFile("frontPicture", inputStream);
//*/
//
//            }
//            if (!StringUtils.isBlank(picture.getRightPicture())) {
//                picture.setRightPicture(fileSavePath + SEPARATOR + picture.getRightPicture());
//                File file = new File(picture.getRightPicture());
//                // JSONObject rightPicture = getLocalImage("rightPicture", String.valueOf(file));
//                String rightPicture = getLocalImage(String.valueOf(file));
//                //imageData.add(rightPicture);
//                pictureVo.setRightPicture(rightPicture);
//               /* InputStream inputStream = new FileInputStream(file);
//                //借助的工具同样引入spring-mock包，pom文件在上边有
//                MultipartFile multipartFile = new MockMultipartFile("rightPicture", inputStream);*/
//
//
//                // imageData.add(getImageBytes(String.valueOf(file)));
//
//                // System.out.println("picture.getLeftPicture()===>"+picture.getRightPicture());
//                /*BufferedImage img = UploadUtils.getImg(fileSavePath + SEPARATOR + picture.getRightPicture());
//                map.put("leftPicture",img);*/
//            }
//
//        }

        //将查询到的数据发送给前端
        // push(stationUid,pictureList);
      /*  List<String> base64Images = imageData.stream()
                .map(Base64.getEncoder()::encodeToString)
                .collect(Collectors.toList());

        System.out.println("base64Images====>"+base64Images);*/
        // imageData.add(pictureVo);
        // push(stationUid, pictureVo);
        // return new RespValue(200, "success", pictureList);
    }

    /**
     * 查询该站点所有需要判图的图片
     *
     * @param
     * @return
     */
/*    public void selectPictureByStationUid(String stationUid) throws IOException {
        // 获取当前系统的分隔符 \ 或者是 /
        String SEPARATOR = File.separator;
        List<Picture> pictureList = pictureService.selectPictureByStationUid(stationUid);
        List<PictureVo> imageData = new ArrayList<>();
        PictureVo pictureVo = new PictureVo();
        // 拼接上文件的根目录地址
        for (Picture picture : pictureList) {
            pictureVo.setUid(picture.getUid());
            if (!StringUtils.isBlank(picture.getLeftPicture())) {
                picture.setLeftPicture(fileSavePath + SEPARATOR + picture.getLeftPicture());
                File file = new File(picture.getLeftPicture());
                String leftPicture = getLocalImage(String.valueOf(file));
                pictureVo.setLeftPicture(leftPicture);


            }
            if (!StringUtils.isBlank(picture.getFrontPicture())) {
                picture.setFrontPicture(fileSavePath + SEPARATOR + picture.getFrontPicture());
                File file = new File(picture.getFrontPicture());
                String frontPicture = getLocalImage(String.valueOf(file));
                pictureVo.setFrontPicture(frontPicture);
            }
            if (!StringUtils.isBlank(picture.getRightPicture())) {
                picture.setRightPicture(fileSavePath + SEPARATOR + picture.getRightPicture());
                File file = new File(picture.getRightPicture());
                String rightPicture = getLocalImage(String.valueOf(file));
                pictureVo.setRightPicture(rightPicture);
            }
            imageData.add(pictureVo);
        }
        //将查询到的数据发送给前端
        push(stationUid, imageData);
    }*/

    /**
     * 将文件转换成base64的数据流
     *
     * @param imagePath
     * @return
     */
    public String getLocalImage(String imagePath) throws IOException {
        //  String imagePath = "C:/images/image.jpg";

        // 拼接获取文件所在路径
        String path = fileSavePath + SEPARATOR
                + imagePath.substring(0, 7) + SEPARATOR
                + imagePath.substring(7, 11) + SEPARATOR
                + imagePath.substring(11, 13) + SEPARATOR
                + imagePath.substring(13, 15) + SEPARATOR
                + imagePath.substring(15, 17) + SEPARATOR
                + imagePath;
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
        /*JSONObject response = new JSONObject();
        response.put("image", base64Image);
        return response;*/
        /*HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(response.toString(), headers, HttpStatus.OK);*/

    }

  /*  public JSONObject getLocalImage(String fileName,String imagePath) throws IOException {
      //  String imagePath = "C:/images/image.jpg";
        // 获取图片
        File imageFile = new File(imagePath);
        // 获取文件名
        String name = imageFile.getName();
        // 获取文件后缀名
        String substring = name.substring(name.lastIndexOf("."));
        FileInputStream in = new FileInputStream(imageFile);
        byte[] imageBytes = new byte[(int) imageFile.length()];
        in.read(imageBytes);
        in.close();
        // 添加base64字节头，拼接成base64的完整格式
        String base64Image ="data:image/"+substring+" ;base64," + Base64.getEncoder().encodeToString(imageBytes);

        JSONObject response = new JSONObject();
        response.put(fileName, base64Image);
       HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(response.toString(), headers, HttpStatus.OK);
        return response;
    }*/



    /**
     * 用于SSE链接，和服务器链接保持通信，当有新的图片需要判断时，可以实时获取
     *
     * @param userUid 操作员的uid，用于查找其所在的站点uid
     * @return
     */
    // 获取
    @GetMapping(path = "/drawingJudgment.do")
    public SseEmitter drawingJudgment(String userUid) {

        // 超时时间设置为1小时
        SseEmitter sseEmitter = new SseEmitter(3600_000L);


        // 根据userUid 查找stationUid
        System.out.println("userUid====>"+userUid);
        String stationUid = userService.findStationUidByUserUid(userUid);

        // 将stationUid作为key放入map中，设置连接缓存
        sseCache.put(stationUid, sseEmitter);
        try {
            System.out.println("stationUid===>"+stationUid);
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


    /**
     * 通过安检
     * @param reqValue
     * @return
     */
    @PostMapping("/passCheck.do")
    public RespValue passCheck(@RequestBody ReqValue reqValue) {


        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        Picture picture = new Picture();
      /*  if (StringUtils.isBlank(jsonObject.getString("uid"))) {
            return new RespValue(500, "The uid cannot empty", null);
        }*/
        picture.setUid(jsonObject.getString("uid"));
        /*if (StringUtils.isBlank(jsonObject.getString("userUid"))) {
            return new RespValue(500, "The userUid cannot empty", null);
        }*/
        picture.setUserUid(jsonObject.getString("userUid"));
        picture.setRemark(jsonObject.getString("remark"));
        // 修改图片状态
        picture.setFlag(1);
        int i = pictureService.passCheck(picture);

     /*   // 查询该站点是否还有未判图的图片
        try {
            selectPictureByStationUid(jsonObject.getString("stationUid"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

        // TODO 将通过结果发送给设备


        if (i != 0) {
            return new RespValue(200, "success", null);
        }
        return new RespValue(500, "Failed to modify information ", null);

    }

    /**
     * 断开SSE连接
     * @param reqValue
     * @return
     */
    @PostMapping("/loginOut.do")
    public RespValue loginOut(@RequestBody ReqValue reqValue){
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        SseEmitter remove = sseCache.remove(jsonObject.getString("stationUid"));
        if (remove != null){
            System.out.println("断开成功");
            return new RespValue(200,"success",null);
        }
        return new RespValue(500,"error",null);
    }


}
