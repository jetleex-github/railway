package com.eaosoft.railway.service.impl;

import com.eaosoft.railway.entity.Station;
import com.eaosoft.railway.entity.StationExit;
import com.eaosoft.railway.mapper.StationExitMapper;
import com.eaosoft.railway.service.IStationExitService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eaosoft.railway.vo.StationExitVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zzs
 * @since 2023-03-30
 */
@Service
public class StationExitServiceImpl extends ServiceImpl<StationExitMapper, StationExit> implements IStationExitService {


    @Autowired
    private StationExitMapper stationExitMapper;

    /**
     * 根据站点uid，查询其关联的出站口
     * @param stationUid
     * @return
     */
    @Override
    public List<Station> findExit(String stationUid) {
        List<Station> list = stationExitMapper.selectExitName(stationUid);
        return list;
    }

    /**
     * 添加出站口
     * @param stationExit
     * @return
     */
    @Override
    public int addStationExit(StationExit stationExit) {
        int i = stationExitMapper.insert(stationExit);
        return i;
    }

    /**
     * 查询所有的站口
     * @return
     */
    @Override
    public List<StationExitVo> findAllExit() {
        List<StationExitVo> list = stationExitMapper.selectAll();
        return list;
    }
}
