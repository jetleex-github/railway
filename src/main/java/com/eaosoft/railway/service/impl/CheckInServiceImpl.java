package com.eaosoft.railway.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eaosoft.railway.entity.CheckIn;
import com.eaosoft.railway.mapper.CheckInMapper;
import com.eaosoft.railway.service.ICheckInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zzs
 * @since 2023-03-31
 */
@Service
public class CheckInServiceImpl extends ServiceImpl<CheckInMapper, CheckIn> implements ICheckInService {

    @Autowired
    private CheckInMapper checkInMapper;

    /**
     * 插叙是否已上班打卡
     * @param checkIn
     * @return
     */
    @Override
    public CheckIn findCheckIn(CheckIn checkIn) {
        /*QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("user_uid",checkIn.getUid());
        wrapper.eq("workday",checkIn.getWorkday());
        CheckIn checkIn1 = checkInMapper.selectOne(wrapper);*/
        CheckIn checkIn1 = checkInMapper.selectCheckIn(checkIn.getSerialNo(),checkIn.getWorkday());
        return checkIn1;
    }

    @Override
    public int addCheckIn(CheckIn checkIn) {
        int i = checkInMapper.insert(checkIn);
        return i;
    }

    @Override
    public int addCheckOut(CheckIn checkIn) {
        int i = checkInMapper.addCheckOut(checkIn.getSerialNo(),checkIn.getWorkday(),checkIn.getOffTime());
        return i;
    }

    @Override
    public CheckIn findCheckOut(CheckIn checkIn) {

        CheckIn check = checkInMapper.selectCheckOut(checkIn.getSerialNo(),checkIn.getWorkday());
        return check;
    }
}
