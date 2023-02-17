package com.clesun.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        List<Parameter> parameterList = new ArrayList<>();
//        parameterList.add(new ParameterBuilder()
//                .name("authToken")
//                .description("TOKEN认证")
//                .modelRef(new ModelRef("String"))
//                .parameterType("header")
//                .required(false)
//                .build());
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.clesun"))
                //加了ApiOperation注解的类，才生成接口文档
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .groupName("聚合支付")
                .globalOperationParameters(parameterList)
                .apiInfo(buildApiInfo());
    }

    private ApiInfo buildApiInfo() {
        return new ApiInfoBuilder()
                .title("API接口文档")
                .description("SpringBoot Swagger2")
                .version("1.0")
                .license("旗硕科技")
                .licenseUrl("http://www.baidu.com")
                //用于定义服务的域名
                .termsOfServiceUrl("https://huanr.younongxin.com/promotion")
                .build();
    }
}
