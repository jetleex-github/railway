package com.eaosoft.railway.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eaosoft.railway.entity.Role;
import com.eaosoft.railway.mapper.RoleMapper;
import com.eaosoft.railway.service.IRoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zzs
 * @since 2023-03-16
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    private RoleMapper roleMapper;

    /**
     * 根据职位名查找角色，用于创建职位是判断该职位是否已经存在
     * @param position
     * @return
     */
    @Override
    public Role findRoleByPosition(String position,String routeName) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("position",position);
        wrapper.eq("route_name",routeName);
        Role role = roleMapper.selectOne(wrapper);
        return role;
    }

    /**
     * 添加角色
     * @param role1
     * @return
     */
    @Override
    public int addRole(Role role1) {
        int insert = roleMapper.insert(role1);
        return insert;
    }

    /**
     * 删除角色,可批量删除
     * @param requestDatas
     * @return
     */
    @Override
    public int deleteRole(Object requestDatas) {
        int i = roleMapper.deleteBatchIds((Collection<?>) requestDatas);
        return i;
    }

    /**
     * 查询该站点下所有角色并完成分页
     * @param currentPage
     * @param pageSize
     * @param role
     * @return
     */
    @Override
    public PageInfo<Role> findAllRole(Integer currentPage, Integer pageSize, Role role) {
        PageHelper.startPage(currentPage,pageSize);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("route_name",role.getRouteName());
        List list = roleMapper.selectList(wrapper);
        PageInfo pageInfo =new PageInfo(list);
        return pageInfo;
    }

    @Override
    public PageInfo<Role> findRole(Integer currentPage, Integer pageSize,Role role) {
        PageHelper.startPage(currentPage,pageSize);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.like("position",role.getPosition());
        wrapper.like("route_name",role.getRouteName());
        List list = roleMapper.selectList(wrapper);
        PageInfo pageInfo =new PageInfo(list);
        return pageInfo;
    }

    @Override
    public int modifyRole(Role role) {
        int i = roleMapper.updateById(role);
        return i;
    }

    @Override
    public List findAllRoleName(String routeName) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("route_name",routeName);
        List list = roleMapper.selectList(wrapper);
        return list;
    }


}
