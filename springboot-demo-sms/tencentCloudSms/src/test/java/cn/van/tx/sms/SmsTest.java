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
package cn.van.tx.sms;

import cn.van.tx.sms.domain.SmsParams;
import cn.van.tx.sms.utils.TxCloudSmsUtil;
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
    TxCloudSmsUtil txCloudSmsUtil;
    @Test
    public void testSms() {
        // 生成随机的六位数验证码
        Random random = new Random(4);
        Integer verifyCode = random.nextInt(1000000);
        SmsParams smsParams = new SmsParams("17098705205",verifyCode.toString());
        System.out.println("生成的验证码为：" + verifyCode);
        String str = txCloudSmsUtil.sendSms(smsParams);
        System.out.println("发送结果：" + str);
    }

}