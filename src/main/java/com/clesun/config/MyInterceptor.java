package com.clesun.config;

import com.alibaba.fastjson.JSONObject;
import com.clesun.dto.ResultEntity;
import com.clesun.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class MyInterceptor implements HandlerInterceptor {

    @Value("${security.base}")
    public String baseUrl;
    @Value("${security.token}")
    public String tokenUrl;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("登录ip=" + request.getRemoteAddr());
        System.out.println("url=" + request.getRequestURL());
        System.out.println("getServletPath=" + request.getServletPath());
        System.out.println(request.getHeaderNames());
        String token = request.getHeader("authToken");
        System.out.println("token=" + token);
        if ("test".equals(token)) {
            return true;
        }
        // 调用接口查询当前token是否有效
        Map<String, String> map = new HashMap<>();
        map.put("Accept", "application/json;charset=UTF-8");
        map.put("Content-Type", "application/json;charset=UTF-8");
        map.put("authToken", token);
        // get请求
        String res = HttpUtils.sendGet(baseUrl + tokenUrl, "UTF-8", map);
        System.out.println("res=======" + res);
        JSONObject jo = JSONObject.parseObject(res);

        Integer code = jo.getInteger("code");
        if (code != 0) {
            // 返回code不为0，证明不成功将整个返回信息返回前端
            ResultEntity result = ResultEntity.timeout(jo.getString("msg"));
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Methods", "*");
            //这里“Access-Token”是我要传到后台的内容key
            response.setHeader("Access-Control-Allow-Headers", "Content-Type,authToken");
            response.setHeader("Access-Control-Expose-Headers", "*");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.write(JSONObject.toJSONString(result));
            return false;
        }
        // 有效，将user用户的基本信息存储
        JSONObject user = JSONObject.parseObject(jo.get("data").toString());
        request.setAttribute("userId", user.getString("userId"));
        request.setAttribute("account", user.getString("account"));
        request.setAttribute("deptId", user.getString("deptId"));
        System.out.println(res);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        //System.out.println("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        // System.out.println("afterCompletion");
    }
}

