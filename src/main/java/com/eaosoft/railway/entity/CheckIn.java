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
 * @since 2023-03-31
 */
@TableName("rw_check_in")
@ApiModel(value = "CheckIn对象", description = "")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckIn implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "uid")
    private String uid;

    private String serialNo;

    @ApiModelProperty("员工uid")
    private String userUid;

    @ApiModelProperty("打卡日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime workday;

    @ApiModelProperty("上班时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime onTime;

    @ApiModelProperty("下班时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime offTime;


}
