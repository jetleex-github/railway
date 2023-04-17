package com.eaosoft.railway.service;

import com.alibaba.fastjson.JSONObject;
import com.eaosoft.railway.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zzs
 * @since 2023-03-17
 */
public interface IUserService extends IService<User> {


    User login(String username, String password);

    User selectByUsername(String username);
    PageInfo<User> findByUsername(Integer currentPage,Integer pageSize,String username);


    int addUser(User user);

    String getToken(User user);

    int deleteToken(String token);

    int modifyUserInfo(User user);

    int modifyPassword(User user);


    String createToken(User user);

    String findUserInfoByToken(String token);

    int batchDelete(Object requestDatas);

    PageInfo<User> findAll(Integer currentPage, Integer pageSize, User user);

    int changeState(User user);

    List<String> findAllUserUid();



    User findSerialNo(String serialNo);

    User findByIdCard(String idCard);

    User findByEmail(String email);

    int findUserNum();

    User findBySerialNo(String serialNo);

    // 根据userUid，查询其所在站点uid
    String findStationUidByUserUid(String uid);

    /* void addUnreadNotice(String uid, String s);*/
}
