package com.example.activti.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.activti.model.CommonResult;
import com.example.activti.model.ConvertUtil;
import com.example.activti.model.Task;
import com.example.activti.model.Workflow;
import com.example.activti.model.request.TaskRq;
import com.example.activti.model.request.WorkflowRq;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * WorkflowController
 *
 * @author Joyuce
 * @date 2020年06月10日
 */
@RestController
@Api(description = "工作流", value = "amsp")
@RequestMapping(value = "/api/activiti/")
public class WorkflowController  {

    @Autowired
    RepositoryService repositoryService;
    @Autowired
    RuntimeService runtimeService;
    @Autowired
    TaskService taskService;
    @Autowired
    HistoryService historyService;

    @ApiOperation(value = "提交流程", notes = "提交流程")
    @RequestMapping(value = "/workflow", method = RequestMethod.POST)
    public CommonResult<Workflow> submitWorkflow(
            WorkflowRq rq
    ) {
        //选择流程执行
        String processDefinitionKey = rq.getProcessDefinitionKey();
        //流程参数填充
        Map<String, Object> variables = rq.getVariables();
        //设置流程发起人
        Integer userId = rq.getUserId();
        String tenantId = rq.getTenantId();
        Authentication.setAuthenticatedUserId(userId.toString());
        ProcessInstance instance = runtimeService
                .startProcessInstanceByKeyAndTenantId(processDefinitionKey, variables, tenantId);
        //这个方法最终使用一个ThreadLocal类型的变量进行存储，也就是与当前的线程绑定，所以流程实例启动完毕之后，需要设置为null，防止多线程的时候出问题。
        Authentication.setAuthenticatedUserId(null);
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                                                                        .processInstanceId(
                                                                                instance.getProcessInstanceId())
                                                                        .singleResult();
        Workflow workflow = ConvertUtil.convertHis2Workflow(historicProcessInstance);
        return new CommonResult<>(workflow);
    }

    
    public CommonResult<List<Workflow>> selectWorkflow(
            String tenantId, Boolean isFinished, Integer pageNum, Integer pageSize
    ) {
        // 创建历史活动实例查询
        HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery();
        query.processInstanceTenantId(tenantId);
        if (isFinished != null) {
            if (isFinished) {
                query.finished();
            } else {
                query.unfinished();
            }
        }
        List<HistoricProcessInstance> list = query.listPage(pageNum * pageSize, pageSize);

        List<Workflow> rs = new ArrayList<>();
        for (HistoricProcessInstance instance : list) {
            Workflow w = ConvertUtil.convertHis2Workflow(instance);
            rs.add(w);
        }
        return new CommonResult<>(rs);
    }

    
    public CommonResult<Boolean> cancelWorkflow(WorkflowRq rq) {
        runtimeService.deleteProcessInstance(rq.getProcessInstanceId(), rq.getDeleteReason());
        return new CommonResult<>(true);
    }

    
    public CommonResult<List<Task>> selectTask(
            String tenantId, String assignee, String processInstanceId, Boolean isFinished, Integer pageNum,
            Integer pageSize
    ) {

        HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery();
        query.taskTenantId(tenantId);
        if (StringUtils.isNotBlank(assignee)) {
            query.taskAssignee(assignee);
        }
        if (StringUtils.isNotBlank(processInstanceId)) {
            query.processInstanceId(processInstanceId);
        }
        if (isFinished != null) {
            if (isFinished) {
                query.finished();
            } else {
                query.unfinished();
            }
        }

        List<HistoricTaskInstance> list = query.listPage(pageNum * pageSize, pageSize);
        List<Task> rs = new ArrayList<>();
        for (HistoricTaskInstance instance : list) {
            Task t = ConvertUtil.convert2Task(instance);
            rs.add(t);
        }
        return new CommonResult<>(rs);
    }

    
    public CommonResult<Boolean> submitTask(
            TaskRq rq
    ) {
        String taskId = rq.getTaskId();
        Map<String, Object> taskLocalVariables = rq.getTaskLocalVariables();
        taskService.setVariablesLocal(taskId, taskLocalVariables);
        taskService.complete(taskId, taskLocalVariables);
        return new CommonResult<>(true);
    }

    
    public CommonResult<Boolean> claimTask(
            TaskRq rq
    ) {
        String taskId = rq.getTaskId();
        Integer userId = rq.getUserId();
        taskService.claim(taskId, userId.toString());
        return new CommonResult<>(true);
    }
}
