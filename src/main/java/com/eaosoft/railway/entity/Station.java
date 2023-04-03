package com.eaosoft.railway.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
 * @since 2023-03-24
 */
@TableName("rw_station")
@ApiModel(value = "Station对象", description = "")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Station implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "uid")
    private String uid;

    @ApiModelProperty("序号")
    private String serialNo;

    @ApiModelProperty("站点名称")
    private String stationName;
    @ApiModelProperty("线路名称")
    private String routeName;
    @ApiModelProperty("出站口")
    private String stationExit;

    @ApiModelProperty("状态（0-删除，1-未删除）")
    @TableLogic(value = "1",delval = "0")
    private Integer isDeleted;



    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;


    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}
