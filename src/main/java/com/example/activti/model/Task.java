package com.example.activti.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Task
 *
 * @author Joyuce
 * @date 2020年07月06日
 */
@ApiModel
@Data
public class Task implements Serializable {

    private static final long serialVersionUID = -1982009086725740002L;

    @ApiModelProperty(value = "任务id")
    private String taskId;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "所有者")
    private String owner;
    @ApiModelProperty(value = "代理人")
    private String assignee;

    @ApiModelProperty(value = "流程id")
    private String processInstanceId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "删除原因")
    private String deleteReason;
    @ApiModelProperty(value = "委派时间")
    private Date claimTime;

    @ApiModelProperty(value = "租户隔离字段")
    private String tenantId;

    @ApiModelProperty(value = "任务参数")
    private Map<String, Object> taskLocalVariables;

    @ApiModelProperty(value = "流程参数")
    private Map<String, Object> processVariables;

}
