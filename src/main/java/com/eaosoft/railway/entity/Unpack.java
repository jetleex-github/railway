package com.eaosoft.railway.entity;

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

    private String uid;

    private String serialNo;

    @ApiModelProperty("开包任务uid")
    private String taskUid;

    @ApiModelProperty("旅客身份证号")
    private String cardId;

    @ApiModelProperty("旅客姓名")
    private String username;

    @ApiModelProperty("开包图片")
    private String image;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}
