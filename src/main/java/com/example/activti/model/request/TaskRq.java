package com.example.activti.model.request;

import java.util.Map;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TaskRq
 *
 * @author Joyuce
 * @date 2020年07月06日
 */
@Data
public class TaskRq {

    @ApiModelProperty(value = "任务id")
    private String taskId;

    @ApiModelProperty(value = "任务参数")
    private Map<String, Object> taskLocalVariables;

    @ApiModelProperty(value = "删除原因")
    private String deleteReason;

    @ApiModelProperty(value = "用户id")
    private Integer userId;
}
