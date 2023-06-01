package com.eaosoft.railway.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.eaosoft.railway.entity.Unpack;
import com.github.pagehelper.PageInfo;

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

    PageInfo<Unpack> findUnpackInfo(Integer pageSize, Integer currentPage, String stationExit, String createTime,
                                    String endTime, String taskUid, String result, String checkUser,
                                    String equipSerialNo,String routeName);

    List<Unpack> findPictureByTaskUid(String taskUid);
}
