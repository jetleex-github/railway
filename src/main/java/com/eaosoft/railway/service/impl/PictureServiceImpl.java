package com.eaosoft.railway.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eaosoft.railway.entity.Picture;
import com.eaosoft.railway.mapper.PictureMapper;
import com.eaosoft.railway.service.IPictureService;
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

    /**
     *  将图片地址存入数据库
     * @param picture
     */
    @Override
    public void insertPictures(Picture picture) {
        pictureMapper.insert(picture);
    }

    @Override
    public List<Picture> selectPictureByStationUid(String stationUid) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("station_uid",stationUid);
        wrapper.eq("flag",0);

        List list = pictureMapper.selectList(wrapper);
        return list;
    }

    @Override
    public int addResult(Picture picture) {
        int i = pictureMapper.updateById(picture);
        return i;
    }

    @Override
    public Picture selectPicture() {
        Picture picture = pictureMapper.selectPicture();
        return picture;
    }

    @Override
    public int passCheck(Picture picture) {
        int i = pictureMapper.updateById(picture);
        return i;
    }

    @Override
    public Picture selectUidByUrl(String frontUrl) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("front_picture",frontUrl);
        Picture picture = pictureMapper.selectOne(wrapper);
        return picture;
    }
}
