package com.eaosoft.railway.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;


@TableName("rw_station_exit")
@ApiModel(value = "StationExit对象", description = "站点管理")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StationExitVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "uid")
    private String uid;

    private String serialNo;

    @ApiModelProperty("站点uid")
    private String stationUid;

    @ApiModelProperty("出口名称")
    private String stationName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;
}
