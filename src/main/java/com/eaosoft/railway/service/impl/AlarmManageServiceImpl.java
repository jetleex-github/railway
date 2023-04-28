package com.eaosoft.railway.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.eaosoft.railway.entity.AlarmManage;
import com.eaosoft.railway.entity.LoginLog;
import com.eaosoft.railway.mapper.AlarmManageMapper;
import com.eaosoft.railway.service.IAlarmManageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eaosoft.railway.vo.AlarmVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zzs
 * @since 2023-04-07
 */
@Service
public class AlarmManageServiceImpl extends ServiceImpl<AlarmManageMapper, AlarmManage> implements IAlarmManageService {

    @Autowired
    private AlarmManageMapper alarmManageMapper;
    @Override
    public int insertAlarm(AlarmManage alarm) {
        int i = alarmManageMapper.insert(alarm);
        return i;
    }


    @Override
    public int deleteAlarm(String uid) {
        int i = alarmManageMapper.deleteById(uid);
        return i;
    }

    @Override
    public PageInfo<AlarmManage> selectAlarm(Integer currentPage, Integer pageSize,String equipName) {
        PageHelper.startPage(currentPage,pageSize);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("is_delete",1);
        if (!StringUtils.isBlank(equipName)){
            wrapper.eq("equip_name",equipName);
        }
        List<AlarmManage> list = alarmManageMapper.selectList(wrapper);
        PageInfo<AlarmManage> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public PageInfo<AlarmManage> selectAlarmByState(Integer currentPage, Integer pageSize) {
        PageHelper.startPage(currentPage,pageSize);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("is_delete",1);
        wrapper.eq("state",1);
        List<AlarmManage> list = alarmManageMapper.selectList(wrapper);
        PageInfo<AlarmManage> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public int dealAlarm(AlarmManage alarmManage) {
        int i = alarmManageMapper.updateById(alarmManage);
        return i;
    }

    @Override
    public List<AlarmVo> alarmInfoExport(String stationUid) {
        List<AlarmVo>  list = alarmManageMapper.alarmInfoExport(stationUid);
        return list;
    }
}
