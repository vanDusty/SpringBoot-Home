package cn.van.code.img.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: ImgValidateCodeUtil
 *
 * @author: Van
 * Date:     2019-09-15 13:55
 * Description: 图片验证码工具类
 * Version： V1.0
 */
public class ImgValidateCodeUtil {

    private static Random random = new Random();
    /**
     * 验证码的宽
     */
    private static int width = 160;
    /**
     * 验证码的高
     */
    private static int height = 40;
    /**
     * 验证码的干扰线数量
     */
    private static int lineSize = 30;
    /**
     * 验证码词典
     */
    private static String randomString = "0123456789abcdefghijklmnopqrstuvwxyz";

    /**
     * 获取字体
     * @return
     */
    private static Font getFont() {
        return new Font("Times New Roman", Font.ROMAN_BASELINE, 40);
    }

    /**
     * 获取颜色
     * @param fc
     * @param bc
     * @return
     */
    private static Color getRandomColor(int fc, int bc) {

        fc = Math.min(fc, 255);
        bc = Math.min(bc, 255);

        int r = fc + random.nextInt(bc - fc - 16);
        int g = fc + random.nextInt(bc - fc - 14);
        int b = fc + random.nextInt(bc - fc - 12);

        return new Color(r, g, b);
    }

    /**
     * 绘制干扰线
     * @param g
     */
    private static void drawLine(Graphics g) {
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        int xl = random.nextInt(20);
        int yl = random.nextInt(10);
        g.drawLine(x, y, x + xl, y + yl);
    }

    /**
     * 获取随机字符
     * @param num
     * @return
     */
    private static String getRandomString(int num) {
        num = num > 0 ? num : randomString.length();
        return String.valueOf(randomString.charAt(random.nextInt(num)));
    }

    /**
     * 绘制字符串
     * @param g
     * @param randomStr
     * @param i
     * @return
     */
    private static String drawString(Graphics g, String randomStr, int i) {
        g.setFont(getFont());
        g.setColor(getRandomColor(108, 190));
        String rand = getRandomString(random.nextInt(randomString.length()));
        randomStr += rand;
        g.translate(random.nextInt(3), random.nextInt(6));
        g.drawString(rand, 40 * i + 10, 25);
        return randomStr;
    }

    /**
     * 生成随机图片，返回 base64 字符串
     * @param
     * @return
     */
    public static Map<String, String> getImgCodeBaseCode(int length) {
        Map<String, String> result = new HashMap<>();
        // BufferedImage类是具有缓冲区的Image类,Image类是用于描述图像信息的类
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();
        g.fillRect(0, 0, width, height);
        // 获取颜色
        g.setColor(getRandomColor(105, 189));
        // 获取字体
        g.setFont(getFont());
        // 绘制干扰线
        for (int i = 0; i < lineSize; i++) {
            drawLine(g);
        }

        // 绘制随机字符
        String randomCode = "";
        for (int i = 0; i < length; i++) {
            randomCode = drawString(g, randomCode, i);
        }
        System.out.println("验证码是：" + randomCode);
        g.dispose();

        result.put("imgCode", randomCode);

        String base64Code = "";
        try {
            //返回 base64
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", bos);

            byte[] bytes = bos.toByteArray();
            Base64.Encoder encoder = Base64.getEncoder();
            base64Code = encoder.encodeToString(bytes);

        } catch (Exception e) {
            e.printStackTrace();
        }
        result.put("data", "data:image/png;base64," + base64Code);
        return result;
    }

}
