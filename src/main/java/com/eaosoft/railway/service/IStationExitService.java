package com.eaosoft.railway.service;

import com.eaosoft.railway.entity.Station;
import com.eaosoft.railway.entity.StationExit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.eaosoft.railway.vo.StationExitVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zzs
 * @since 2023-03-30
 */
public interface IStationExitService extends IService<StationExit> {

    List<Station> findExit(String stationUid);

    int addStationExit(StationExit stationExit);

    // 查询所有的站口名称，用于树形菜单将站口绑定到站点上
    List<StationExitVo> findAllExit();
}
