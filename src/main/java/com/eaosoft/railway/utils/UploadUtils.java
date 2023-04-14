package com.eaosoft.railway.utils;

import com.alibaba.fastjson.support.retrofit.Retrofit2ConverterFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/railway/upload")
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
       // String fileName = path + UUID.randomUUID().toString().replace("-", "") + file.getOriginalFilename();

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


    public static String upload(HttpServletRequest request) throws ServletException, IOException{
        // 存储的文件夹所在路径
        // String path =  "https://192.168.1.210//realway/新建文件夹";
        String path =  "Z:/realway/新建文件夹";

        String fileName = "";
        // 获取文件名称和字节流
        InputStream stream = null;
        // 获取http中携带的数据字段
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
        // 获取当前的日期，用于创建文件目录
        Calendar calendar = Calendar.getInstance();
        String day = calendar.get(Calendar.YEAR)+"/"+(calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.DATE)+"/";

        // 重新设置图片的名称，避免图片被覆盖
        String name = UUID.randomUUID().toString().replace("-","")+fileName;

        // 设置文件夹的完整路径
        File file = new File(path + "/" + day);
        // 如果文件夹不存在则创建
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }

        // 将文件保存到服务器上指定的路径
        String savePath = file + "/"+ name;
        FileOutputStream out = new FileOutputStream(savePath);
        byte[] buffer = new byte[4096];
        // 用于获取输入流的长度
        int bytesRead;
        while ((bytesRead = stream.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
        out.close();
        return savePath;
    }


    public static String uploadVoice(MultipartFile multipartFile) {
        //视频上传

        String path =  "Z:/realway/新建文件夹";
        //获取原文件名
        String name=multipartFile.getOriginalFilename();
        //获取文件后缀
        String subffix=name.substring(name.lastIndexOf(".")+1,name.length());
       /* //控制格式
        if(subffix.equals("")||!subffix.equals("mp4")||!subffix.equals("mov")||!subffix.equals("avi")||!subffix.equals("wmv")||!subffix.equals("m4v")||!subffix.equals("dat")||!subffix.equals("flv")||!subffix.equals("mkv"))
        {
            return AjaxResult.error("视频格式不对");
        }*/
        //新的文件名以日期命名
        String fileName=UUID.randomUUID().toString().replace("-","")+name;
        // 获取当前的日期，用于创建文件目录
        Calendar calendar = Calendar.getInstance();
        String day = calendar.get(Calendar.YEAR)+"/"+(calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.DATE)+"/";
        // 设置文件夹的完整路径
        File file = new File(path + "/" + day);

        if(!file.exists())//文件夹不存在就创建
        {
            file.mkdirs();
        }
        //保存文件
        try {
            multipartFile.transferTo(new File(file + "/"+ fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String realPath=file + "/"+ fileName;
        String simulationVideo="/videos/"+fileName+"."+subffix;
        return realPath;
    }
}
