package com.eaosoft.railway.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eaosoft.railway.entity.LoginLog;
import org.apache.ibatis.annotations.Mapper;

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
public interface LoginLogMapper extends BaseMapper<LoginLog> {

    List<LoginLog> selectLoginLog();
}
