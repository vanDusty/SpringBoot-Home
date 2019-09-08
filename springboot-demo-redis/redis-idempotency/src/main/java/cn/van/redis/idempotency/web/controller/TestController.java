package cn.van.redis.idempotency.web.controller;

import cn.van.redis.idempotency.annotation.ApiIdempotent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    @ApiIdempotent
    @PostMapping("testIdempotence")
    public String testIdempotence() {
        return "success";
    }

}
