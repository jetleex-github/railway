package com.eaosoft.railway.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class UploadUtils {

    // 设置文件将要存入服务器的根目录

    private static String fileSavePath;

    // 通过配置文件，映射图片上传到服务器的根路径
    @Value("${file.save.path}")
    public void setFileSavePath(String path) {
        UploadUtils.fileSavePath = path;
    }

    /**
     * 上传图片和声音
     *
     * @param multipartFile
     * @return
     * @throws Exception
     */
    public static String upload(MultipartFile multipartFile) throws Exception {
        // 获取当前系统的分隔符 \ 或者是 /
        String SEPARATOR = File.separator;

        // 文件夹名称
        String path ="";
        // 获取文件名称和字节流
        InputStream stream = multipartFile.getInputStream();
        // 获取文件名称和后缀
        String name = multipartFile.getOriginalFilename();
        System.out.println("name===>"+name);
        String substring = name.substring(0, 7);
        if (substring.equals("aiofile")){
            // 根据文件名获得文件夹的名称
            path = name.substring(0,7) +SEPARATOR
                    + name.substring(7,11) +SEPARATOR
                    + name.substring(11,13) +SEPARATOR
                    +name.substring(13,15) + SEPARATOR
                    +name.substring(15,17) +SEPARATOR;


            // 重新设置文件名称，避免重名被覆盖，新的文件名为当前时间的毫秒数+文件名后缀
            name = System.currentTimeMillis() + name.substring(name.lastIndexOf("."));

            String replace = path.replace(SEPARATOR, "");
            name = replace +name;
        }
        else{
            // 重新设置文件名称，避免重名被覆盖，新的文件名为当前时间的毫秒数+文件名后缀
            name = System.currentTimeMillis() + name.substring(name.lastIndexOf("."));
            // 根据当前系统时间设置文件存放的路径和名称
            Calendar calendar = Calendar.getInstance();

            // 将小于10的月份设置为 0X
            int a = calendar.get(Calendar.MONTH) + 1;
            String month = "";
            if (a<10){
                month = "0"+a;
            }

            path = "aiofile"+SEPARATOR
                    + calendar.get(Calendar.YEAR) + SEPARATOR
                    + month + SEPARATOR
                    + calendar.get(Calendar.DATE)+ SEPARATOR
                    + calendar.get(Calendar.HOUR_OF_DAY);

            String replace = path.replace(SEPARATOR, "");
            name = replace +name;
        }
        //获取文件夹路径
        File file = new File(fileSavePath + SEPARATOR +path);
        //如果文件夹不存在则创建
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }


        // 将文件保存到服务器上指定的路径
        String savePath = file + SEPARATOR +name;
        System.out.println("savePath===>"+savePath);
        FileOutputStream out = new FileOutputStream(savePath);
        byte[] buffer = new byte[1024];
        // 用于获取流的长度
        int bytesRead=0;
        while ((bytesRead = stream.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
        out.close();
        // 返回相对路径地址
        return  name;
    }

    /**
     * 通过url获取图片传给前端
     *
     * @param url      前端传过来的图片url
     * @return
     */
   // @RequestMapping(value = "/getimg")
    public static BufferedImage getImg(String url){
        System.out.println("获取的url"+url);
        String str1="http";
        boolean b=url.contains(str1);
        if (b==false){//判断是否是网络图片的url
            url="http://*****:8080/"+url;
        }
        try {
            URL urlimg = new URL(url);
            //创建链接对象
            URLConnection urlConnection = urlimg.openConnection();
            //设置超时
            urlConnection.setConnectTimeout(1000);
            urlConnection.setReadTimeout(5000);
            urlConnection.connect();
            //获取流
            InputStream inputStream = urlConnection.getInputStream();
            //读取图片
            BufferedImage bufferedImage = ImageIO.read(inputStream);

            if (bufferedImage!=null){
                //获取图片格式
                //String format = url.substring(url.lastIndexOf(".") + 1);
                //打印图片
               // ImageIO.write(bufferedImage,format,response.getOutputStream());// 将文件流放入response中
                return bufferedImage;
            }
        }catch (Exception e){
            System.out.println("图片异常"+e);
            System.out.println("参数异常"+url);
        }
        return null;
    }


    public static byte[] getPic(Path path) {
        //进行图片路径处理。拼接上图片名
        //final Path path = Path.of(System.getProperty("user.dir"), "/home", "/pic", picName);
        //final Path path = picName;
        //String path = picName;
        byte[] bytes = null;
        if (path.toFile().exists()) {
            try {
                bytes = Files.readAllBytes(path);
            } catch (Exception e) {
            }
        } else {
        }
       //bytes = Files.readAllBytes(path);
        return bytes;
    }


}