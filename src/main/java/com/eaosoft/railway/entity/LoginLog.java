package com.eaosoft.railway.entity;

import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("rw_login_log")
@ApiModel(value = "LoginLog对象", description = "")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "uid")
    private String uid;

    private String serialNo;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("IP地址")
    private String ipAddr;

    @ApiModelProperty("登陆状态")
    private String state;

    @ApiModelProperty("登陆状态")
    private String path;

    @ApiModelProperty("登录设备类型")
    private String callerName;

    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
