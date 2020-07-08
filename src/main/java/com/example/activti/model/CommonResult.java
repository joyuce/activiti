package com.example.activti.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author joyuce
 * @Type CommonRs
 * @Desc 通用结果
 * @date 2019年07月01日
 * @Version V1.0
 */
@Getter
@Setter
@ApiModel(value = "通用结果")
public class CommonResult<T> implements Serializable {

    /**
     * 序列化对象
     */
    private static final long serialVersionUID = -8226113453013682250L;

    /**
     * data 数据
     */
    @ApiModelProperty("数据")
    private T data;
    /**
     * 状态码
     */
    @ApiModelProperty("状态码")
    private String code;

    /**
     * 中文信息
     */
    @ApiModelProperty("中文信息")
    private String cnMsg;

    /**
     * 英文信息
     */
    @ApiModelProperty("英文信息")
    private String enMsg;

    public CommonResult() {

    }

    public CommonResult(T data) {
        this.data = data;

    }



}
