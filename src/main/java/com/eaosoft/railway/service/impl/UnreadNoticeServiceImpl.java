package com.eaosoft.railway.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.eaosoft.railway.entity.Notice;
import com.eaosoft.railway.entity.UnreadNotice;
import com.eaosoft.railway.mapper.UnreadNoticeMapper;
import com.eaosoft.railway.mapper.UserMapper;
import com.eaosoft.railway.service.IUnreadNoticeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zzs
 * @since 2023-03-30
 */
@Service
public class UnreadNoticeServiceImpl extends ServiceImpl<UnreadNoticeMapper, UnreadNotice> implements IUnreadNoticeService {


    @Autowired
    private UnreadNoticeMapper unreadNoticeMapper;

    @Autowired
    private UserMapper userMapper;



    @Override
    public void addUnreadNotice(Notice notice) {
        List<String> list= userMapper.findAllUserUid();
        for (int j = 0; j < list.size(); j++) {
            UnreadNotice unreadNotice = new UnreadNotice();
            // 为所有人设置未读通知
            unreadNotice.setNoticeUid(notice.getUid());
            unreadNotice.setUserUid(list.get(j));
            unreadNotice.setCreateTime(LocalDateTime.now());
            unreadNoticeMapper.insert(unreadNotice);
        }
    }

    @Override
    public List<Notice> findNotice(String uid) {
        List<Notice> list = unreadNoticeMapper.findUnreadNotice(uid);
        return list;
    }

    @Override
    public void deleteUnread(String userUid) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("user_uid",userUid);
        unreadNoticeMapper.delete(wrapper);
    }
}
