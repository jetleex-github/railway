package com.eaosoft.railway.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 
 * </p>
 *
 * @author zzs
 * @since 2023-03-30
 */
@TableName("rw_station_exit")
@ApiModel(value = "StationExit对象", description = "站点管理")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StationExit implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "uid")
    private String uid;

    private String serialNo;

    @ApiModelProperty("站点uid")
    private String stationUid;

    @ApiModelProperty("出口名称")
    private String stationExit;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;

}
