package com.eaosoft.railway.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zzs
 * @since 2023-03-16
 */
@TableName("rw_permissions")
@ApiModel(value = "Permissions对象", description = "")
public class Permissions implements Serializable {

    private static final long serialVersionUID = 1L;

    private String uid;

    @ApiModelProperty("权限")
    private Integer privilege;

    @ApiModelProperty("描述")
    private String caption;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    public Integer getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Integer privilege) {
        this.privilege = privilege;
    }
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public String toString() {
        return "Power{" +
            "uid=" + uid +
            ", privilege=" + privilege +
            ", caption=" + caption +
        "}";
    }
}
