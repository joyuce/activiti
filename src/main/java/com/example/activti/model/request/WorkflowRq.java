package com.example.activti.model.request;

import java.io.Serializable;
import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * WorkflowRq
 *
 * @author Joyuce
 * @date 2020年07月06日
 */
@ApiModel
@Data
public class WorkflowRq implements Serializable {

    @ApiModelProperty(value = "流程定义的名称")
    private String processDefinitionKey;

    @ApiModelProperty(value = "流程启动输入的参数")
    private Map<String, Object> variables;

    @ApiModelProperty(value = "启动流程的用户id")
    private Integer userId;

    @ApiModelProperty(value = "租户隔离字段")
    private String tenantId;



    @ApiModelProperty(value = "流程id")
    private String processInstanceId;

    @ApiModelProperty(value = "删除原因")
    private String deleteReason;

}
