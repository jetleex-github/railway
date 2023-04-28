package com.eaosoft.railway.vo;

import com.alibaba.excel.EasyExcel;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class DownExcel {
    public static void download(HttpServletResponse response, Class t, List list)  {
        response.setContentType("application/vnd.ms-excel");// 设置文本内省
        response.setCharacterEncoding("utf-8");// 设置字符编码
        response.setHeader("content-disposition","attachment;filename=alarmInfoExport_"+System.currentTimeMillis()+".xlsx");// 设置响应头
        try {
            EasyExcel.write(response.getOutputStream(), t).sheet("智能识别设备报警数据导出").doWrite(list); //用io流来写入数据
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}