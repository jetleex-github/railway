package com.eaosoft.railway.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eaosoft.railway.entity.Affair;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 事件管理 Mapper 接口
 * </p>
 *
 * @author zzs
 * @since 2023-04-04
 */
@Mapper
public interface AffairMapper extends BaseMapper<Affair> {

    List<Affair> selectAll();
}
