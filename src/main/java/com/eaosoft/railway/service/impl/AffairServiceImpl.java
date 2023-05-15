package com.eaosoft.railway.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eaosoft.railway.entity.Affair;
import com.eaosoft.railway.mapper.AffairMapper;
import com.eaosoft.railway.service.IAffairService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 事件管理 服务实现类
 * </p>
 *
 * @author zzs
 * @since 2023-04-04
 */
@Service
public class AffairServiceImpl extends ServiceImpl<AffairMapper, Affair> implements IAffairService {

    @Autowired
    private AffairMapper affairMapper;

    @Override
    public int addAffair(Affair affair) {
        int i = affairMapper.insert(affair);
        return i;
    }

    /**
     * 查询所有站点的事件
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<Affair> findAffair(Integer currentPage, Integer pageSize) {
        PageHelper.startPage(currentPage,pageSize);
        List<Affair> list = affairMapper.selectAll();
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    /**
     * 根据站点uid查询该站点的所有事件
     * @param currentPage
     * @param pageSize
     * @param uid
     * @return
     */
    @Override
    public PageInfo<Affair> findAffairByStationName(Integer currentPage, Integer pageSize, String uid) {
        PageHelper.startPage(currentPage,pageSize);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("station_uid",uid);
        List list = affairMapper.selectList(wrapper);
        PageInfo<Affair> pageInfo = new PageInfo(list);
        return pageInfo;
    }
}
