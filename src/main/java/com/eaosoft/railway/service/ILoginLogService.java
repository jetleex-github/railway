package com.eaosoft.railway.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.eaosoft.railway.entity.LoginLog;
import com.github.pagehelper.PageInfo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zzs
 * @since 2023-04-07
 */
public interface ILoginLogService extends IService<LoginLog> {

    void insertLoginLog(LoginLog loginLog);

    PageInfo<LoginLog> selectLoginLog(Integer currentPage, Integer pageSize);
}
