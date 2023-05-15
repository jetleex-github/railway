package com.eaosoft.railway.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eaosoft.railway.entity.LoginLog;
import com.eaosoft.railway.mapper.LoginLogMapper;
import com.eaosoft.railway.service.ILoginLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zzs
 * @since 2023-04-07
 */
@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements ILoginLogService {

    @Autowired
    private LoginLogMapper loginLogMapper;


    /**
     * 添加登录日志
     * @param loginLog
     */
    @Override
    public void insertLoginLog(LoginLog loginLog) {
        loginLogMapper.insert(loginLog);
    }

    /**
     * 查询登录日志并分页
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<LoginLog> selectLoginLog(Integer currentPage, Integer pageSize) {
        PageHelper.startPage(currentPage,pageSize);
        List<LoginLog> list = loginLogMapper.selectLoginLog();
        PageInfo<LoginLog> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }
}
