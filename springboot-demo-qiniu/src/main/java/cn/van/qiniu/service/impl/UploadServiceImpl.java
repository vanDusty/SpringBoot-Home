package cn.van.qiniu.service.impl;

import cn.van.qiniu.config.QiNiuCloudConfiguration;
import cn.van.qiniu.service.UploadService;
import cn.van.qiniu.utils.QiNiuCloudUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: UploadServiceImpl
 *
 * @author: Van
 * Date:     2019-06-25 16:57
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
@Service
public class UploadServiceImpl implements UploadService {

    /**
     * 限制上传图片的大小
     */
    private static Double Mb = QiNiuCloudConfiguration.MB;
    private static Double maxSize = QiNiuCloudConfiguration.maxSize;

    @Override
    public String uploadImg(MultipartFile image) {
        String result = "";
        if(!image.isEmpty()) {
            if (!checkSize(image)) {
                //超出限制大小
                return "OUTOFSIZE";
            }
            String widthAndHeight = this.getWidthAndHeight(image);
            if (("ERROR").equals(widthAndHeight)) {
                return "ERROR";
            }
            try {
                byte[] bytes = image.getBytes();
                // 用随机字符串当做图片名存储
                String imageName = UUID.randomUUID().toString();
                QiNiuCloudUtil qiNiuCloudUtil = new QiNiuCloudUtil();
                try {
                    //使用base64方式上传到七牛云
                    result = qiNiuCloudUtil.uploadPicByBase64(bytes, imageName);
                    return result;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            } catch (IOException e) {
                //上传失败
                result = "UPLOADFAILED！";
                return result;
            }
        }
        // 图片为空
        return "EMPTY！";
    }
    /**
     * 校验大小，上传图片不能大于指定大小
     * @param file
     * @return
     */
    public static boolean checkSize(MultipartFile file){
        //校验大小
        double ratio = file.getSize() / Mb;
        if(ratio > maxSize){
            return false;
        }else{
            return true;
        }
    }
    /**
     * 获取图片宽高拼接字符串：?width=100&height=200
     * @return
     */
    private String getWidthAndHeight(MultipartFile file){
        try {
            InputStream inputStream = file.getInputStream();
            BufferedImage image = ImageIO.read(inputStream);
            if (null == image){
                return "ERROR";
            }
            int width = image.getWidth();
            int height = image.getHeight();
            return "?width="+width+"&height="+height;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
