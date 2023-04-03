package com.eaosoft.railway.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eaosoft.JWT.util.TokenModel;
import com.eaosoft.railway.entity.AuthToken;
import com.eaosoft.railway.mapper.AuthTokenMapper;
import com.eaosoft.railway.service.AuthTokenService;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author ZhouWenTao
 * @Date 2022/7/14 16:08
 * @Version 1.0
 */
@Service
public class AuthTokenServiceImpl extends ServiceImpl<AuthTokenMapper, AuthToken> implements AuthTokenService {
    @Override
    public void setToken(String token, long time) {
        AuthToken authToken = new AuthToken();
        authToken.setToken(token);
        Date date = new Date();
        date.setTime(time);
        authToken.setCreateTime(new Date());
        authToken.setEndTime(date);
        this.saveOrUpdate(authToken);
    }

    @Override
    public void deleteToken(String token) {
        this.remove(new LambdaQueryWrapper<AuthToken>().eq(AuthToken::getToken,token));
    }

    @Override
    public TokenModel verifyToken(String token) {
        TokenModel tokenModel=new TokenModel();
        List<AuthToken> list = this.list(new LambdaQueryWrapper<AuthToken>().eq(AuthToken::getToken, token));
        if(list.size()>0){
            AuthToken authToken = list.get(0);
            Date endTime = authToken.getEndTime();
            long time = endTime.getTime();
            if(new Date().getTime()<time){
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String format = sdf.format(endTime);
                tokenModel.setToken(token);
                tokenModel.setEndTime(format);
                tokenModel.setAlive(true);
                return tokenModel;
            }
        }
        tokenModel.setToken(null);
        tokenModel.setAlive(false);
        return tokenModel;
    }
}
