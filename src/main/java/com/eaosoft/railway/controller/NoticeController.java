package com.eaosoft.railway.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.eaosoft.railway.entity.Notice;
import com.eaosoft.railway.entity.User;
import com.eaosoft.railway.service.INoticeService;
import com.eaosoft.railway.service.IUnreadNoticeService;
import com.eaosoft.railway.service.IUserService;
import com.eaosoft.railway.utils.ReqValue;
import com.eaosoft.railway.utils.RespValue;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zzs
 * @since 2023-03-24
 */
@RestController
@RequestMapping("/railway/notice")
public class NoticeController {

    @Autowired
    private INoticeService noticeService;

    @Autowired
    private IUnreadNoticeService unreadNoticeService;


    /**
     * 发布通知
     * @param reqValue
     * @return
     */
    @PostMapping("/addNotice.do")
    public RespValue addNotice(@RequestBody ReqValue reqValue){
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        Notice notice = new Notice();
        notice.setTitle(jsonObject.getString("title"));
        notice.setContent(jsonObject.getString("content"));
        notice.setUsername(jsonObject.getString("username"));
        notice.setCreateTime(LocalDateTime.now());
        int i = noticeService.addNotice(notice);
        if (i!=0){

            // 获取最新发布的通知
            Notice notice1 = noticeService.findNewNotice();

            // 为所有在职用户设置未读通知
            //unreadNoticeController.addUnreadNotice(notice1);
            unreadNoticeService.addUnreadNotice(notice1);


            return new RespValue(200,"success",null);
        }
        return new RespValue(500,"error",null);
    }


    /**
     * 删除通知
     * @param reqValue
     * @return
     */

    @PostMapping("/deleteNotice.do")
    public RespValue deleteNote(@RequestBody ReqValue reqValue){
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        String uid = jsonObject.getString("uid");
        if (StringUtils.isBlank(uid)){
            return new RespValue(500,"The uid cannot empty",null);
        }
        Notice notice = new Notice();
        notice.setUid(uid);
        notice.setUpdateTime(LocalDateTime.now());
        int i =noticeService.deleteNotice(notice);
        if (i!=0){
            return new RespValue(200,"success",null);
        }
        return new RespValue(500,"error",null);
    }


    /**
     * 如果有条件，根据条件查找近一年通知，没条件直接查询近三个月的通知
     * @param reqValue
     * @return
     *
     */
    @PostMapping("/findNotice.do")
    public RespValue findNotice(@RequestBody ReqValue reqValue){
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        String title = jsonObject.getString("title");
        String username = jsonObject.getString("username");
        Integer pageSize = jsonObject.getInteger("pageSize");
        Integer currentPage = jsonObject.getInteger("currentPage");
        PageInfo list = noticeService.findNotice(title,username,currentPage,pageSize);
        return new RespValue(200,"success",list);
    }


}
