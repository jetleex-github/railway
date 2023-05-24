package com.eaosoft.railway.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eaosoft.railway.entity.Equip;
import com.eaosoft.railway.mapper.EquipMapper;
import com.eaosoft.railway.service.IEquipService;
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
 * @since 2023-03-27
 */
@Service
public class EquipServiceImpl extends ServiceImpl<EquipMapper, Equip> implements IEquipService {

    @Autowired
    private EquipMapper equipMapper;

    /**
     * 根据站点名查询设备
     * @param stationExitUid
     * @return
     */
    @Override
    public List<Equip> findEquip(String stationExitUid) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("station_exit_uid",stationExitUid);
        wrapper.eq("code",1);
        wrapper.eq("repair",1);
        List list = equipMapper.selectList(wrapper);
        return list;
    }

    @Override
    public int addEquip(Equip equip) {
        int i = equipMapper.insert(equip);
        return i;
    }

    @Override
    public List<String> findEquipName(String routeName) {
        List<String> list = equipMapper.selectEquipName(routeName);
        return list;
    }



    /**
     * 查找未绑定的设备
     * @param stationUid
     * @param equipName
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<Equip> findUnboundEquip(String stationUid,String equipName,Integer currentPage,Integer pageSize) {
        PageHelper.startPage(currentPage,pageSize);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("station_uid",stationUid);
        if (!StringUtils.isBlank(equipName)){
            wrapper.eq("equip_name",equipName);
        }
        wrapper.eq("code",0);
        wrapper.eq("repair",1);
        List list = equipMapper.selectList(wrapper);
        PageInfo<Equip> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public int boundEquip(Equip equip) {
        int i = equipMapper.updateById(equip);
        return i;
    }

    @Override
    public int removeBound(Equip equip) {
        int i = equipMapper.updateById(equip);
        return i;
    }

    @Override
    public Equip findEquipBySerialNo(String serialNo) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("serial_no",serialNo);
        Equip equip = equipMapper.selectOne(wrapper);
        return equip;
    }

    @Override
    public int equipRepair(String serialNo) {
        int i = equipMapper.equipRepair(serialNo);
        return i;
    }

    @Override
    public int equipDel(String serialNo) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("serial_no",serialNo);
        int i = equipMapper.delete(wrapper);
        return i;
    }

    @Override
    public Equip equipLogin(String username) {
        Equip equip = equipMapper.equipLogin(username);
        return equip;
    }

    @Override
    public int updateEquip(Equip equip) {
        int i = equipMapper.updateById(equip);
        return i;
    }

    @Override
    public PageInfo<Equip> selectEquipByCondition(Equip equip, Integer pageSize, Integer currentPage) {
        PageHelper.startPage(currentPage,pageSize);

        List<Equip> list = equipMapper.selectEquipByCondition(
                equip.getStationUid(),equip.getEquipName(), equip.getSerialNo(),equip.getState());
        PageInfo pageInfo = new PageInfo(list);

        return pageInfo;
    }

    @Override
    public Equip findEquipByEquipUid(String equipUid) {
        Equip equip = equipMapper.selectById(equipUid);
        return equip;
    }
}
