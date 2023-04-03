package com.eaosoft.railway.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 
 * </p>
 *
 * @author zzs
 * @since 2023-03-27
 */
@TableName("rw_equip")
@ApiModel(value = "Equip对象", description = "")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Equip implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "uid")
    private String uid;

    @ApiModelProperty("序列号")
    private String serialNo;

    @ApiModelProperty("设备名称")
    private String equipName;

    @ApiModelProperty("IP地址")
    private String ipAddr;

    @ApiModelProperty("厂家")
    private String producer;

    @ApiModelProperty("站点名称")
    private String stationUid;

    @ApiModelProperty("站点名称")
    private String stationExitUid;

    @ApiModelProperty("线路号")
    private String routeName;


    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastTime;

    @ApiModelProperty("状态（0-离线，1-在线）")
    private Integer state;
    @ApiModelProperty("是否绑定（0-未绑定，1-已绑定）")
    private Integer code;


}
