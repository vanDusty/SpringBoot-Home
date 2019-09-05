package cn.van.qr.code.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Hashtable;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: QRCodeUtils
 *
 * @author: Van
 * Date:     2019-03-05 11:43
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
public class QRCodeUtils {
    // //编码格式,采用utf-8
    // private static final String UNICODE = "utf-8";
    // //图片格式
    // private static final String FORMAT = "JPG";
    // //二维码宽度,单位：像素pixels
    // private static final int QRCODE_WIDTH = 300;
    // //二维码高度,单位：像素pixels
    // private static final int QRCODE_HEIGHT = 300;
    // //LOGO宽度,单位：像素pixels
    // private static final int LOGO_WIDTH = 100;
    // //LOGO高度,单位：像素pixels
    // private static final int LOGO_HEIGHT = 100;
    //
    // /**
    //  * 生成二维码图片
    //  *
    //  * @param content      二维码内容
    //  * @param logoPath     中间log地址
    //  * @param needCompress 是否压缩
    //  * @return
    //  * @throws Exception
    //  */
    // private static BufferedImage createImage(String content, String logoPath, boolean needCompress) throws Exception {
    //     Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
    //     hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
    //     hints.put(EncodeHintType.CHARACTER_SET, UNICODE);
    //     hints.put(EncodeHintType.MARGIN, 1);
    //     BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_WIDTH, QRCODE_HEIGHT,
    //             hints);
    //     int width = bitMatrix.getWidth();
    //     int height = bitMatrix.getHeight();
    //     BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    //     for (int x = 0; x < width; x++) {
    //         for (int y = 0; y < height; y++) {
    //             image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
    //         }
    //     }
    //     if (logoPath == null || "".equals(logoPath)) {
    //         return image;
    //     }
    //     // 插入图片
    //     QRCodeUtils.insertImage(image, logoPath, needCompress);
    //     return image;
    // }
    //
    // /**
    //  * 插入LOGO
    //  *
    //  * @param source       二维码图片
    //  * @param logoPath     LOGO图片地址
    //  * @param needCompress 是否压缩
    //  * @throws Exception
    //  */
    // private static void insertImage(BufferedImage source, String logoPath, boolean needCompress) throws Exception {
    //     File file = new File(logoPath);
    //     if (!file.exists()) {
    //         throw new Exception("logo file not found.");
    //     }
    //     Image src = ImageIO.read(new File(logoPath));
    //     int width = src.getWidth(null);
    //     int height = src.getHeight(null);
    //     if (needCompress) { // 压缩LOGO
    //         if (width > LOGO_WIDTH) {
    //             width = LOGO_WIDTH;
    //         }
    //         if (height > LOGO_HEIGHT) {
    //             height = LOGO_HEIGHT;
    //         }
    //         Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    //         BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    //         Graphics g = tag.getGraphics();
    //         g.drawImage(image, 0, 0, null); // 绘制缩小后的图
    //         g.dispose();
    //         src = image;
    //     }
    //     // 插入LOGO
    //     Graphics2D graph = source.createGraphics();
    //     int x = (QRCODE_WIDTH - width) / 2;
    //     int y = (QRCODE_HEIGHT - height) / 2;
    //     graph.drawImage(src, x, y, width, height, null);
    //     Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
    //     graph.setStroke(new BasicStroke(3f));
    //     graph.draw(shape);
    //     graph.dispose();
    // }
    //
    // /**
    //  * 生成二维码(内嵌LOGO)
    //  * 调用者指定二维码文件名
    //  *
    //  * @param content      二维码的内容
    //  * @param logoPath     中间log地址
    //  * @param destPath     生成文件的存储路径
    //  * @param fileName     生成文件的名称
    //  * @param needCompress 是否压缩
    //  * @return
    //  * @throws Exception
    //  */
    // public static String encode(String content, String logoPath, String destPath, String fileName, boolean needCompress) throws Exception {
    //     BufferedImage image = QRCodeUtils.createImage(content, logoPath, needCompress);
    //     mkdirs(destPath);
    //     //文件名称通过传递
    //     fileName = fileName.substring(0, fileName.indexOf(".") > 0 ? fileName.indexOf(".") : fileName.length())
    //             + "." + FORMAT.toLowerCase();
    //     ImageIO.write(image, FORMAT, new File(destPath + "/" + fileName));
    //     return fileName;
    // }
    //
    // /**
    //  * 创建文件夹， mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
    //  *
    //  * @param destPath
    //  */
    // public static void mkdirs(String destPath) {
    //     File file = new File(destPath);
    //     if (!file.exists() && !file.isDirectory()) {
    //         file.mkdirs();
    //     }
    // }
    //
    // /**
    //  * 解析二维码
    //  *
    //  * @param path 二维码图片路径
    //  * @return String 二维码内容
    //  * @throws Exception
    //  */
    // public static String decode(String path) throws Exception {
    //     File file = new File(path);
    //     BufferedImage image = ImageIO.read(file);
    //     if (image == null) {
    //         return null;
    //     }
    //     BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
    //     BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
    //     Result result;
    //     Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
    //     hints.put(DecodeHintType.CHARACTER_SET, UNICODE);
    //     result = new MultiFormatReader().decode(bitmap, hints);
    //     return result.getText();
    // }
    //
    // /**
    //  * 测试
    //  *
    //  * @param args
    //  * @throws Exception
    //  */
    public static void main(String[] args) throws Exception {

        // String content = "https://www.dustyblog.cn";
        // //不含Logo Thread.currentThread().getContextClassLoader().getResource("").getPath()
        // //                     + "templates" + File.separator + "dusty_blog.png";
        // // QRCodeUtils.encode(text, null, "../", "qrcode", true);
        // // 相对路径
        // String fileName = QRCodeUtils.encode(content, null, "springboot-demo-list/qr-code/src/main/resources", "qrcode66", true);
        // System.out.println(fileName);
        // //含Logo，指定二维码图片名
        // // QRCodeUtils.encode(text, "./logo.jpg", "./", "qrcode1", true);
        // // System.out.println(QRCodeUtils.decode("d:\\cc\\qrcode1.jpg"));
    }
}
