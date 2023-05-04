package com.eaosoft.shiro.config;

import com.eaosoft.shiro.realm.MyRealm;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import java.util.HashMap;
import javax.servlet.Filter;
@Configuration
public class ShiroConfig {


    @Bean
    public DefaultWebSecurityManager securityManager(){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm());
        return securityManager;
    }

    @Bean
    public Realm realm(){
        MyRealm myRealm = new MyRealm();
        myRealm.setCredentialsMatcher(credentialsMatcher());
        return myRealm;
    }

    @Bean
    public CredentialsMatcher credentialsMatcher(){
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("MD5");
        /*credentialsMatcher.setHashIterations(1024);*/
        return credentialsMatcher;
    }

    @Bean(value = "shiroFilter")
    public ShiroFilterFactoryBean filterFactoryBean(){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager());

        // 设置拦截规则
        HashMap<String,String> map = new HashMap<>();
        map.put("/**/login.html","anon");
        map.put("/swagger-ui.html", "anon");
        map.put("/swagger-resources/**", "anon");
        map.put("/v2/**", "anon");
        map.put("/webjars/**", "anon");
        map.put("/railway/login/**","anon");
        map.put("/**.html", "anon");
        map.put("/base/**","anon");
        map.put("/railway/login/login.do","anon");
        map.put("/railway/picture/drawingJudgment.do","anon");
        map.put("/railway/user/exportModel.do","anon");
        map.put("/railway/alarmManage/alarmInfoExport.do","anon");
        map.put("/sse/**","anon");
        map.put("/testPublish/**","anon");
       // map.put("/railway/sse/**","anon");
        map.put("/**", "authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);

        // 设置自定义认证的过滤器
//        HashMap<String, Filter> filterHashMap = new HashMap<>();
//        filterHashMap.put("authc",new LoginFilter());
//        shiroFilterFactoryBean.setFilters(filterHashMap);

        return shiroFilterFactoryBean;
    }

    @Bean //注册filter
    public FilterRegistrationBean<Filter> filterRegistrationBean(){
        FilterRegistrationBean<Filter> filterRegistrationBean=new FilterRegistrationBean<>();
        filterRegistrationBean.setName("shiroFilter");
        filterRegistrationBean.setFilter(new DelegatingFilterProxy());
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

    //开始shiro注解
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor sourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        sourceAdvisor.setSecurityManager(securityManager());
        return sourceAdvisor;
    }
    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator=new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        creator.setUsePrefix(true);
        return creator;
    }



}
