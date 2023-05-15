package com.eaosoft.railway.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class AlarmVo {

    @ExcelProperty("报警编号")
    private String uid;

    @ExcelProperty("设备序号")
    private String equipSerial;

    @ExcelProperty("站点")
    private String stationName;


    @ExcelProperty("站口")
    private String stationExit;

    @ExcelProperty("生日")
    @ColumnWidth(20)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;


    @ExcelProperty("报警等级")
    private Integer grade;


    @ExcelProperty("报警设备ip")
    private String ipAddr;

    @ExcelIgnore

    private Integer state;


    @ExcelProperty("报警内容")
    private String content;


    @ExcelProperty("设备名称")
    private String equipName;


}
