package com.eaosoft.railway.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eaosoft.railway.entity.LoginLog;
import com.eaosoft.railway.service.ILoginLogService;
import com.eaosoft.railway.utils.ReqValue;
import com.eaosoft.railway.utils.RespValue;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zzs
 * @since 2023-04-07
 */
@RestController
@RequestMapping("/railway/loginLog")
public class LoginLogController {

    @Autowired
    private ILoginLogService loginLogService;

    /**
     *  查看登录日志
     * @param reqValue
     * @return
     */
    @PostMapping("/selectLoginLog.do")
    public RespValue selectLoginLog(@RequestBody ReqValue reqValue){
        Object requestDatas = reqValue.getRequestDatas();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestDatas));
        Integer pageSize = jsonObject.getInteger("pageSize");
        Integer currentPage = jsonObject.getInteger("currentPage");
        String state = jsonObject.getString("state");
        String username = jsonObject.getString("username");
        PageInfo<LoginLog> pageInfo = loginLogService.selectLoginLog(currentPage,pageSize,state,username);
        return new RespValue(200,"success",pageInfo);
    }

}
