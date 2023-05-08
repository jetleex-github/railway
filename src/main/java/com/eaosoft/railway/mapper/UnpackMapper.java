package com.eaosoft.railway.mapper;

import com.eaosoft.railway.entity.Unpack;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 开包结果 Mapper 接口
 * </p>
 *
 * @author zzs
 * @since 2023-05-04
 */
@Mapper
public interface UnpackMapper extends BaseMapper<Unpack> {

    List<Unpack> findUnpackInfo(@Param("stationName") String stationName,@Param("createTime") String createTime);
}
