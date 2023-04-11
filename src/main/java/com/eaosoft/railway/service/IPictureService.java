package com.eaosoft.railway.service;

import com.eaosoft.railway.entity.Picture;
import com.baomidou.mybatisplus.extension.service.IService;

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
}