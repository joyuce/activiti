package com.example.activti.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Workflow
 */
@ApiModel
@Data
public class Workflow implements Serializable {
    private static final long serialVersionUID = -3913890908452340051L;

    @ApiModelProperty(value = "流程id")
    private String processInstanceId;

    @ApiModelProperty(value = "流程定义id")
    private String processDefinitionId;

    @ApiModelProperty(value = "流程定义的关键字")
    private String processDefinitionKey;

    @ApiModelProperty(value = "流程定义的名称")
    private String processDefinitionName;

    @ApiModelProperty(value = "流程定义的版本号")
    private Integer processDefinitionVersion;

    @ApiModelProperty(value = "文档id")
    private String deploymentId;

    @ApiModelProperty(value = "业务关键字")
    private String businessKey;

    @ApiModelProperty(value = "是否被挂起")
    private boolean isSuspended;

    @ApiModelProperty(value = "流程参数")
    private Map<String, Object> processVariables;

    @ApiModelProperty(value = "租户隔离字段")
    private String tenantId;

    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "结束活动id")
    private String endActivityId;
    @ApiModelProperty(value = "启动用户id")
    private String startUserId;
    @ApiModelProperty(value = "启动活动id")
    private String startActivityId;
    @ApiModelProperty(value = "父级流程id")
    private String superProcessInstanceId;
    @ApiModelProperty(value = "删除原因")
    private String deleteReason;
}
