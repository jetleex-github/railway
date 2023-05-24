package com.eaosoft.railway.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.TableId;
import com.eaosoft.railway.utils.GenderConverter;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 批量添加人员信息后，导出人员信息
 */
@Data
public class UserVo {
    @TableId(value = "uid")
    @ExcelIgnore
    private String uid;

    @ExcelProperty("工号")
    @ColumnWidth(10)
    private String serialNo;

    @ExcelProperty("用户名")
    @ColumnWidth(20)
    private String username;

    @ExcelProperty("真实姓名")
    @ColumnWidth(20)
    private String realName;

    @ExcelProperty("身份")
    @ColumnWidth(40)
    @ExcelIgnore
    private Integer caption ;

    @ExcelProperty("密码")
    @ColumnWidth(40)
    private String password;


    @ExcelProperty(value = "性别",converter = GenderConverter.class)
    @ColumnWidth(10)
    private Integer gender;

    @ExcelProperty("年龄")
    @ColumnWidth(10)
    private Integer age;

    @ExcelProperty("手机号")
    @ColumnWidth(20)
    private String phone;

    @ExcelProperty("住址")
    @ColumnWidth(40)
    private String address;

    @ExcelProperty("状态")
    @ExcelIgnore
    private Integer state;

    @ExcelProperty("身份证号")
    @ColumnWidth(30)
    private String idCard;

    @ExcelProperty("电子邮箱")
    @ColumnWidth(20)
    private String email;

    @ColumnWidth(20)
    @ExcelProperty("创建日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    @ExcelProperty("离职时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ExcelIgnore
    private Date leaveTime;

    @ExcelProperty("更新时间")
    @ExcelIgnore
    private LocalDateTime updateTime;

    @ExcelProperty("线路名称")
    @ColumnWidth(10)
    private String routeName;

    @ExcelProperty("所属站点")
    @ColumnWidth(10)
    private String stationUid;

    @ExcelProperty("职位")
    @ColumnWidth(10)
    @ExcelIgnore
    private String position;


}
