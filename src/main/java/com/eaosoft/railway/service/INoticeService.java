package com.eaosoft.railway.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.eaosoft.railway.entity.Notice;
import com.github.pagehelper.PageInfo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zzs
 * @since 2023-03-24
 */
public interface INoticeService extends IService<Notice> {

    int addNotice(Notice notice);

    int deleteNotice(Notice notice);

    PageInfo findNotice(String title, String username, Integer currentPage, Integer pageSize);


    Notice findNewNotice();
;
}
