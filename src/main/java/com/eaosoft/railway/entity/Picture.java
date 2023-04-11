package com.eaosoft.railway.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.sql.Blob;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 判图系统
 * </p>
 *
 * @author zzs
 * @since 2023-04-10
 */
@TableName("rw_picture")
@ApiModel(value = "Picture对象", description = "判图系统")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Picture implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "uid")
    private String uid;

    private String serialNo;

    @ApiModelProperty("站点uid")
    private String stationUid;

    @ApiModelProperty("是否处理（0-未处理，1-已处理）")
    private Integer flag;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("左边图片")
    private String leftPicture;

    @ApiModelProperty("右边图片")
    private String rightPicture;

    @ApiModelProperty("正面图片")
    private String frontPicture;

    @ApiModelProperty("判图人uid")
    private String userUid;

    @ApiModelProperty("判图人声音")
    private byte[] voice;

    @ApiModelProperty("判图标记")
    private String label;

    @ApiModelProperty("判图备注")
    private String remark;

    @TableField(exist = false) // 用于接受坐标数组，表示数据库中并不存在该列
   private String[] coordinate;
}
