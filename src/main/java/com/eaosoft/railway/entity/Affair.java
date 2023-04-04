package com.eaosoft.railway.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 事件管理
 * </p>
 *
 * @author zzs
 * @since 2023-04-04
 */
@TableName("rw_affair")
@ApiModel(value = "Affair对象", description = "事件管理")
public class Affair implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "uid")
    private String uid;

    @ApiModelProperty("事件编号")
    private String serialNo;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creatTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @ApiModelProperty("创建事件的站点")
    private String stationUid;

    @ApiModelProperty("创建事件的站口")
    private String stationExitUid;

    @ApiModelProperty("线路名")
    private String routeName;

    @ApiModelProperty("接收人uid")
    private String userUid;

    @ApiModelProperty("事件详情")
    private String details;

    @ApiModelProperty("创建事件的设备编号")
    private String equipSerial;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }
    public LocalDateTime getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(LocalDateTime creatTime) {
        this.creatTime = creatTime;
    }
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    public String getStationUid() {
        return stationUid;
    }

    public void setStationUid(String stationUid) {
        this.stationUid = stationUid;
    }
    public String getStationExitUid() {
        return stationExitUid;
    }

    public void setStationExitUid(String stationExitUid) {
        this.stationExitUid = stationExitUid;
    }
    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }
    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }
    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
    public String getEquipSerial() {
        return equipSerial;
    }

    public void setEquipSerial(String equipSerial) {
        this.equipSerial = equipSerial;
    }

    @Override
    public String toString() {
        return "Affair{" +
            "uid=" + uid +
            ", serialNo=" + serialNo +
            ", creatTime=" + creatTime +
            ", updateTime=" + updateTime +
            ", stationUid=" + stationUid +
            ", stationExitUid=" + stationExitUid +
            ", routeName=" + routeName +
            ", userUid=" + userUid +
            ", details=" + details +
            ", equipSerial=" + equipSerial +
        "}";
    }
}
