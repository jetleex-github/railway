package com.eaosoft.railway.service;

import com.eaosoft.railway.entity.Notice;
import com.eaosoft.railway.entity.UnreadNotice;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zzs
 * @since 2023-03-30
 */
public interface IUnreadNoticeService extends IService<UnreadNotice> {

    void addUnreadNotice(Notice notice);

    List<Notice> findNotice(String uid);

    void deleteUnread(String userUid);
}
