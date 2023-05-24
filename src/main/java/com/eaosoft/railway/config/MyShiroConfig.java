package com.eaosoft.railway.config;

import com.eaosoft.railway.filter.JWTFilter;
import com.eaosoft.railway.filter.URLPathMatchingFilter;
import com.eaosoft.railway.filter.UrlFilter;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ Program       :  com.ljnt.blog.config.MyShiroConfig
 * @ Description   :  Shrio配置类
 * @ Author        :  lj
 * @ CreateDate    :  2020-2-4 13:48
 */
@Configuration
public class MyShiroConfig {
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean=new ShiroFilterFactoryBean();

        //自定义拦截器
        Map<String, Filter> filterMap=new LinkedHashMap<>();
        filterMap.put("jwt", new JWTFilter());
        filterMap.put("url",new UrlFilter());
        // 访问权限配置
       // filterMap.put("requestURL",new URLPathMatchingFilter());
        shiroFilterFactoryBean.setFilters(filterMap);
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //不要用HashMap来创建Map，会有某些配置失效，要用链表的LinkedHashmap
        Map<String,String> filterRuleMap=new LinkedHashMap<>();




        /* 配置映射关系*/
        //authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问
        filterRuleMap.put("/","anon");
        filterRuleMap.put("/webjars/**","anon");
        filterRuleMap.put("/login","anon");
        filterRuleMap.put("/css/**","anon");
        filterRuleMap.put("/images/**","anon");
        filterRuleMap.put("/js/**","anon");
        filterRuleMap.put("/lib/**","anon");
        filterRuleMap.put("/railway/login/login.do","anon");
        filterRuleMap.put("/railway/login/equipLogin.do","anon");

        filterRuleMap.put("/railway/picture/drawingJudgment.do","anon");

        filterRuleMap.put("/railway/user/exportModel.do","anon");
        //filterRuleMap.put("/railway/**","authc");
        //拦截所有接口
        filterRuleMap.put("/**","jwt");

        filterRuleMap.put("/railway/**","url");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterRuleMap);
        return shiroFilterFactoryBean;
    }



    @Bean
    public SecurityManager securityManager(CustomRealm customRealm){
        //设置自定义Realm
        DefaultWebSecurityManager securityManager=new DefaultWebSecurityManager();
        securityManager.setRealm(customRealm);
        //关闭shiro自带的session
        DefaultSubjectDAO subjectDAO=new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator=new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        return securityManager;
    }


    /**
     * 配置代理会导致doGetAuthorizationInfo执行两次
     */
//    @Bean
//    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
//        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator=new DefaultAdvisorAutoProxyCreator();
//        //强制使用从cglib动态代理机制，防止重复代理可能引起代理出错问题
//        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
//        return defaultAdvisorAutoProxyCreator;
//    }

    /**
     * 授权属性源配置
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor=new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);

        return authorizationAttributeSourceAdvisor;

    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public CredentialsMatcher credentialsMatcher(){
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("MD5");
        /*credentialsMatcher.setHashIterations(1024);*/
        return credentialsMatcher;
    }
}
