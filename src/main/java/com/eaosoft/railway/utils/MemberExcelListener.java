package com.eaosoft.railway.utils;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.eaosoft.railway.entity.Station;
import com.eaosoft.railway.entity.User;
import com.eaosoft.railway.service.IStationService;
import com.eaosoft.railway.service.IUserService;

import java.time.LocalDateTime;

/**
 * 监听数据导入
 */

public class MemberExcelListener extends AnalysisEventListener<User> {


    // 由于 MemberExcelListener 不能交给Spring管理 所以我们只能手动传入 userService
    public IUserService userService;
    public IStationService stationService;

    public MemberExcelListener() {

    }

    public MemberExcelListener(IUserService userService, IStationService stationService) {
        this.userService = userService;
        this.stationService = stationService;
    }

    @Override
    public void invoke(User users, AnalysisContext analysisContext) {

        if (users.getRealName().equals("张XXXX") || users.getIdCard().equals("411524************")) {
        } else {
            User user = new User();
            user.setUsername(String.valueOf(System.currentTimeMillis()));
            user.setRealName(users.getRealName());
            user.setPassword(MD5Utils.md5("123456"));
            user.setGender(users.getGender());
            user.setAge(users.getAge());
            user.setPhone(users.getPhone());
            user.setAddress(users.getAddress());
            user.setIdCard(users.getIdCard());
            user.setEmail(users.getEmail());
            user.setCreateTime(LocalDateTime.now());
            user.setUpdateTime(LocalDateTime.now());
            user.setRouteName(users.getRouteName());

            // 根据站点名称获取站点信息
            Station station = stationService.findStation(users.getStationUid());

            if (station != null) {
                user.setStationUid(station.getUid());
            }
            user.setCaption(1);
            String userSerial = getUserSerial(userService);
            user.setSerialNo(userSerial);
            userService.addUser(user);
        }

        // do something

        // System.out.println("读取Member=" + member);
        // do something

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

        // do something
        System.out.println("读取Excel完毕");
        // do something

    }

    public String getUserSerial(IUserService userService) {
        Boolean a = true;
        String substring = "";
        while (a) {
            String str = String.valueOf(System.currentTimeMillis());
            // 取后四位为编号
            substring = str.substring(str.length() - 4);
            // 验证编号是否重复
            int i = userService.findSerialNo(substring);
            if (i == 0) {
                a = false;
            }
        }
        return substring;
    }

}
