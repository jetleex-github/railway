package com.eaosoft.railway.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eaosoft.railway.entity.CheckIn;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zzs
 * @since 2023-03-31
 */

@Mapper
public interface CheckInMapper extends BaseMapper<CheckIn> {

    CheckIn selectCheckIn(@Param("serialNo")String serialNo, @Param("workday") LocalDateTime workday);

    int addCheckOut(@Param("serialNo")String serialNo,@Param("workday") LocalDateTime workday,@Param("offTime") LocalDateTime offTime);

    CheckIn selectCheckOut(@Param("serialNo")String serialNo, @Param("workday") LocalDateTime workday);
}
