package com.eaosoft.railway.service.impl;

import com.eaosoft.railway.entity.Unpack;
import com.eaosoft.railway.mapper.UnpackMapper;
import com.eaosoft.railway.service.IUnpackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 开包结果 服务实现类
 * </p>
 *
 * @author zzs
 * @since 2023-05-04
 */
@Service
public class UnpackServiceImpl extends ServiceImpl<UnpackMapper, Unpack> implements IUnpackService {

    @Autowired
    private UnpackMapper unpackMapper;

    @Override
    public int addUnpackInfo(Unpack unpack) {
        int i = unpackMapper.insert(unpack);
        return i;
    }

    @Override
    public List<Unpack> findUnpackInfo(Integer pageSize, Integer currentPage, String stationName,String createTime) {
        PageHelper.startPage(currentPage,pageSize);
        List<Unpack> list = unpackMapper.findUnpackInfo(stationName,createTime);
        return list;
    }
}