package com.eaosoft.railway.controller;

import com.eaosoft.railway.service.IAttendanceService;
import com.eaosoft.railway.utils.RespValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

/**
 * <p>
 *  前端控制器
 * </p>
 *打卡
 * @author zzs
 * @since 2023-03-29
 */
@Controller
@RequestMapping("/railway/attendance")
public class AttendanceController {

    @Autowired
    private IAttendanceService attendanceService;

    @PostMapping("/planWork.do")
    public RespValue planWork(@RequestBody RespValue respValue){

        return null;
    }

}
