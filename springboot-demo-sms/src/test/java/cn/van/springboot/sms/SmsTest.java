/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: SmsTest
 * Author:   zhangfan
 * Date:     2019-06-12 16:19
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.springboot.sms;

import cn.van.springboot.sms.utils.AliYunSmsUtils;
import cn.van.springboot.sms.web.param.SmsParam;
import cn.van.springboot.sms.utils.TxCloudSmsUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Random;

/**
 * 〈测试发送验证码〉<br>
 * 〈〉
 *
 * @author zhangfan
 * @create 2019-06-12
 * @since 1.0.0
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class SmsTest {

    @Resource
    private TxCloudSmsUtil txCloudSmsUtil;
    @Resource
    private AliYunSmsUtils aliYunSmsUtils;

    @Test
    public void txCloudSmsUtil() {
        // 生成随机的六位数验证码
        Random random = new Random(4);
        Integer verifyCode = random.nextInt(1000000);
        SmsParam smsParams = new SmsParam("17098705205",verifyCode.toString());
        System.out.println("生成的验证码为：" + verifyCode);
        String str = txCloudSmsUtil.sendSms(smsParams);
        System.out.println("发送结果：" + str);
    }

    @Test
    public void aliYunSmsUtils() {
        // 生成随机的六位数验证码
        Random random = new Random(4);
        Integer verifyCode = random.nextInt(1000000);
        SmsParam smsParams = new SmsParam("17098705205",verifyCode.toString());
        System.out.println("生成的验证码为：" + verifyCode);
        String str = aliYunSmsUtils.sendSms(smsParams);
        System.out.println("发送结果：" + str);
    }

}