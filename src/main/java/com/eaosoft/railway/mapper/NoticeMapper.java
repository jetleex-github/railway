package com.eaosoft.railway.mapper;

import com.eaosoft.railway.entity.Notice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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
public interface NoticeMapper extends BaseMapper<Notice> {


    List<Notice> selectNotice(@Param("username") String username, @Param("title") String title);

    Notice findNewNotice();

    void addUnreadNotice(@Param("noticeUid") String uid, @Param("userUid") String s, @Param("createTime") LocalDateTime createTime);

    int deleteNotice(@Param("uid") String uid,@Param("updateTime") LocalDateTime updateTime);
}
