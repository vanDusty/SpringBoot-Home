package cn.van.qiniu.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: UploadService
 *
 * @author: Van
 * Date:     2019-06-25 16:57
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
public interface UploadService {
    /**
     * 上传图片到七牛云
     * @param image
     * @return
     */
    String uploadImg(MultipartFile image);

}
