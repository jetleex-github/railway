package com.eaosoft.railway.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.eaosoft.railway.entity.AlarmManage;
import com.eaosoft.railway.vo.AlarmVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zzs
 * @since 2023-04-07
 */
public interface IAlarmManageService extends IService<AlarmManage> {

    int insertAlarm(AlarmManage alarm);

    int deleteAlarm(String uid);

    PageInfo<AlarmManage> selectAlarm(Integer currentPage, Integer pageSize,String equipName);

    PageInfo<AlarmManage> selectAlarmByState(Integer currentPage, Integer pageSize);

    int dealAlarm(AlarmManage alarmManage);

    List<AlarmVo> alarmInfoExport(String stationUid);
}
