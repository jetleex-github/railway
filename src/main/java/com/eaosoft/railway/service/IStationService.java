package com.eaosoft.railway.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.eaosoft.railway.entity.Station;
import com.eaosoft.railway.vo.StationVo;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zzs
 * @since 2023-03-24
 */
public interface IStationService extends IService<Station> {

    List<StationVo> findAllStation(String routeName);

    int addStation(Station station);

    Station findByName(String stationName,String routeName);

    int modifyStationInfo(Station station);

    int deleteStation(String uid);


    ArrayList<List> find(String uid);

    Station findStation(String stationName);

    Station findStationByUid(String stationUid);
}
