package com.eaosoft.railway.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eaosoft.railway.entity.User;
import com.eaosoft.railway.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zzs
 * @since 2023-03-17
 */
@Repository
@Mapper
public interface UserMapper extends BaseMapper<User> {

    User selectByUsernameAndPassword(@Param("username") String username,@Param("password") String password);

    User selectByUsername(@Param("username")String username);

    int deleteToken(@Param("token")String token);

    List<User> selectAll();

    List<User> selectByCondition(@Param("route_name")String routeName);

    List<String> findAllUserUid();


    int selectUserNum();

    String findStationUidByUserUid(@Param("userUid") String uid);

    List<User> exportModel(@Param("uid") String uid);

    List<UserVo> exportUser();

    List<User> findAllUserByRouteNameAndStationUid(@Param("routeName") String routeName);

    List<User> findAllUser( @Param("routeName") String routeName);
}
