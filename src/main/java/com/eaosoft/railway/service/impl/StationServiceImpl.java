package com.eaosoft.railway.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.eaosoft.railway.entity.Station;
import com.eaosoft.railway.mapper.StationMapper;
import com.eaosoft.railway.service.IStationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eaosoft.railway.vo.StationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zzs
 * @since 2023-03-24
 */
@Service
public class StationServiceImpl extends ServiceImpl<StationMapper, Station> implements IStationService {

    @Autowired
    private StationMapper stationMapper;

    /**
     * 查询所有的站点信息
     * @param uid
     * @return
     */
    @Override
    public ArrayList<List> find(String uid) {
       /* QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("uid",uid);
        List list = stationMapper.selectList(wrapper);*/
        ArrayList<List> list = stationMapper.selectA(uid);
        return list;
    }


    /**
     * 根据站点名称查询站点信息
     * @param stationName
     * @return
     */
    @Override
    public Station findStation(String stationName) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("station_name",stationName);
        Station station = stationMapper.selectOne(wrapper);
        return station;
    }

    /**
     * 根据线路名称查询该线路下的所有站点信息，用于树形菜单
     * @param routeName
     * @return
     */
    @Override
    public List<StationVo> findAllStation(String routeName) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("route_name",routeName);
        List<StationVo> list = stationMapper.selectByRouteName(routeName);
        return list;
    }

    /**
     * 添加站点信息
     * @param station
     * @return
     */
    @Override
    public int addStation(Station station) {
        int i = stationMapper.insert(station);

        return i;
    }

    /**
     * 查找所有站点名称
     * @param stationName
     * @return
     */
    @Override
    public Station findByName(String stationName,String routeName) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("station_name",stationName);
        wrapper.eq("route_name",routeName);
        Station station = stationMapper.selectOne(wrapper);
        return station;
    }

    /**
     * 修改站点信息
     * @param station
     * @return
     */

    @Override
    public int modifyStationInfo(Station station) {
        int i = stationMapper.updateById(station);
        return i;
    }

    /**
     * 删除站点
     * @param uid
     * @return
     */
    @Override
    public int deleteStation(String uid) {
        int i = stationMapper.deleteById(uid);
        return i;
    }


}
