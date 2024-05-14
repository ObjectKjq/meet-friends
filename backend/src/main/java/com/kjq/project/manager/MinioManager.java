package com.kjq.project.manager;

import cn.hutool.core.io.FileTypeUtil;
import com.kjq.project.common.ErrorCode;
import com.kjq.project.common.FileTypeEnum;
import com.kjq.project.config.MinioClientConfig;
import com.kjq.project.exception.BusinessException;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.UUID;

@Service
public class MinioManager {

    @Resource
    private MinioClient minioClient;

    @Resource
    private MinioClientConfig minioClientConfig;

    public String upload(MultipartFile file) {
        try {
            // 上传文件流。
            InputStream inputStream = file.getInputStream();
            // 校验文件上传的后缀
            String type = FileTypeUtil.getType(inputStream);
            inputStream.close();
            boolean isType = false;
            for (FileTypeEnum fileTypeEnum : FileTypeEnum.values()) {
                if (type.equals(fileTypeEnum.getType())){
                    isType = true;
                    break;
                }
            }
            if (!isType){
                throw new BusinessException(ErrorCode.FILE_TYPE_ERROR);
            }
            //生成随机唯一值，使用uuid，添加到文件名称里面
            String fileName = file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            fileName = uuid+fileName;
            //按照当前日期，创建文件夹，上传到创建文件夹里面
            //2021/02/02/01.jpg
            String pathFileName = null;
            String timeUrl = new DateTime().toString("yyyy/MM/dd");
            pathFileName = timeUrl+"/"+fileName;

            //调用方法实现上传
            InputStream inputStreamFile = file.getInputStream();
            // 1.jpg    /a/b/1.jpg
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioClientConfig.getBucketName())
                    .object(pathFileName)
                    .stream(inputStreamFile, file.getSize(), -1)
                    .build());
            inputStreamFile.close();
            //获取文件上传之后文件路径
            String url = minioClientConfig.getEndpoint()+"/"+minioClientConfig.getBucketName()+"/"+pathFileName;
            //返回
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
