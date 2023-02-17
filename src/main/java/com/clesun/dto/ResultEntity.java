package com.clesun.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@ApiModel(description = "返回响应数据")
public class ResultEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "接口返回错误码")
    private int code;
    @ApiModelProperty(value = "错误信息")
    private String msg;
    @ApiModelProperty(value = "时间戳")
    private Long timestamp = System.currentTimeMillis();
    @ApiModelProperty(value = "数据")
    private Object data;

    private final static int CODE_FAILED = -1;
    private final static int CODE_SUCCESS = 0;
    private final static int CODE_TIMEOUT = -2;

    public ResultEntity(Object data) {
        this.code = CODE_SUCCESS;
        this.timestamp = System.currentTimeMillis();
        this.msg = "success";
        this.data = data;
    }
    public ResultEntity(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public static ResultEntity success(String msg) {
        ResultEntity result = new ResultEntity();
        result.setCode(CODE_SUCCESS);
        result.setTimestamp(System.currentTimeMillis());
        result.setMsg(msg);
        return result;
    }

    public static ResultEntity success(String msg, Object data) {
        ResultEntity result = new ResultEntity();
        result.setCode(CODE_SUCCESS);
        result.setTimestamp(System.currentTimeMillis());
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static ResultEntity failed(String msg) {
        ResultEntity result = new ResultEntity();
        result.setCode(CODE_FAILED);
        result.setTimestamp(System.currentTimeMillis());
        result.setMsg(msg);
        return result;
    }

    public static ResultEntity timeout(String msg) {
        ResultEntity result = new ResultEntity();
        result.setCode(CODE_TIMEOUT);
        result.setTimestamp(System.currentTimeMillis());
        result.setMsg(msg);
        return result;
    }

    @Override
    public String toString() {
        return "ResultEntity{" + "code=" + code + ", msg='" + msg + '\'' + ", timestamp=" + timestamp + ", data=" + data + '}';
    }

}
