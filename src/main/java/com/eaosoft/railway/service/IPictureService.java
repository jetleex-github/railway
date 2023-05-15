package com.eaosoft.railway.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.eaosoft.railway.entity.Picture;

import java.util.List;

/**
 * <p>
 * 判图系统 服务类
 * </p>
 *
 * @author zzs
 * @since 2023-04-10
 */
public interface IPictureService extends IService<Picture> {

    List findPicture();

    void insertPictures(Picture picture);

    List<Picture> selectPictureByStationUid(String stationUid);

    int addResult(Picture picture);

    Picture selectPicture();

    int passCheck(Picture picture);

    Picture selectUidByUrl(String frontUrl);
}
