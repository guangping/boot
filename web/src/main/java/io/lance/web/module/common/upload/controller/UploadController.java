package io.lance.web.module.common.upload.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Author Lance.
 * Date: 2017-09-06 15:30
 * Desc: 文件上传
 */
@RestController
public class UploadController {

    private static final Logger logger = LogManager.getLogger(UploadController.class);

    /**
     * @desc: 测试文件上传
     * @author lance
     * @time: 2017-09-06 15:36:10
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public JSONObject upload(@RequestParam(value = "Filedata", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        JSONObject object = new JSONObject();
        object.put("success", "1");

        return object;
    }


}
