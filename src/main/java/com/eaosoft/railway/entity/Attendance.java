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
 * @since 2023-03-29
 */
@TableName("rw_attendance")
@ApiModel(value = "Attendance对象", description = "勤务管理")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Attendance implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "uid")
    private String uid;

    private String serialNo;

    @ApiModelProperty("上班日期")
    private LocalDateTime workDate;

    @ApiModelProperty("开始时间")
    private LocalDateTime beginTime;

    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("员工uid")
    private String userUid;

    @ApiModelProperty("上班时间")
    private LocalDateTime onTime;

    @ApiModelProperty("下班时间")
    private LocalDateTime offTime;

    @ApiModelProperty("是否请假（0-未请假，1-请假）")
    private Integer note;


}
