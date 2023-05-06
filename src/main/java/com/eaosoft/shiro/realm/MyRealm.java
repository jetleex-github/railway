package com.eaosoft.shiro.realm;

import com.eaosoft.railway.entity.User;
import com.eaosoft.railway.mapper.UserMapper;

import com.eaosoft.railway.service.IUserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;


public class MyRealm extends AuthorizingRealm {


    @Resource
    private IUserService userService;


    //什么时候执行该方法: 当你进行权限校验时会执行该方法
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //1.根据token获取账号
        String s = authenticationToken.getPrincipal().toString();
        String username = authenticationToken.getPrincipal().toString();
        //2.根据账号查询用户信息
        User user = userService.selectByUsername(username);
        /*  User user = permissionOpenFeign.findByUsername(username);*/
        if (user != null) {
            //从数据库中获取的密码
          /*  ByteSource salt = ByteSource.Util.bytes(user.getSalt());
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(), salt, this.getName());*/
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(), this.getName());
            return info;
        }
        return null;
    }
}
