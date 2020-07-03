package com.example.activti;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * LeaveTest
 *
 * @author Joyuce
 * @date 2020年06月08日
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ActivtiApplication.class)
public class LeaveTest {

    @Autowired
    RepositoryService repositoryService;
    @Autowired
    RuntimeService runtimeService;
    @Autowired
    TaskService taskService;


    /**启动流程*/
    @Test
    public void start() {

        //选择流程执行
        String processDefinitionKey ="leave";
        //流程参数填充
        HashMap<String, Object> variables=new HashMap<>();
        variables.put("applicant", "张三");
        variables.put("approver", "李四");

        ProcessInstance instance = runtimeService
                .startProcessInstanceByKey(processDefinitionKey,variables);

        System.out.println("流程实例ID:"+instance.getId());
        System.out.println("流程定义ID:"+instance.getProcessDefinitionId());
    }

    /**查询当前人的个人任务*/
    @Test
    public void findTask(){
        String assignee = "张三";
        //String assignee = "李四";
        List<Task> list = taskService.createTaskQuery()//创建任务查询对象
                                     .taskAssignee(assignee)//指定个人任务查询
                                     .list();
        if(list!=null && list.size()>0){
            for(Task task:list){
                System.out.println("任务ID:"+task.getId());
                System.out.println("任务名称:"+task.getName());
                System.out.println("任务的创建时间:"+task.getCreateTime());
                System.out.println("任务的办理人:"+task.getAssignee());
                System.out.println("流程实例ID："+task.getProcessInstanceId());
                System.out.println("执行对象ID:"+task.getExecutionId());
                System.out.println("流程定义ID:"+task.getProcessDefinitionId());
                System.out.println("getOwner:"+task.getOwner());
                System.out.println("getCategory:"+task.getCategory());
                System.out.println("getDescription:"+task.getDescription());
                System.out.println("getFormKey:"+task.getFormKey());
                Map<String, Object> map = task.getProcessVariables();
                for (Map.Entry<String, Object> m : map.entrySet()) {
                    System.out.println("key:" + m.getKey() + " value:" + m.getValue());
                }
                for (Map.Entry<String, Object> m : task.getTaskLocalVariables().entrySet()) {
                    System.out.println("key:" + m.getKey() + " value:" + m.getValue());
                }

            }
        }
    }

    /**完成任务*/
    @Test
    public void completeTask(){
        //任务ID
        String taskId = "70005";
        Map<String, Object> variables = new HashMap<>();
        variables.put("status","不同意");
        taskService.complete(taskId,variables);
        System.out.println("完成任务：任务ID："+taskId);
    }

    /**取消流程*/
    @Test
    public void cancelProcess(){
        runtimeService.deleteProcessInstance("75001","不请假了");
        runtimeService.deleteProcessInstance("77501","不请假了");
    }

    /**查询全部任务*/
    @Test
    public void findAllTask(){
        List<Task> list = taskService.createTaskQuery()//创建任务查询对象
                                     .list();
        if(list!=null && list.size()>0){
            for(Task task:list){
                System.out.println("任务ID:"+task.getId());
                System.out.println("任务名称:"+task.getName());
                System.out.println("任务的创建时间:"+task.getCreateTime());
                System.out.println("任务的办理人:"+task.getAssignee());
                System.out.println("流程实例ID："+task.getProcessInstanceId());
                System.out.println("执行对象ID:"+task.getExecutionId());
                System.out.println("流程定义ID:"+task.getProcessDefinitionId());
                System.out.println("getOwner:"+task.getOwner());
                System.out.println("getCategory:"+task.getCategory());
                System.out.println("getDescription:"+task.getDescription());
                System.out.println("getFormKey:"+task.getFormKey());
                Map<String, Object> map = task.getProcessVariables();
                for (Map.Entry<String, Object> m : map.entrySet()) {
                    System.out.println("key:" + m.getKey() + " value:" + m.getValue());
                }
                for (Map.Entry<String, Object> m : task.getTaskLocalVariables().entrySet()) {
                    System.out.println("key:" + m.getKey() + " value:" + m.getValue());
                }

            }
        }
    }


}
