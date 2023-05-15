package com.eaosoft.railway.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author zzs
 * @since 2023-04-07
 */
@TableName("rw_alarm_manage")
@ApiModel(value = "AlarmManage对象", description = "")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmManage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "uid")
    private String uid;

    @ApiModelProperty("报警序号")
    private String serialNo;

    @ApiModelProperty("设备序号")
    private String equipSerial;

    @ApiModelProperty("站点uid")
    private String stationUid;

    @ApiModelProperty("站口uid")
    private String stationExitUid;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty("设备uid")
    private String equipUid;

    @ApiModelProperty("报警等级")
    private Integer grade;

    @ApiModelProperty("报警设备ip")
    private String ipAddr;

    @ApiModelProperty("状态（0-已读，1-未读）")
    private Integer state;

    @ApiModelProperty("报警内容")
    private String content;

    @ApiModelProperty("解决方式")
    private String solution;

    @ApiModelProperty("设备名称")
    private String equipName;

    @ApiModelProperty("处理人员uid")
    private String userUid;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dealTime;


    @ApiModelProperty("是否删除（0-已删除，1-未删除）")
    @TableLogic(value = "1",delval = "0")
    private Integer isDeleted;



}
