package com.eaosoft.railway.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.eaosoft.railway.entity.Role;
import com.eaosoft.railway.entity.User;
import com.eaosoft.railway.service.IRoleService;
import com.eaosoft.railway.utils.ReqValue;
import com.eaosoft.railway.utils.RespValue;
import com.eaosoft.railway.utils.SignUpModel;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zzs
 * @since 2023-03-16
 */
@RestController
@RequestMapping("/railway/role")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    /**
     * 添加角色
     * @param reqValue
     * @return
     */
    @PostMapping("/addRold.do")
    public RespValue addRold(@RequestBody ReqValue reqValue){
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        Integer caption = jsonObject.getInteger("caption");
        String position = jsonObject.getString("position");
        String routeName = jsonObject.getString("routeName");

        Role role1 = new Role();
        // 判断职位名称是否为空
        if(StringUtils.isBlank(jsonObject.getString("position"))){
            return new RespValue(500,"The position cannot be empty",null);
        }else {
            // 判断职位名称是否已存在
            Role role = roleService.findRoleByPosition(position,routeName);
            if (role != null){
                return new RespValue(500,"The position already exists!",null);
            }
            role1.setPosition(position);
        }
        // Role role1 = new Role();
        // 判断身份是否为空
        if (caption == null){
            return  new RespValue(500,"The identity cannot empty",null);
        }
        // 判断身份是否合法
        if (caption.equals(0)  || caption.equals(1)){
            role1.setCaption(caption);
        }else {
            return new RespValue(500,"The identity selection error",null);
        }

        // 判断线路名称是否为空
        if (StringUtils.isBlank(jsonObject.getString("routeName"))){
            return new RespValue(500,"The routeName cannot be empty",null);
        }

        // 设置备注
        if (!StringUtils.isBlank(jsonObject.getString("note"))){
            role1.setNote(jsonObject.getString("note"));
        }
        role1.setRouteName(jsonObject.getString("routeName"));

        role1.setCreateTime(LocalDateTime.now());
        role1.setUpdateTime(LocalDateTime.now());

        // 添加角色
        int i = roleService.addRole(role1);
        if (i!=0){
            return new RespValue(200,"success",null);
        }
        return new RespValue(500,"error",null);
    }

    /**
     * 删除角色,可批量删除
     * @param reqValue
     * @return
     */
    @PostMapping("/deleteRole.do")
    public RespValue deleteRole(@RequestBody ReqValue reqValue){
        Object requestDatas = reqValue.getRequestDatas();
        int i = roleService.deleteRole(requestDatas);
        if (i!=0){
            return new RespValue(200,"success",null);
        }
        return new RespValue(500,"error",null);
    }

    /**
     * 查询该站点下所有角色
     * @param reqValue
     * @return
     */
    @PostMapping("/findAllRole.do")
    public RespValue findAllRole(@RequestBody ReqValue reqValue){
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        Role role = new Role();
        role.setRouteName(jsonObject.getString("routeName"));
        Integer pageSize = jsonObject.getInteger("pageSize");
        Integer currentPage = jsonObject.getInteger("currentPage");
        PageInfo<Role> pageInfo = roleService.findAllRole(currentPage,pageSize,role);
        return new RespValue(200,"success",pageInfo);

    }

    /**
     * 根据职位模糊查询所有角色信息
     * @param reqValue
     * @return
     */
    @PostMapping("/findRoleByPosition.do")
    public RespValue findRole(@RequestBody ReqValue reqValue){
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        Role role = new Role();
        role.setPosition(jsonObject.getString("position"));
        role.setRouteName(jsonObject.getString("station"));
        Integer pageSize = jsonObject.getInteger("pageSize");
        Integer currentPage = jsonObject.getInteger("currentPage");
        PageInfo<Role> pageInfo = roleService.findRole(currentPage,pageSize,role);
        return new RespValue(200,"success",pageInfo);
    }

    /**
     *
     * @param reqValue
     * @return
     */
    @PostMapping("/modifyRole.do")
    public RespValue modifyRole(@RequestBody ReqValue reqValue){
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        Role role = new Role();
        role.setUid(jsonObject.getString("uid"));
        if (!StringUtils.isBlank(jsonObject.getString("position"))){
            role.setPosition(jsonObject.getString("position"));
        }
        if (!StringUtils.isBlank(jsonObject.getString("note"))){
            role.setPosition(jsonObject.getString("position"));
        }
        int i = roleService.modifyRole(role);
        if (i != 0){
            return new RespValue(200,"success",null);
        }
        return new RespValue(500,"error",null);
    }


    /**
     * 查找所有的角色名称，用于添加角色时选择角色名称
     * @param reqValue
     * @return
     */
    @PostMapping("/findAllRoleName.do")
    public RespValue findAllRoleName(@RequestBody ReqValue reqValue){
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        String routeName = jsonObject.getString("routeName");
        List list = roleService.findAllRoleName(routeName);
        return new RespValue(200,"success",list);
    }
}
