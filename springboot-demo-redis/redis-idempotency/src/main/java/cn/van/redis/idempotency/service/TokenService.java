package cn.van.redis.idempotency.service;


import javax.servlet.http.HttpServletRequest;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: TokenService
 *
 * @author: Van
 * Date:     2019-09-03 00:11
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
public interface TokenService {

    String createToken();

    void checkToken(HttpServletRequest request);
}
