package io.lance.web.module.api.test.controller;

import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Author Lance.
 * Date: 2017-09-01 16:45
 * Desc:
 */
@Controller
@RequestMapping("/api/test")
public class TestController {

    private static Logger logger = LogManager.getLogger(TestController.class);


    @ResponseBody
    @RequestMapping(value = "/check")
    public String check(@RequestParam("user_name") String userName) {
        logger.info("userName :{}", userName);
        return "ok";
    }

    @ResponseBody
    @RequestMapping(value = "/ids")
    public Map ids(String [] ids) {
        logger.info("数组赋值 :{}", ids);
        Map values = Maps.newHashMap();
        values.put("id", ids);
        return values;
    }
}
