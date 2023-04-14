package com.eaosoft.railway.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


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

        // 获取文件名称和字节流
        InputStream stream = multipartFile.getInputStream();
        // 获取文件名称和后缀
        String name = multipartFile.getOriginalFilename();
        // 重新设置文件名称，避免重名被覆盖
        name = UUID.randomUUID().toString().replace("-", "") + name;

        // 设置文件夹的路径和名称
        // 根据当前系统时间设置文件存放的路径
        Calendar calendar = Calendar.getInstance();
        String fileName = calendar.get(Calendar.YEAR) + SEPARATOR
                + (calendar.get(Calendar.MONTH) + 1) + SEPARATOR
                + calendar.get(Calendar.DATE);
        String path = fileSavePath + SEPARATOR + fileName;

        //获取文件夹路径
        File file = new File(path);
        //如果文件夹不存在则创建
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }

        // 将文件保存到服务器上指定的路径
        String savePath = file + SEPARATOR + name;
        FileOutputStream out = new FileOutputStream(savePath);
        byte[] buffer = new byte[10240];
        // 用于获取流的长度
        int bytesRead;
        while ((bytesRead = stream.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
        out.close();
        // 返回相对路径地址
        return fileName + SEPARATOR + name;
    }

}