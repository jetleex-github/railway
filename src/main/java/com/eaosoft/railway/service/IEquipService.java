package com.eaosoft.railway.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.eaosoft.railway.entity.Equip;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zzs
 * @since 2023-03-27
 */
public interface IEquipService extends IService<Equip> {

    List<Equip> findEquip(String stationExitUid);

    int addEquip(Equip equip);

    List findEquipName(String routeName);


    PageInfo<Equip> findUnboundEquip(String stationUid, String equipName,Integer currentPage, Integer pageSize);

    int boundEquip(Equip equip);

    int removeBound(Equip equip);

    Equip findEquipBySerialNo(String serialNo);

    int equipRepair(String serialNo);

    int equipDel(String serialNo);

    Equip equipLogin(String username);

    int updateEquip(Equip equip);

    PageInfo<Equip> selectEquipByCondition(Equip equip, Integer pageSize, Integer currentPage);
}
