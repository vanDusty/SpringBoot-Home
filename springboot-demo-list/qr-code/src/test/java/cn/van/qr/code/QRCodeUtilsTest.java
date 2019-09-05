package cn.van.qr.code;

import cn.van.qr.code.utils.QRCodeUtils;
import cn.van.qr.code.utils.QrCodeUtil;
import org.junit.Test;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: QRCodeUtilsTest
 *
 * @author: Van
 * Date:     2019-09-05 11:02
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
public class QRCodeUtilsTest {


    @Test
    public void test22() throws Exception {

        String content = "https://www.dustyblog.cn";
        //不含Logo Thread.currentThread().getContextClassLoader().getResource("").getPath()
        //                     + "templates" + File.separator + "dusty_blog.png";
        // QRCodeUtils.encode(text, null, "../", "qrcode", true);
        // 相对路径
        // QrCodeUtil.encode(content, null, "springboot-demo-list/qr-code/src/main/resources", true);


    }
}
