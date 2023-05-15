package com.eaosoft.railway.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.eaosoft.railway.entity.User;
import com.eaosoft.railway.vo.UserVo;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

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

  //  String getToken(User user);

    int deleteToken(String token);

    int modifyUserInfo(User user);

    int modifyPassword(User user);


  //  String createToken(User user);

    String findUserInfoByToken(String token);

    int batchDelete(Object requestDatas);

    PageInfo<User> findAll(Integer currentPage, Integer pageSize, User user);

    int changeState(User user);

    List<String> findAllUserUid();



    int findSerialNo(String serialNo);

    List<User> findByIdCard(String idCard);

    List<User>  findByEmail(String email);

    int findUserNum();

    User findBySerialNo(String serialNo);

    // 根据userUid，查询其所在站点uid
    String findStationUidByUserUid(String uid);

    List<User> exportModel(String uid);

    void importUser(MultipartFile file, IUserService userService);

    List<UserVo> exportUser();

    int changeIdentity(User user);



    /* void addUnreadNotice(String uid, String s);*/
}
