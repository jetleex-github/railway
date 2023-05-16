package com.eaosoft.railway.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eaosoft.railway.entity.User;
import com.eaosoft.railway.mapper.UserMapper;
import com.eaosoft.railway.service.IUserService;
import com.eaosoft.railway.utils.MemberExcelListener;
import com.eaosoft.railway.utils.TokenUtil;
import com.eaosoft.railway.vo.UserVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zzs
 * @since 2023-03-17
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    /**
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public User login(String username, String password) {
        User user  =  userMapper.selectByUsernameAndPassword(username,password);
        return user;
    }

    /**
     * 根据用户名模糊查询用户
     * @param username
     * @return
     */

    @Override
    public PageInfo<User> findByUsername(Integer currentPage,Integer pageSize,String username) {
        PageHelper.startPage(currentPage,pageSize);
        QueryWrapper wrapper = new QueryWrapper();
        // wrapper.eq("username",username);
        wrapper.like("username",username);
        List list = userMapper.selectList(wrapper);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    /**
     * Check whether the username exists
     * @param username
     * @return
     */
    @Override
    public User selectByUsername(String username) {
       User user= userMapper.selectByUsername(username);
        return user;
    }

    /**
     * addUser
     * @param user
     * @return
     */
    @Override
    public int addUser(User user) {
        int insert = userMapper.insert(user);
        return insert;
    }


    /**
     * 生成token
     * @param user
     * @return
     */
//    @Override
//    public String getToken(User user) {
//       /* Date start = new Date();
//        long currentTime = System.currentTimeMillis() + (60*8)* 60 * 1000;
//        Date end = new Date(currentTime);
//        String token = "";
//        // JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secret)).build();
//        Algorithm algorithm = Algorithm.HMAC256(user.getPassword());
//        token = JWT.create()
//                .withAudience(String.valueOf(user.getUsername()))
//                .withIssuedAt(start).withExpiresAt(end)
//                .sign(algorithm);*/
//        long currentTime = System.currentTimeMillis() + (60*8)* 60 * 1000;
//        String token = Jwts.builder()
//                //分类
//                .setSubject("AUTH-USER")
//
//                //设置token有效时长
//                .setExpiration(new Date(currentTime))
//
//                //设置主体部分
//                .claim("username", user.getUsername())
//                .claim("password", user.getPassword())
//
//                //签名部分
//                .signWith(SignatureAlgorithm.HS512, tokenSignKey)
//                .compressWith(CompressionCodecs.GZIP)
//                .compact();
//        authTokenService.deleteToken(token);
//        //save
//        authTokenService.setToken(token,currentTime);
//        return token;
//    }

    /**
     * 退出登录
     * @param token
     * @return
     */
    @Override
    public int deleteToken(String token) {
        int i = userMapper.deleteToken(token);
        return i;

    }

    /**
     * 根据uid修改用户信息
     * @param user
     * @return
     */
    @Override
    public int modifyUserInfo(User user) {
        int i = userMapper.updateById(user);
        return i;
    }

    /**
     * 修改用户密码
     * @param user
     * @return
     */
    @Override
    public int modifyPassword(User user) {
        int i = userMapper.updateById(user);
        return i;
    }

    /**
     *  创建token
     * @param user
     * @return
     */
//    @Override
//    public String createToken(User user) {
//        long currentTime = System.currentTimeMillis() + (60*8)* 60 * 1000;
//        String token = Jwts.builder()
//                //分类
//                .setSubject("AUTH-USER")
//
//                //设置token有效时长
//                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
//
//                //设置主体部分
//                .claim("username", user.getUsername())
//                .claim("password", user.getPassword())
//
//                //签名部分
//                .signWith(SignatureAlgorithm.HS512, tokenSignKey)
//                .compressWith(CompressionCodecs.GZIP)
//                .compact();
//
//        return token;
//    }

    /**
     * 根据token获取用户名
     * @param token
     * @return
     */
    @Override
    public String findUserInfoByToken(String token) {
//        try {
//            if (StringUtils.isEmpty(token))
//                return "";
//            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
//            Claims claims = claimsJws.getBody();
//            // 查询token有效期
//            /*Date expiration = claims.getExpiration();
//            System.out.println("expiration===>"+expiration);*/
//            return (String) claims.get("username");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
        if (StringUtils.isEmpty(token))
            return "";
        String username = TokenUtil.getUsername(token);
        return username;
    }

    /**
     * 员工离职
     * @param requestDatas
     * @return
     */
    @Override
    public int batchDelete(Object requestDatas) {
        int i = userMapper.deleteBatchIds((Collection<?>) requestDatas);
        return i;
    }

    /**
     * 模糊查询该站点下所有用户信息
     * @param currentPage 第几页
     * @param pageSize 每页显示条数
     * @param user
     * @return
     */
    @Override
    public PageInfo<User> findAll(Integer currentPage, Integer pageSize, User user) {
        PageHelper.startPage(currentPage,pageSize);
        List<User> outInformations = userMapper.selectByCondition(user.getRouteName());
        PageInfo pageInfo = new PageInfo(outInformations);
        return pageInfo;
    }


    /**
     * 修改用户状态
     * @param user
     * @return
     */
    @Override
    public int changeState(User user) {
        /*QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("state",user.getState());*/
        int i = userMapper.updateById(user);
        return i;
    }

    /**
     * 查询所有在职的用户名
     * @return
     */
    @Override
    public List<String> findAllUserUid() {
        List<String> list =userMapper.findAllUserUid();
        return list;
    }




    /**
     * 查询工号是否重复
     * @param serialNo
     * @return
     */
    @Override
    public int findSerialNo(String serialNo) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("serial_no",serialNo);
        User user = userMapper.selectOne(wrapper);
        if (user ==null){
            return 0;
        }
        return 1;
    }

    @Override
    public  List<User> findByIdCard(String idCard) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("id_card",idCard);
        List<User> list = userMapper.selectList(wrapper);
        return list;
    }

    @Override
    public List<User>  findByEmail(String email) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("email",email);
       // List<User>  user = userMapper.selectOne(wrapper);
        List<User> list = userMapper.selectList(wrapper);
        return list;
    }

    @Override
    public int findUserNum() {
        QueryWrapper wrapper = new QueryWrapper();
        int i = userMapper.selectUserNum();
        return i;
    }

    @Override
    public User findBySerialNo(String serialNo) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("serial_no",serialNo);
        User user = userMapper.selectOne(wrapper);
        return user;
    }

    @Override
    public String findStationUidByUserUid(String uid){
        String stationUid = userMapper.findStationUidByUserUid(uid);
        return stationUid;
    }

    @Override
    public List<User> exportModel(String uid) {
        List<User> user = userMapper.exportModel(uid);
        return user;
    }

    // 批量导入人员信息
    @Override
    public void importUser(MultipartFile file, IUserService userService){
        try {
            EasyExcel.read(file.getInputStream(), User.class, new MemberExcelListener(userService)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 导出今日导入的人员信息
    @Override
    public List<UserVo> exportUser() {
        List<UserVo> userVos = userMapper.exportUser();
        return userVos;
    }

    // 将普通用户设为管理员
    @Override
    public int changeIdentity(User user) {
      /*  QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("uid",uid);
        wrapper.eq("station","");
        wrapper.eq("caption",0);*/
        int i = userMapper.updateById(user);
        return i;
    }


}
