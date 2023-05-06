package com.eaosoft.railway.service;

import com.eaosoft.railway.entity.Unpack;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 开包结果 服务类
 * </p>
 *
 * @author zzs
 * @since 2023-05-04
 */
public interface IUnpackService extends IService<Unpack> {

    int addUnpackInfo(Unpack unpack);

    List<Unpack> findUnpackInfo(Integer pageSize, Integer currentPage, String stationName,String createTime);
}
