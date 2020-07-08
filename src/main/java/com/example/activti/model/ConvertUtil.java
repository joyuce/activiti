package com.example.activti.model;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;

/**
 * ConvertUtil
 *
 * @author Joyuce
 * @date 2020年07月07日
 */
public class ConvertUtil {

    public static Task convert2Task(HistoricTaskInstance instance){
        Task t = new Task();
        t.setTaskId(instance.getId());
        t.setName(instance.getName());
        t.setOwner(instance.getOwner());
        t.setAssignee(instance.getAssignee());
        t.setProcessInstanceId(instance.getProcessInstanceId());
        t.setCreateTime(instance.getCreateTime());
        t.setStartTime(instance.getStartTime());
        t.setEndTime(instance.getEndTime());
        t.setDeleteReason(instance.getDeleteReason());
        t.setClaimTime(instance.getClaimTime());
        t.setTenantId(instance.getTenantId());
        t.setTaskLocalVariables(instance.getTaskLocalVariables());
        t.setProcessVariables(instance.getProcessVariables());
        return t;
    }




    public static Workflow convert2Workflow(ProcessInstance instance) {
        Workflow w = new Workflow();
        w.setProcessInstanceId(instance.getId());
        w.setProcessDefinitionId(instance.getProcessDefinitionId());
        w.setProcessDefinitionKey(instance.getProcessDefinitionKey());
        w.setProcessDefinitionName(instance.getProcessDefinitionName());
        w.setProcessDefinitionVersion(instance.getProcessDefinitionVersion());
        w.setDeploymentId(instance.getDeploymentId());
        w.setBusinessKey(instance.getBusinessKey());
        w.setSuspended(instance.isSuspended());
        w.setProcessVariables(instance.getProcessVariables());
        w.setTenantId(instance.getTenantId());
        w.setName(instance.getName());
        w.setDescription(instance.getDescription());
        return w;
    }

    public static Workflow convertHis2Workflow(HistoricProcessInstance instance) {
        Workflow w = new Workflow();
        w.setProcessInstanceId(instance.getId());
        w.setProcessDefinitionId(instance.getProcessDefinitionId());
        w.setProcessDefinitionKey(instance.getProcessDefinitionKey());
        w.setProcessDefinitionName(instance.getProcessDefinitionName());
        w.setProcessDefinitionVersion(instance.getProcessDefinitionVersion());
        w.setDeploymentId(instance.getDeploymentId());
        w.setBusinessKey(instance.getBusinessKey());
        w.setProcessVariables(instance.getProcessVariables());
        w.setTenantId(instance.getTenantId());
        w.setName(instance.getName());
        w.setDescription(instance.getDescription());
        w.setStartTime(instance.getStartTime());
        w.setEndTime(instance.getEndTime());
        w.setEndActivityId(instance.getStartActivityId());
        w.setStartActivityId(instance.getStartActivityId());
        w.setStartUserId(instance.getStartUserId());
        w.setSuperProcessInstanceId(instance.getSuperProcessInstanceId());
        w.setDeleteReason(instance.getDeleteReason());
        return w;
    }

}
