package com.eaosoft.railway.config;

import com.eaosoft.railway.entity.JWTToken;
import com.eaosoft.railway.entity.User;
import com.eaosoft.railway.service.IUserService;
import com.eaosoft.railway.utils.RedisUtil;
import com.eaosoft.railway.utils.TokenUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @ Program       :  com.ljnt.blog.config.CustomRealm
 * @ Description   :  自定义Realm，实现Shiro认证
 * @ Author        :  lj
 * @ CreateDate    :  2020-2-4 18:15
 */
@Component
public class CustomRealm extends AuthorizingRealm {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    private IUserService userService;
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 用户授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("用户授权");
        System.out.println("===>>>"+principalCollection.toString());
        String username=TokenUtil.getUsername(principalCollection.toString());
        SimpleAuthorizationInfo info= new SimpleAuthorizationInfo();
        //正确的业务流程是到数据库拿该用户的权限再去进行授权的，这里只是简单的直接授权
        if (username.equals("admin")){
            Set<String> role=new HashSet<>();
            role.add("admin");
            info.setRoles(role);
        }else {
            Set<String> role=new HashSet<>();
            role.add("user");
            info.setRoles(role);
        }
        return info;
//        SimpleAuthorizationInfo info= new SimpleAuthorizationInfo();
//        Set<String> role=new HashSet<>();
//        role.add("user");
//        info.setRoles(role);
//        return info;
    }

    /**
     * 用户身份认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
       //System.out.println("身份认证");
        String token= (String) authenticationToken.getCredentials();
        String username= TokenUtil.getUsername(token);
        //根据账号查询用户信息
        // 判断redis中是否存在，存在则认证通过
        boolean b = redisUtil.hasKey(username);
        if (b){
            SimpleAuthenticationInfo info =new SimpleAuthenticationInfo(token,token,"MyRealm");
            return info;
        }

        // 在数据库中查询是否存在该用户
//        User user = userService.selectByUsername(username);
//        if (user != null) {
//            //从数据库中获取的密码
//            SimpleAuthenticationInfo info =new SimpleAuthenticationInfo(token,token,"MyRealm");
//            return info;
//        }
        return null;
    }
}
