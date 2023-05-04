package com.eaosoft.railway.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

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
 * @since 2023-03-17
 */
@TableName("rw_user")
@ApiModel(value = "User对象", description = "")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "uid")

    private String uid;

    @ApiModelProperty("工号")

    private String serialNo;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("身份")

    private Integer caption;

    @ApiModelProperty("密码")

    private String password;

    @ApiModelProperty("性别（0-女，1-男）")

    private Integer gender;

    @ApiModelProperty("年龄")

    private Integer age;

    @ApiModelProperty("手机号")

    private String phone;

    @ApiModelProperty("住址")

    private String address;

    @ApiModelProperty("状态")

    private Integer state;

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("身份证号")
    private String idCard;

    @ApiModelProperty("电子邮箱")
    private String email;

    @ApiModelProperty("入职时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty("离职时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime leaveTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @ApiModelProperty("所属站点")
    private String station;

    @ApiModelProperty("线路名称")
    private String routeName;

    @ApiModelProperty("职位")
    private String position;

    @ApiModelProperty("状态（0-不可用，1-可用）")
    @TableLogic(value = "1",delval = "0")
    private Integer isDeleted;

    /*public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public LocalDateTime getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(LocalDateTime leaveTime) {
        this.leaveTime = leaveTime;
    }
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }
    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "User{" +
            "uid=" + uid +
            ", serialNo=" + serialNo +
            ", username=" + username +
            ", caption=" + caption +
            ", password=" + password +
            ", gender=" + gender +
            ", age=" + age +
            ", phone=" + phone +
            ", address=" + address +
            ", createTime=" + createTime +
            ", leaveTime=" + leaveTime +
            ", updateTime=" + updateTime +
            ", station=" + station +
            ", isDeleted=" + isDeleted +
        "}";
    }*/
}
