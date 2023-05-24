package com.eaosoft.railway.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.eaosoft.railway.entity.Role;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zzs
 * @since 2023-03-16
 */
public interface IRoleService extends IService<Role> {

    Role findRoleByPosition(String position,String routeName);

    int addRole(Role role1);

    int deleteRole(Object requestDatas);

    PageInfo<Role> findAllRole(Integer currentPage, Integer pageSize, Role role);

    PageInfo<Role> findRole(Integer currentPage, Integer pageSize, Role role);

    int modifyRole(Role role);

    List findAllRoleName(String routeName);

    Role findRoleByCaption(Integer caption);
}
