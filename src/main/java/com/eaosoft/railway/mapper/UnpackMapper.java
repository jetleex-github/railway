package com.eaosoft.railway.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eaosoft.railway.entity.Unpack;
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

    List<Unpack> findUnpackInfo(@Param("stationExit") String stationExit,
                                @Param("createTime") String createTime,
                                @Param("endTime") String endTime,
                                @Param("taskUid")String taskUid,
                                @Param("result")String result,
                                @Param("checkUser")String checkUser,
                                @Param("equipSerialNo")String equipSerialNo,
                                @Param("routeName") String routeName);
}
