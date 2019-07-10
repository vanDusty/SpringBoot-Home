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

import cn.van.tx.sms.domain.SmsCode;
import cn.van.tx.sms.utils.TxCloudSmsUtil;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Random;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author zhangfan
 * @create 2019-06-12
 * @since 1.0.0
 */

// 参考文章 https://mp.weixin.qq.com/s/kWR-awfOutRZNAmpxvNzQA
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
        SmsCode smsCode = new SmsCode("17098705205",verifyCode.toString());
        System.out.println("生成的验证码为：" + verifyCode);
        // smsCode.setPhone("17098705205").setVerifyCode();
        String str = txCloudSmsUtil.sendSms(smsCode);
        System.out.println(str);
    }

}