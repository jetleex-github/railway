package com.eaosoft.railway.utils;

import com.alibaba.fastjson.support.retrofit.Retrofit2ConverterFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UploadUtils {

    public static String uploadPicture(@RequestParam("file") MultipartFile[] files) {

        // 设置文件将要存入服务器目录
        // String path = "/data/file/";
        String path = "Z:/realway/新建文件夹";

        File targetFile = null;
        String url = "";//返回存储路径

        // 设置文件的路径和名称
        // 设置文件存放的路径
        Calendar calendar = Calendar.getInstance();
        path = path + calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DATE) + "/";
        // 设置文件的名称


        //获取文件夹路径
        File file1 = new File(path);
        //如果文件夹不存在则创建
        if (!file1.exists() && !file1.isDirectory()) {
            file1.mkdirs();
        }

        try {

            for (MultipartFile file : files) {


                // 获取照片文件名和字节流
                String fileName = path + UUID.randomUUID().toString().replace("-", "") + file.getOriginalFilename();
                InputStream stream = file.getInputStream();

                //将图片存入文件夹
                targetFile = new File(file1, fileName);
                // 将照片保存到服务器上指定的路径
                String savePath = "/path/to/save/directory/" + UUID.randomUUID().toString() + ".jpg";


               //ByteStreamToFile.convert(stream, savePath);

                // 构造照片访问地址并返回给前端
             //   url = "http://your.domain.com/uploads/" + fileName;
               // return url;



            //将上传的文件写到服务器上指定的文件。
            file.transferTo(targetFile);
            //赋予权限
            String command = "chmod 775 -R " + targetFile;
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec(command);
            //生成文件地址
            url = "http://XXXXXXXXX.cn" + path + "/" + fileName;
                return url;
            }
        } catch (IOException e) {

            throw new RuntimeException(e);
        }
        return url;
    }



   /* @ResponseBody
    @RequestMapping("/upload")
    public Object uploadPicture(@RequestParam(value="file",required=false)MultipartFile file, HttpServletRequest request, HttpServletResponse response){
        ResultResp result = new ResultResp();
        Map<String, Object> map = new HashMap<String, Object>();
        File targetFile=null;
        String url="";//返回存储路径
        String fileName=file.getOriginalFilename();//获取文件名加后缀
        if(fileName!=null&&fileName!=""){
            String path = "/data/file/";
            //String path = "D:/data/file/";
            String fileF = fileName.substring(fileName.lastIndexOf("."), fileName.length());//文件后缀
            if (!(fileF.equals(".jpg") || fileF.equals(".jpeg") || fileF.equals(".png") || fileF.equals(".image"))) {
                result.setMsg("只能上传jpg,jpeg,png,image格式");
                return result;
            }
            //新的文件名
            fileName=new Date().getTime()+"_"+new Random().nextInt(1000)+fileF;
            //获取文件夹路径
            File file1=new File(path);
            //如果文件夹不存在则创建
            if(!file1.exists()  && !file1.isDirectory()){
                file1.mkdirs();
            }
            //将图片存入文件夹
            targetFile = new File(file1, fileName);
            try {
                //将上传的文件写到服务器上指定的文件。
                file.transferTo(targetFile);
                //赋予权限
                String command = "chmod 775 -R " + targetFile;
                Runtime runtime = Runtime.getRuntime();
                Process proc = runtime.exec(command);
                //生成文件地址
                url="http://XXXXXXXXX.cn"+path+"/"+fileName;
                result.setCode(200+"");
                result.setMsg("图片上传成功");
                System.out.println("图片上传成功 url:"+url);
                map.put("url", url);
                result.setData(map);
            } catch (Exception e) {
                e.printStackTrace();
                result.setMsg("系统异常，图片上传失败");
            }
        }
        return result;
    }*/


  /*  @ResponseBody
    @RequestMapping("/upload")
    public String uploadPicture(@RequestParam(value="file",required=false) MultipartFile file, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        File targetFile = null;
        String url = "";//返回存储路径
        int code = 1;
        System.out.println(file);
        String fileName = file.getOriginalFilename();//获取文件名加后缀
        if (fileName != null && fileName != "") {
            String returnUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/upload/imgs/";//存储路径
            String path = request.getSession().getServletContext().getRealPath("upload/imgs"); //文件存储位置
            String fileF = fileName.substring(fileName.lastIndexOf("."), fileName.length());//文件后缀
            fileName = new Date().getTime() + "_" + new Random().nextInt(1000) + fileF;//新的文件名

            //先判断文件是否存在
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String fileAdd = sdf.format(new Date());
            //获取文件夹路径
            File file1 = new File(path + "/" + fileAdd);
            //如果文件夹不存在则创建
            if (!file1.exists() && !file1.isDirectory()) {
                file1.mkdir();
            }
            //将图片存入文件夹
            targetFile = new File(file1, fileName);
            try {
                //将上传的文件写到服务器上指定的文件。
                file.transferTo(targetFile);
                url = returnUrl + fileAdd + "/" + fileName;
                map.put("url", url);
                map.put("fileName", fileName);
                return Result.toResult(ResultCode.SUCCESS, map);
            } catch (Exception e) {
                e.printStackTrace();
                return Result.toResult(ResultCode.SYSTEM_INNER_ERROR);
            }
        }
        return Result.toResult(ResultCode.SYSTEM_INNER_ERROR);

    }*/
}
