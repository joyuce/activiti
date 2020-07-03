package com.example.activti.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TestController
 *
 * @author Joyuce
 * @date 2020年06月10日
 */
@RestController
public class TestController {

    @Autowired
    RepositoryService repositoryService;
    @Autowired
    RuntimeService runtimeService;
    @Autowired
    TaskService taskService;
    @Autowired
    HistoryService historyService;

    @GetMapping("/test")
    public Object get() {
        List<Task> list = taskService.createTaskQuery()//创建任务查询对象
                                     .taskAssignee("客户").list();
        List<Map<String, Object>> rs = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (Task task : list) {

                Map<String, Object> t = new HashMap<>();
                rs.add(t);
                t.put("任务ID:", task.getId());
                t.put("任务名称:", task.getName());
                t.put("任务的创建时间:", task.getCreateTime());
                t.put("任务的办理人:", task.getAssignee());
                t.put("流程实例ID：", task.getProcessInstanceId());
                t.put("执行对象ID:", task.getExecutionId());
                t.put("流程定义ID:", task.getProcessDefinitionId());
                t.put("getOwner:", task.getOwner());
                t.put("getCategory:", task.getCategory());
                t.put("getDescription:", task.getDescription());
                t.put("getFormKey:", task.getFormKey());
                Map<String, Object> map = task.getProcessVariables();
                t.put("ProcessVariables", map);

            }
        }
        return rs;
    }

}
