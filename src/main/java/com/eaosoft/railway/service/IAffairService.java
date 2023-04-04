package com.eaosoft.railway.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.eaosoft.railway.entity.Affair;
import com.github.pagehelper.PageInfo;


/**
 * <p>
 * 事件管理 服务类
 * </p>
 *
 * @author zzs
 * @since 2023-04-04
 */
public interface IAffairService extends IService<Affair> {

    int addAffair(Affair affair);

    PageInfo<Affair> findAffair(Integer currentPage, Integer pageSize);

    PageInfo<Affair> findAffairByStationName(Integer currentPage, Integer pageSize, String uid);
}
