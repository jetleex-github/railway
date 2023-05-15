package com.eaosoft.railway.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eaosoft.railway.entity.Notice;
import com.eaosoft.railway.entity.UnreadNotice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
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
public interface UnreadNoticeMapper extends BaseMapper<UnreadNotice> {

    void addUnreadNotice(@Param("noticeUid") String uid, @Param("userUid") String s, @Param("createTime") LocalDateTime createTime);

    List<Notice> findUnreadNotice(@Param("uid") String uid);
}
