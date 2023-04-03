package com.eaosoft.railway.mapper;

import com.eaosoft.railway.entity.Station;
import com.eaosoft.railway.entity.StationExit;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eaosoft.railway.vo.StationExitVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zzs
 * @since 2023-03-30
 */

@Mapper
public interface StationExitMapper extends BaseMapper<StationExit> {

    List<Station> selectExitName(@Param("stationUid") String stationUid);

    List<StationExitVo> selectAll();

    // List<StationExitVo> selectAll();
}
