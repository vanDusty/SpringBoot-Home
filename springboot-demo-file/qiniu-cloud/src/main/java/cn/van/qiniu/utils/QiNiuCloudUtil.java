package cn.van.qiniu.utils;

import cn.van.qiniu.config.QiNiuCloudConfiguration;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.Base64;
import com.qiniu.util.StringMap;
import com.qiniu.util.UrlSafeBase64;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: QiNiuCloudUtil
 *
 * @author: Van
 * Date:     2019-06-25 14:52
 * Description: 七牛上传工具类
 * Version： V1.0
 */
public class QiNiuCloudUtil {
    /**
     * 设置账号的AK和SK
     */
    private static final String ACCESS_KEY = QiNiuCloudConfiguration.accessKey;
    private static final String SECRET_KEY = QiNiuCloudConfiguration.secretKEY;
    /**
     * 要上传的空间
     */
    private static final String bucketName = QiNiuCloudConfiguration.bucketName;
    /**
     * 上传空间域名
     */
    private static final String bucketNameUrl = QiNiuCloudConfiguration.bucketNameUrl;
    /**
     * 密钥
     */
    private static final Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    /**
     * 自定义的图片样式
     */
    private static final String imgStyle = QiNiuCloudConfiguration.imgStyle;

    public static String getUpToken() {
        //生成凭证
        return auth.uploadToken(bucketName, null, 3600, new StringMap().put("insertOnly", 1));
    }
    /**
     *  base64方式上传
     * @param base64 ：图片编码
     * @param fileName：图片名称
     * @return
     * @throws Exception
     */
    public static String uploadPicByBase64(byte[] base64, String fileName) throws Exception{
        String file64 = Base64.encodeToString(base64, 0);
        Integer length = base64.length;
        String dateStr = LocalDate.now().toString();
        // 将当前日期拼接在文件名称中
        fileName = dateStr + "/" + fileName;
        String url = "http://upload.qiniu.com/putb64/" + length + "/key/"+ UrlSafeBase64.encodeToString(fileName);
        //非华东空间需要根据注意事项 : 修改上传域名
        RequestBody requestBody = RequestBody.create(null, file64);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/octet-stream")
                .addHeader("Authorization", "UpToken " + getUpToken())
                .post(requestBody).build();
        OkHttpClient client = new OkHttpClient();
        okhttp3.Response response = client.newCall(request).execute();
        // 上传成功返回图片地址
        return response.isSuccessful()?("http://" + bucketNameUrl + "/" + fileName):"Up Img Failed";
    }
}
