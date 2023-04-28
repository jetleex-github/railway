package com.eaosoft.railway.mapper;

import com.eaosoft.railway.entity.AlarmManage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eaosoft.railway.vo.AlarmVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zzs
 * @since 2023-04-07
 */
@Mapper
public interface AlarmManageMapper extends BaseMapper<AlarmManage> {

    List<AlarmVo> alarmInfoExport(@Param("stationUid") String stationUid);
}
