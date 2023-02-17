package com.clesun.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="page翻页", description="")
public class MybatisPage {
    @ApiModelProperty(value = "每页显示多少条")
    private transient  int pageSize;
    @ApiModelProperty(value = "当前页")
    private transient  int currentPage;
    @ApiModelProperty(value = "查询起始时间")
    private transient Date queryStartTime ;
    @ApiModelProperty(value = "查询截止时间")
    private transient Date queryEndTime ;
    @ApiModelProperty(value = "模糊查询关键字")
    private transient String queryKey ;

    //模糊查询or "vqor ": {"dtuCode":1}
    private  transient Map<String,Object>vqor=new LinkedHashMap<String, Object>();
    //模糊查询and  "vqand": {"dtuCode":1}
    private  transient Map<String,Object>vqand=new LinkedHashMap<String, Object>();
    //模区间查询    "bq":    {"createTime":"'2021-07-24','2021-07-24'"}
    private  transient Map<String,Object>bq=new LinkedHashMap<String, Object>();
    //模区间查询  "in":{"factoryId":"['2']"}
    private  transient Map<String,Object>in=new LinkedHashMap<String, Object>();
    //模区间查询  "in":{"factoryId":"['2']"}
    private  transient Map<String,Object> notIn =new LinkedHashMap<String, Object>();

    //排序条件 默认：{"create_time" : "desc"}
    private transient Map<String, Object> orderBy = new LinkedHashMap<String, Object>();
}
