package com.eaosoft.railway.controller;

import com.eaosoft.railway.entity.Notice;
import com.eaosoft.railway.entity.User;
import com.eaosoft.railway.service.IUnreadNoticeService;
import com.eaosoft.railway.service.IUserService;
import com.eaosoft.railway.utils.ReqValue;
import com.eaosoft.railway.utils.RespValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zzs
 * @since 2023-03-30
 */
@RestController
@RequestMapping("/railway/unreadNotice")
public class UnreadNoticeController {

    @Autowired
    private IUnreadNoticeService unreadNoticeService;

    @Autowired
    private IUserService userService;


    @PostMapping("/findUnread.do")
    public RespValue findUnread(@RequestBody ReqValue reqValue) {
        String token = reqValue.getToken();
        //根据token获取用户名
        String username = userService.findUserInfoByToken(token);
        if (StringUtils.isEmpty(username)){
            return new RespValue(500,"Please log in again ",null);
        }
        // 通过用户名获取用户信息
        User user = userService.selectByUsername(username);

        //判断该用户是否有未读通知
        List<Notice> list = unreadNoticeService.findNotice(user.getUid());
        if (list != null) {
            // 获取表后直接删除该数据
            // 在未读表中删除未读通知
            unreadNoticeService.deleteUnread(user.getUid());
            return new RespValue(200, "success", list);
        }
        return null;
    }


}
