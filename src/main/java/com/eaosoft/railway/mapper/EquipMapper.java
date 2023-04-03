package com.eaosoft.railway.mapper;

import com.eaosoft.railway.entity.Equip;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zzs
 * @since 2023-03-27
 */

@Mapper
public interface EquipMapper extends BaseMapper<Equip> {
    List<String> selectEquipName(@Param("routeName")String routeName);
}
