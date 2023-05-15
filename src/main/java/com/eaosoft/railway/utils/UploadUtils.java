package com.eaosoft.railway.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Calendar;


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
        String path = "";
        // 获取文件名称和字节流
        InputStream stream = multipartFile.getInputStream();
        // 获取文件名称和后缀
        String name = multipartFile.getOriginalFilename();
        if (StringUtils.isBlank(name)){
            return "";
        }
        String substring = name.substring(0, 7);

        if (substring.equals("aiofile")) {
            // 根据文件名获得文件夹的名称
            path = name.substring(0, 7) + SEPARATOR
                    + name.substring(7, 11) + SEPARATOR
                    + name.substring(11, 13) + SEPARATOR
                    + name.substring(13, 15) + SEPARATOR
                    + name.substring(15, 17);


            // 重新设置文件名称，避免重名被覆盖，新的文件名为当前时间的毫秒数+文件名后缀
            name = System.currentTimeMillis() + name.substring(name.lastIndexOf("."));

            String replace = path.replace(SEPARATOR, "");
            name = replace + name;
        } else {
            // 重新设置文件名称，避免重名被覆盖，新的文件名为当前时间的毫秒数+文件名后缀
            name = System.currentTimeMillis() + name.substring(name.lastIndexOf("."));
            // 根据当前系统时间设置文件存放的路径和名称
            Calendar calendar = Calendar.getInstance();

            // 将小于10的月份设置为 0X
            int a = calendar.get(Calendar.MONTH) + 1;
            String month = "";
            if (a < 10) {
                month = "0" + a;
            } else {
                month = String.valueOf(a);
            }

            String hour = "";
            int x = calendar.get(Calendar.HOUR_OF_DAY);
            if (x < 10) {
                hour = "0" + x;
            } else {
                hour = String.valueOf(x);
            }
            String day = "";
            int d = calendar.get(Calendar.DATE);
            if (d < 10){
                day = "0" + d;
            }else {
                day = String.valueOf(d);
            }
            path = "aiofile" + SEPARATOR
                    + calendar.get(Calendar.YEAR) + SEPARATOR
                    + month + SEPARATOR
                    + day + SEPARATOR
                    + hour;

            String replace = path.replace(SEPARATOR, "");
            name = replace + name;
        }
        //获取文件夹路径
        File file = new File(fileSavePath + SEPARATOR + path);
        //如果文件夹不存在则创建
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }

        // 将文件保存到服务器上指定的路径
        String savePath = file + SEPARATOR + name;
        System.out.println("savePath===>" + savePath);
        FileOutputStream out = new FileOutputStream(savePath);
        byte[] buffer = new byte[1024];
        // 用于获取流的长度
        int bytesRead = 0;
        while ((bytesRead = stream.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
        out.close();
        // 返回相对路径地址
        return name;
    }
}