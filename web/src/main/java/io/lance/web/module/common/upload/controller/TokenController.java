package io.lance.web.module.common.upload.controller;

import com.alibaba.fastjson.JSONObject;
import io.lance.web.module.common.upload.util.Constant;
import io.lance.web.module.common.upload.util.IoUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Author Lance.
 * Date: 2017-09-07 11:33
 * Desc: 文件分片token
 */
@RestController
public class TokenController {

    private static final Logger logger = LogManager.getLogger(TokenController.class);


    static final String SERVER_FIELD = "server";


    /**
     * @desc: 获取token
     * @author lance
     * @time: 2017-09-07 13:35:58
     */
    @RequestMapping(value = "/token", method = {RequestMethod.GET, RequestMethod.POST})
    public JSONObject token(@RequestParam String name, @RequestParam Long size) {
        logger.info("文件名称:{},文件大小:{}", name, IoUtil.getFileSize(size));
        JSONObject object = new JSONObject();
        object.put(Constant.SUCCESS, false);
        try {
            String token = generateToken(name, String.valueOf(size));
            object.put(Constant.TOKEN_FIELD, token);
            object.put(Constant.SUCCESS, true);
            object.put(Constant.MESSAGE, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }


    /**
     * @desc: 生成token
     * @author lance
     * @time: 2017-09-07 13:38:10
     */
    private String generateToken(String name, String size) throws IOException {
        if (StringUtils.isBlank(name) || StringUtils.isBlank(size)) {
            return "";
        }
        int code = name.hashCode();
        String token = (code > 0 ? "A" : "B") + Math.abs(code) + "_" + size.trim();
        IoUtil.storeToken(token);

        return token;
    }


}
