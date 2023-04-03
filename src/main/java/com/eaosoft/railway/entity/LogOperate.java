package com.eaosoft.railway.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 
 * </p>
 *
 * @author zzs
 * @since 2023-03-16
 */
@TableName("rw_log_operate")
@ApiModel(value = "LogOperate对象", description = "")
public class LogOperate implements Serializable {

    private static final long serialVersionUID = 1L;

    private String uid;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("IP地址")
    private String ipAddr;

    @ApiModelProperty("登陆状态")
    private String status;

    @ApiModelProperty("进行的操作")
    private String caption;

    @ApiModelProperty("登陆时间")
    private LocalDateTime createTime;

    @ApiModelProperty("备注")
    private String note;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "LogOperate{" +
            "uid=" + uid +
            ", username=" + username +
            ", ipAddr=" + ipAddr +
            ", status=" + status +
            ", caption=" + caption +
            ", createTime=" + createTime +
            ", note=" + note +
        "}";
    }
}
