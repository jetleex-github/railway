package com.eaosoft.railway.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eaosoft.railway.entity.Notice;
import com.eaosoft.railway.mapper.NoticeMapper;
import com.eaosoft.railway.service.INoticeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zzs
 * @since 2023-03-24
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements INoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    /**
     * 发布通知
     *
     * @param notice
     * @return
     */
    @Override
    public int addNotice(Notice notice) {
        int i = noticeMapper.insert(notice);
        return i;
    }

    /**
     * 删除通知
     *
     * @param notice
     * @return
     */
    //
    @Override
    public int deleteNotice(Notice notice) {
        // int i = noticeMapper.deleteById(uid,LocalDateTime.now());

        //int i = noticeMapper.deleteNotice(notice.getUid(),notice.getUpdateTime());

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("uid", notice.getUid());
        wrapper.eq("update_time", LocalDateTime.now());
        //wrapper.eq("is_deleted",0);
        //int i = noticeMapper.delete(wrapper);
        noticeMapper.updateById(notice);
        int i = noticeMapper.deleteById(notice);
        return i;
    }

    /**
     * 查找通知并完成分页
     *
     * @param title
     * @param username
     * @return
     */
    @Override
    public PageInfo findNotice(String title, String username, Integer currentPage, Integer pageSize) {
        /*PageHelper.startPage(currentPage,pageSize);
        QueryWrapper wrapper = new QueryWrapper<>();
        if (!StringUtils.isBlank(title)){
            wrapper.like("title",title);
        }
        if (!StringUtils.isBlank(username)){
            wrapper.like("username",username);
        }
        List list = noticeMapper.selectList(wrapper);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;*/
        PageHelper.startPage(currentPage, pageSize);
        List<Notice> list = noticeMapper.selectNotice(username, title);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;

    }


    @Override
    public Notice findNewNotice() {
        Notice n = noticeMapper.findNewNotice();
        return n;
    }


}
