package io.lance.web.module.common.file.service;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.InitiateMultipartUploadRequest;
import com.aliyun.oss.model.InitiateMultipartUploadResult;
import com.aliyun.oss.model.UploadPartRequest;
import com.aliyun.oss.model.UploadPartResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * Author Lance.
 * Date: 2017-09-07 11:06
 * Desc: oss文件上传
 */
@Service
public class OssFileService {

    private static final Logger logger = LogManager.getLogger(OssFileService.class);

    static final String BUCKET_NAME = "fosun-upload-local";

    private static OSSClient client = null;

    static {

    }


    /**
     * 获得uploadId
     */
    public String getUploadId(OSSClient client, String bucketName, String key) {
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, key);
        InitiateMultipartUploadResult result = client.initiateMultipartUpload(request);
        return result.getUploadId();
    }


    public static void main(String[] args) {
        OssFileService fileService=new OssFileService();
        String key= UUID.randomUUID().toString();
        System.out.println("key--->"+key);
        String uploadId=fileService.getUploadId(client,BUCKET_NAME,key);


    }

    private static class PartUploader implements Runnable {
        private File localFile;
        private long startPos;
        private long partSize;
        private int partNumber;
        private String uploadId;
        private String key;

        public PartUploader(File localFile, long startPos, long partSize, int partNumber, String uploadId, String key) {
            this.localFile = localFile;
            this.startPos = startPos;
            this.partSize = partSize;
            this.partNumber = partNumber;
            this.uploadId = uploadId;
            this.key = key;
        }

        @Override
        public void run() {
            InputStream instream = null;
            try {
                instream = new FileInputStream(this.localFile);
                instream.skip(this.startPos);

                UploadPartRequest uploadPartRequest = new UploadPartRequest();
                uploadPartRequest.setBucketName(BUCKET_NAME);
                uploadPartRequest.setKey(key);
                uploadPartRequest.setUploadId(this.uploadId);
                uploadPartRequest.setInputStream(instream);
                uploadPartRequest.setPartSize(this.partSize);
                uploadPartRequest.setPartNumber(this.partNumber);
                //上传部分文件
                UploadPartResult uploadPartResult = client.uploadPart(uploadPartRequest);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (instream != null) {
                    try {
                        instream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }



}
