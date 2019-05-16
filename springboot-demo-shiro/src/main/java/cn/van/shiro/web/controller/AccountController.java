package cn.van.shiro.web.controller;


import cn.van.shiro.domain.ApiResult;
import cn.van.shiro.domain.IpUtil;
import cn.van.shiro.domain.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "账户相关")
@RestController
@RequestMapping("/account")
public class AccountController {

	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	private StringRedisTemplate redisTemplate;

	@ApiOperation(value = "注册")
//	@ApiImplicitParams({
//			@ApiImplicitParam(name = "userName",value = "用户名",required = true),
//			@ApiImplicitParam(name = "password",value = "密码",required = true),
//	})
	@PostMapping("/register")
	public ApiResult register(@RequestBody SysUser sysUser, HttpServletRequest request) {
		logger.info("sysUser:{}",sysUser);
		String tokenKey = redisTemplate.opsForValue().get("TOKEN_KEY_" + IpUtil.getIpFromRequest(WebUtils.toHttp(request)).toUpperCase());
		System.out.println(IpUtil.getIpFromRequest(WebUtils.toHttp(request)));
		return null;
	}

	@ApiOperation(value = "登陆")
	@PostMapping("/login")
	public ApiResult login() {
		return null;
	}
}