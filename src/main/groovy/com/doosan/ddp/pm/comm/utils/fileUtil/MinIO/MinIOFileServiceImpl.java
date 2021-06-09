package com.doosan.ddp.pm.comm.utils.fileUtil.MinIO;

import com.doosan.ddp.pm.comm.utils.fileUtil.FileService;
import io.minio.*;
import io.minio.errors.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class MinIOFileServiceImpl implements FileService {
    private MinioClient minioClient;

    @Autowired
    private MinIOConfig minIOConfig;

    @PostConstruct
    private void init(){
        minioClient=MinioClient.builder()
                .endpoint(minIOConfig.getEndPoint())
                .credentials(minIOConfig.getAccessKey(),minIOConfig.getSecretKey())
                .build();
    }


    @Override
    public String uploadFileWithRandomName(MultipartFile file, String secondDirectory){
        String filePath="";
        String fileName=secondDirectory+"/"+UUID.randomUUID().toString().replace("-","")+"."+getFileExtention(file.getOriginalFilename());
        try {
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(getBucket(minIOConfig.getBucket()))
                            .object(fileName)
                            .stream(file.getInputStream(),-1,10485760)
                            .contentType(file.getContentType())
                            .build());
            filePath=minIOConfig.getBucket()+"/"+fileName;
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (XmlParserException e) {
            e.printStackTrace();
        }
        return filePath;
    }

    @Override
    public String uploadFileWithOriginalName(MultipartFile file) {
        return null;
    }

    @Override
    public InputStream downloadFile(String filePath) {
        Map<String,String> bucketAndObject=splitFilePath(filePath);
        InputStream stream=null;
        if(bucketAndObject!=null&&bucketAndObject.size()==2){
            try {
                stream=minioClient.getObject(GetObjectArgs.builder().bucket(bucketAndObject.get("bucket")).object(bucketAndObject.get("objectName")).build());
            } catch (ErrorResponseException e) {
                e.printStackTrace();
            } catch (InsufficientDataException e) {
                e.printStackTrace();
            } catch (InternalException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (InvalidResponseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (ServerException e) {
                e.printStackTrace();
            } catch (XmlParserException e) {
                e.printStackTrace();
            }
        }
        return stream;
    }

    @Override
    public void deleteFiles(List<String> filePaths) {
        filePaths.forEach(a->{removeFile(a);});
    }

    private void removeFile(String filePath){
        Map<String,String> bucketAndObject=splitFilePath(filePath);
        if(bucketAndObject!=null&&bucketAndObject.size()==2){
            try {
                minioClient.removeObject(
                        RemoveObjectArgs.builder().bucket(bucketAndObject.get("bucket")).object(bucketAndObject.get("objectName")).build()
                );
            } catch (ErrorResponseException e) {
                e.printStackTrace();
            } catch (InsufficientDataException e) {
                e.printStackTrace();
            } catch (InternalException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (InvalidResponseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (ServerException e) {
                e.printStackTrace();
            } catch (XmlParserException e) {
                e.printStackTrace();
            }
        }
    }

    private Map<String,String> splitFilePath(String filePath){
        Map<String,String> resultMap=new HashMap();
        if(StringUtils.isNotEmpty(filePath)){
            String bucket="";
            String objectName="";
            int index=filePath.indexOf("/");
            if(index>0&&index<filePath.length()-1){
                bucket=filePath.substring(0,index);
                objectName=filePath.substring(index);
            }
            if(StringUtils.isNotEmpty(bucket)&&StringUtils.isNotEmpty(objectName)){
                resultMap.put("bucket",bucket);
                resultMap.put("objectName",objectName);
            }
        }
        return resultMap;
    }

    /**
     *@description 获取文件扩展名
     *@param fileName 完整文件名
     *@author weijian.lin
     */
    private String getFileExtention(String fileName){
        int index=fileName.lastIndexOf(".");
        if(index>0&&index<fileName.length()-1){
            return fileName.substring(index+1);
        }
        return null;
    }

    /**
     *@description bucket检查，没有的话先创建
     *@author weijian.lin
     */
    private String getBucket(String bucketName){
        try {
            boolean isBucketExists=minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if(!isBucketExists){
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (XmlParserException e) {
            e.printStackTrace();
        }
        return bucketName;
    }

}
