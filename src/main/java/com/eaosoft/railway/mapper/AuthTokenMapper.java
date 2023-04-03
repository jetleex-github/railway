package com.eaosoft.railway.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eaosoft.railway.entity.AuthToken;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author ZhouWenTao
 * @Date 2022/7/14 16:06
 * @Version 1.0
 */
@Repository
@Mapper
public interface AuthTokenMapper extends BaseMapper<AuthToken> {
}
