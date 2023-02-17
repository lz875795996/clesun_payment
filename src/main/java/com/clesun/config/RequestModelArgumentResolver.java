package com.clesun.config;

import com.alibaba.fastjson.JSON;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Type;

/**
 * @author: lixq
 * @date: 2019-12-19
 * @描述: spring的参数注解解析器
 */
public class RequestModelArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestModel.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        final String parameterJson = webRequest.getParameter(parameter.getParameterName());

        //parameter.getGenericParameterType() 返回参数的完整类型（带泛型）
        final Type type = parameter.getGenericParameterType();
        final Object o = JSON.parseObject(parameterJson, type);
        return o;
    }
}
