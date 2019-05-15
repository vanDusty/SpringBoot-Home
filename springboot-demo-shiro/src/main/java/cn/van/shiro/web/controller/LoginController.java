package cn.van.shiro.web.controller;


import cn.van.shiro.domain.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Api(value = "登陆相关接口", tags = "账户相关")
@RestController
@RequestMapping("/account")
public class LoginController {

	@ApiOperation(value = "注册")
	@PostMapping("/register")
	public String register(@RequestBody SysUser sysUser) {
		System.out.println(sysUser.toString());
		return "index";
	}
}