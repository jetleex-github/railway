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
@TableName("rw_unread_notice")
@ApiModel(value = "UnreadNotice对象", description = "")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnreadNotice implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("uid")
    private String uid;

    private String serialNo;

    @ApiModelProperty("通知uid")
    private String noticeUid;

    @ApiModelProperty("用户uid")
    private String userUid;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("读取时间")
    private LocalDateTime readTime;

    @ApiModelProperty("状态（0-已读，1-未读）")
    private Integer state;

}
