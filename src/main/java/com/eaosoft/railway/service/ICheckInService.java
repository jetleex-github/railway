package com.eaosoft.railway.service;

import com.eaosoft.railway.entity.CheckIn;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zzs
 * @since 2023-03-31
 */
public interface ICheckInService extends IService<CheckIn> {

    CheckIn findCheckIn(CheckIn checkIn);

    int addCheckIn(CheckIn checkIn);

    int addCheckOut(CheckIn checkIn);

    CheckIn findCheckOut(CheckIn checkIn);
}
