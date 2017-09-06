package io.lance.web.module.common.upload.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Author Lance.
 * Date: 2017-09-06 15:32
 * Desc:
 */
@Controller
@RequestMapping("/test/upload")
public class TestUploadController {


    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String upload(){
        return "common/upload/index";
    }


}
