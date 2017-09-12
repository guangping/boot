package io.lance.web.module.common.file.service;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Author Lance.
 * Date: 2017-09-07 11:06
 * Desc: oss文件上传
 */
@Service
public class OssFileService {

    private static final Logger logger = LogManager.getLogger(OssFileService.class);

    static final String BUCKET_NAME = "";
    static final String END_POINT = "oss-cn-hangzhou.aliyuncs.com";

    private static OSSClient client = null;

    //记录上传块tag
    protected static List<PartETag> partETags = Collections.synchronizedList(Lists.newArrayList());
    //记录token与uploadId关系
    protected static Map<String, String> uploadIdMaps = Maps.newHashMap();
    //记录uploadId与partNumber是否已上传
    protected static Map<String, String> uploadParts = Maps.newHashMap();


    static {
        client = new OSSClient(END_POINT, "", "");
    }


    /**
     * 初始化分块上传事件并生成uploadID，用来作为区分分块上传事件的唯一标识
     */
    private String getUploadId(String key) {
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(BUCKET_NAME, key);
        InitiateMultipartUploadResult result = client.initiateMultipartUpload(request);

        return result.getUploadId();
    }

    public String getUploadId(String token, String key) {
        String value = uploadIdMaps.get(token);
        if (StringUtils.isNotBlank(value)) {
            return value;
        }
        String uploadId = this.getUploadId(key);
        uploadIdMaps.put(token, uploadId);
        return uploadId;
    }

    /**
     * 将文件分块进行升序排序并执行文件上传。
     *
     * @param uploadId
     */
    public void completeMultipartUpload(String uploadId, String key, String token) {
        String tokenValue = uploadIdMaps.get(token);

        if (partETags.isEmpty() || StringUtils.isBlank(tokenValue) || !tokenValue.equals(uploadId)) {
            return;
        }
        // 将文件分块按照升序排序
        partETags.sort(new Comparator<PartETag>() {
            @Override
            public int compare(PartETag o1, PartETag o2) {
                return o1.getPartNumber() - o2.getPartNumber();
            }
        });

        CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(BUCKET_NAME,
                key, uploadId, partETags);
        // 完成分块上传
        client.completeMultipartUpload(completeMultipartUploadRequest);
        partETags.clear();
    }


    /*private static class PartUploader implements Runnable {
        private File localFile;
        private long startPos;
        private long partSize;
        private int partNumber;
        private String uploadId;
        private String fileName;

        */

    /**
     * @param localFile  要上传的文件
     * @param startPos   每个文件块的开始
     * @param partSize   块大小
     * @param partNumber 第几块
     * @param uploadId   作为块的标识
     * @param fileName   上传后的文件名称
     * @desc:
     * @author lance
     * @time: 2017-09-08 13:50:55
     *//*
        public PartUploader(File localFile, long startPos, long partSize, int partNumber, String uploadId, String fileName) {
            this.localFile = localFile;
            this.startPos = startPos;
            this.partSize = partSize;
            this.partNumber = partNumber;
            this.uploadId = uploadId;
            this.fileName = fileName;
        }

        @Override
        public void run() {
            InputStream instream = null;
            try {
                //获取输入流
                instream = new FileInputStream(this.localFile);
                instream.skip(this.startPos);//跳到每个分块的开头

                //创建UploadPartRequest，上传分块
                UploadPartRequest uploadPartRequest = new UploadPartRequest();
                uploadPartRequest.setBucketName(BUCKET_NAME);
                uploadPartRequest.setKey(fileName);
                uploadPartRequest.setUploadId(this.uploadId);
                uploadPartRequest.setInputStream(instream);
                uploadPartRequest.setPartSize(this.partSize);
                uploadPartRequest.setPartNumber(this.partNumber);
                //上传部分文件
                UploadPartResult uploadPartResult = client.uploadPart(uploadPartRequest);
                synchronized (partETags) {
                    // 将返回的PartETag保存到List中。
                    partETags.add(uploadPartResult.getPartETag());
                }
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
    }*/

   /* public static void main(String[] args) {
        // 创建一个可重用固定线程数的线程池。若同一时间线程数大于10，则多余线程会放入队列中依次执行
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        final long partSize = Constant.SIZE;

        File file = new File("E:\\lance\\Git-2.14.1-64-bit.zip");

        // 计算分块数目
        long fileLength = file.length();
        int partCount = (int) (fileLength / partSize);
        if (fileLength % partSize != 0) {
            partCount++;
        }
        System.out.println("分片数目:" + partCount);

        OssFileService fileService = new OssFileService();
        String key = "lance/1/2/Git-2.14.1-64-bit.zip";

        String uploadId = fileService.getUploadId(key);

        long start = System.currentTimeMillis();
        for (int i = 0; i < partCount; i++) {
            long startPos = i * partSize;
            long curPartSize = (i + 1 == partCount) ? (fileLength - startPos) : partSize;
            executorService.execute(new PartUploader(file, startPos, curPartSize, i + 1, uploadId, key));
        }

        executorService.shutdown();

        // 如果关闭后所有任务都已完成，则返回 true。
        while (!executorService.isTerminated()) {
            try {
                // 用于等待子线程结束，再继续执行下面的代码
                executorService.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        fileService.completeMultipartUpload(uploadId, key, "");
        long end = System.currentTimeMillis();
        System.out.println("耗时:" + (end - start) + "ms");

        String url = END_POINT + "/" + BUCKET_NAME + "/" + client.getObject(BUCKET_NAME, key).getKey();

        System.out.println(url);
    }*/
    public void multipartUpload(InputStream inputStream, String fileName, String uploadId, long partSize, int partNumber) {
        logger.info("文件名称:{},uploadId:{},partNumber:{}", fileName, uploadId, partNumber);
        //验证uploadId partNumner是否已上传过
        //创建UploadPartRequest，上传分块
        String checkey = uploadId + "_" + partNumber;
        boolean check = Boolean.valueOf(uploadParts.get(checkey));
        if (check) {
            return;
        }

        UploadPartRequest uploadPartRequest = new UploadPartRequest();
        uploadPartRequest.setBucketName(BUCKET_NAME);
        uploadPartRequest.setKey(fileName);
        uploadPartRequest.setUploadId(uploadId);
        uploadPartRequest.setInputStream(inputStream);
        uploadPartRequest.setPartSize(partSize);
        uploadPartRequest.setPartNumber(partNumber);
        //上传部分文件
        UploadPartResult uploadPartResult = client.uploadPart(uploadPartRequest);
        synchronized (partETags) {
            // 将返回的PartETag保存到List中。
            partETags.add(uploadPartResult.getPartETag());
        }
        uploadParts.put(checkey, String.valueOf(true));
    }

}
