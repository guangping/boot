package io.lance.common.core.web.exception;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.lance.common.core.bean.JsonResult;
import io.lance.common.core.exception.EbsException;
import io.lance.common.core.util.Constans;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Author Lance.
 * Date: 2017-09-05 17:00
 * Desc:异常处理
 */
public class SpringExceptionResolver extends SimpleMappingExceptionResolver {

    private static final Logger logger = LogManager.getLogger(SpringExceptionResolver.class);


    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String viewName = determineViewName(ex, request);
        ex.printStackTrace();
        try {
            //返回json的情况
            if (handler instanceof HandlerMethod) {
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                ResponseBody responseBody = handlerMethod.getMethodAnnotation(ResponseBody.class);
                if (null != responseBody) {
                    handleException(ex, response);
                    return null;
                }
                Class<?> beanType = handlerMethod.getBeanType();
                responseBody = beanType.getAnnotation(ResponseBody.class);
                RestController restController = beanType.getAnnotation(RestController.class);
                if (null != responseBody || null != restController) {
                    handleException(ex, response);
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null != viewName) {
            if (!(request.getHeader("accept").indexOf("application/json") > -1 || (request
                    .getHeader("X-Requested-With") != null && request
                    .getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1))) {
                Integer statusCode = determineStatusCode(request, viewName);
                if (statusCode != null) {
                    applyStatusCodeIfPossible(request, response, statusCode);
                }
                return getModelAndView(viewName, ex, request);
            }
            try {
                handleException(ex, response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void handleException(Exception ex, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();

        JsonResult jsonResult = new JsonResult();
        jsonResult.setSuccess(Constans.NO);
        jsonResult.setMessage("server error");

        if (ex instanceof EbsException) {
            EbsException exception = (EbsException) ex;
            jsonResult.setMessage(exception.getMessage());
            jsonResult.setCode(exception.getCode());
        }
        writer.write(JSONObject.toJSONString(jsonResult, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNonStringValueAsString, SerializerFeature.WriteNonStringKeyAsString));
        writer.flush();
        writer.close();
    }
}
