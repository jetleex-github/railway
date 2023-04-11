package com.eaosoft.railway.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.eaosoft.railway.entity.Picture;
import com.eaosoft.railway.mapper.PictureMapper;
import com.eaosoft.railway.service.IPictureService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 判图系统 服务实现类
 * </p>
 *
 * @author zzs
 * @since 2023-04-10
 */
@Service
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture> implements IPictureService {
    @Autowired
    private PictureMapper pictureMapper;

    @Override
    public List findPicture() {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("flag",0);
        List list = pictureMapper.selectList(wrapper);
        return list;
    }
}
