package com.eaosoft.railway.mapper;

import com.eaosoft.railway.entity.Picture;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 判图系统 Mapper 接口
 * </p>
 *
 * @author zzs
 * @since 2023-04-10
 */

@Mapper
public interface PictureMapper extends BaseMapper<Picture> {

    Picture selectOneByStationUid(String stationUid);
}
