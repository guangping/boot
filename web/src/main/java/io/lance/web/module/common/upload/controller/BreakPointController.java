package io.lance.web.module.common.upload.controller;

import com.alibaba.fastjson.JSONObject;
import io.lance.web.module.common.upload.bean.Range;
import io.lance.web.module.common.upload.config.Configurations;
import io.lance.web.module.common.upload.exception.StreamException;
import io.lance.web.module.common.upload.util.Constant;
import io.lance.web.module.common.upload.util.IoUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;

/**
 * Author Lance.
 * Date: 2017-09-06 15:30
 * Desc: 文件上传
 */
@RestController
public class BreakPointController {

    private static final Logger logger = LogManager.getLogger(BreakPointController.class);

    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public JSONObject upload(String token, String size, String name) {
        JSONObject object = new JSONObject();
        boolean success = true;
        String message = "";

        long start = 0;

        //获取已记录的文件大小
        try {
            File f = IoUtil.getTokenedFile(token);
            start = f.length();
            //TODO 文件上传完成后
            if (token.endsWith("_0") && "0".equals(size) && 0 == start) {
                logger.info("end");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        object.put(Constant.START_FIELD, start);
        object.put(Constant.SUCCESS, success);
        object.put(Constant.MESSAGE, message);
        return object;
    }

    /**
     * @param name  文件名称
     * @param token token
     * @desc: 测试文件上传
     * @author lance
     * @time: 2017-09-06 15:36:10
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public JSONObject upload(HttpServletRequest request, String token, String name) {
        JSONObject object = new JSONObject();
        boolean success = false;

        OutputStream out = null;
        InputStream content = null;
        long start = 0;
        Range range = null;
        File f = null;
        try {
            range = IoUtil.parseRange(request);
            f = IoUtil.getTokenedFile(token);
            if (f.length() != range.getFrom()) {
                /** drop this uploaded data */
                throw new StreamException(StreamException.ERROR_FILE_RANGE_START);
            }

            out = new FileOutputStream(f, true);
            content = request.getInputStream();
            int read = 0;
            final byte[] bytes = new byte[Constant.BUFFER_LENGTH];
            while ((read = content.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            start = f.length();
            success = true;
        } catch (IOException e) {
            success = false;
            e.printStackTrace();
        } finally {
            IoUtil.close(out);
            IoUtil.close(content);
            try {
                //TODO 更改文件名称
                if (range.getSize() == start) {
                    // 先删除
                    IoUtil.getFile(name).delete();
                    Files.move(f.toPath(), f.toPath().resolveSibling(name));
                    logger.info("name:{},token:{}", name, token);
                    /** 是否删除文件 此处可上传文件至阿里云并删除本地文件*/
                    if (Configurations.isDeleteFinished()) {
                        IoUtil.getFile(name).delete();
                    }
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
