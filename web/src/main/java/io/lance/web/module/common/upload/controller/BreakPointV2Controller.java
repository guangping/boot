package io.lance.web.module.common.upload.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import io.lance.web.module.common.file.service.OssFileService;
import io.lance.web.module.common.upload.bean.Range;
import io.lance.web.module.common.upload.util.Constant;
import io.lance.web.module.common.upload.util.IoUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Author Lance.
 * Date: 2017-09-11 15:00
 * Desc:
 */
@RestController
public class BreakPointV2Controller {

    private static final Logger logger = LogManager.getLogger(BreakPointV2Controller.class);

    private static final Map<String, Long> sizeMap = Maps.newHashMap();

    @Autowired
    private OssFileService fileService;

    @RequestMapping(value = "/v2/upload", method = RequestMethod.GET)
    public JSONObject upload(String token, String size, String name) {
        JSONObject object = new JSONObject();
        boolean success = true;
        String message = "";

        long start = 0;
        Long value = sizeMap.get(token);
        if (null != value) {
            start = value;
        }
        object.put(Constant.START_FIELD, start);
        object.put(Constant.SUCCESS, success);
        object.put(Constant.MESSAGE, message);
        return object;
    }

    /**
     * @param name      文件名称
     * @param token     token
     * @param totalSize 文件大小
     * @desc:
     * @author lance
     * @time: 2017-09-11 15:21:29
     */
    @RequestMapping(value = "/v2/upload", method = RequestMethod.POST)
    public JSONObject upload(HttpServletRequest request, @RequestParam String token, @RequestParam String name, @RequestParam(name = "size") Long totalSize) {
        JSONObject object = new JSONObject();
        boolean success = false;

        InputStream content = null;
        long start = 0;
        Range range = null;
        String uploadId = this.fileService.getUploadId(token, name);
        try {
            range = IoUtil.parseRange(request);
            content = request.getInputStream();

            start = range.getTo();
            //上传到阿里云
            long partSize = Constant.SIZE;
            int partNum = (int) (start / partSize);
            if ((start % partSize) != 0) {//
                partSize = totalSize - start;
                partNum = partNum + 1;
            }
            if(partNum==9){
                System.out.println("start:"+start+"");
            }
            this.fileService.multipartUpload(content, name, uploadId, partSize, partNum);
            sizeMap.put(token, start);
            success = true;
        } catch (IOException e) {
            success = false;
            e.printStackTrace();
        } finally {
            IoUtil.close(content);
            try {
                //TODO 更改文件名称
                if (range.getSize() == start) {
                    /** 是否删除文件 此处可上传文件至阿里云并删除本地文件*/
                    this.fileService.completeMultipartUpload(uploadId, name, token);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            object.put(Constant.START_FIELD, start);
            object.put(Constant.SUCCESS, success);
            return object;
        }
    }


}
