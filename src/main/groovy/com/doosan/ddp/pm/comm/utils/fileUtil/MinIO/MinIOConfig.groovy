package com.doosan.ddp.pm.comm.utils.fileUtil.MinIO

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("minio")
class MinIOConfig {
    String endPoint;
    String accessKey;
    String secretKey;
    String bucket;
}