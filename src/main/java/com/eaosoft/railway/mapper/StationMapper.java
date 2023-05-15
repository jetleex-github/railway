package com.eaosoft.railway.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eaosoft.railway.entity.Station;
import com.eaosoft.railway.vo.StationVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zzs
 * @since 2023-03-24
 */


@Mapper
public interface StationMapper extends BaseMapper<Station> {

    ArrayList<List> selectA(@Param("uid") String uid);

    List<StationVo> selectByRouteName(@Param("routeName") String routeName);
}
