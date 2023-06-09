package com.eaosoft.railway.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 开包结果
 * </p>
 *
 * @author zzs
 * @since 2023-05-04
 */
@TableName("rw_unpack")
@ApiModel(value = "Unpack对象", description = "开包结果")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Unpack implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "uid")
    private String uid;

    private String serialNo;

    @ApiModelProperty("开包任务uid")
    private String taskUid;

    @ApiModelProperty("设备序列号")
    private String equipSerialNo;

    @ApiModelProperty("旅客身份证号")
    private String cardId;

    @ApiModelProperty("旅客姓名")
    private String username;

    @ApiModelProperty("开包图片")
    private String image;

    @ApiModelProperty("线路名称")
    private String routeName;

    @ApiModelProperty("站口")
    private String stationExit;

    @ApiModelProperty("开包结果")
    private String result;

    @ApiModelProperty("复检人姓名")
    private String checkUser;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}
