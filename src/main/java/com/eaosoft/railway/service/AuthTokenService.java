package com.eaosoft.railway.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.eaosoft.JWT.util.TokenModel;
import com.eaosoft.railway.entity.AuthToken;
import org.springframework.stereotype.Service;

/**
 * @Author ZhouWenTao
 * @Date 2022/7/14 16:07
 * @Version 1.0
 */
@Service
public interface AuthTokenService extends IService<AuthToken> {
    public void setToken(String token,long time);
    public void deleteToken(String token);
    public TokenModel verifyToken(String token);
}
