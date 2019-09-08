package cn.van.redis.idempotency.service.impl;

import cn.van.redis.idempotency.expction.ServiceException;
import cn.van.redis.idempotency.service.TokenService;
import cn.van.redis.idempotency.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: TokenServiceImpl
 *
 * @author: Van
 * Date:     2019-09-03 00:13
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */

@Service
public class TokenServiceImpl implements TokenService {

    private static final String TOKEN_NAME = "token";

    @Autowired
    private JedisUtil jedisUtil;

    @Override
    public String createToken() {
        String str = RandomUtil.UUID32();
        StrBuilder token = new StrBuilder();
        token.append(Constant.Redis.TOKEN_PREFIX).append(str);

        jedisUtil.set(token.toString(), token.toString(), Constant.Redis.EXPIRE_TIME_MINUTE);

        return token.toString();
    }

    @Override
    public void checkToken(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_NAME);
        if (StringUtils.isBlank(token)) {// header中不存在token
            token = request.getParameter(TOKEN_NAME);
            if (StringUtils.isBlank(token)) {// parameter中也不存在token
                throw new ServiceException("parameter中也不存在token");
            }
        }

        if (!jedisUtil.exists(token)) {
            throw new ServiceException("请勿重复操作");
        }

        Long del = jedisUtil.del(token);
        if (del <= 0) {
            throw new ServiceException("请勿重复操作");
        }
    }

}

