package com.example.activti;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * RepairTest
 *
 * @author Joyuce
 * @date 2020年06月10日
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ActivtiApplication.class)
public class RepairTest {

    @Autowired
    RepositoryService repositoryService;
    @Autowired
    RuntimeService runtimeService;
    @Autowired
    TaskService taskService;
    @Autowired
    HistoryService historyService;
    @Autowired
    IdentityService identityService;

    /**
     * 创建组
     * 操作的数据表：act_id_group
     */
    @Test
    public void test2() {
        //historyService.createHistoricProcessInstanceQuery().variableValueEquals().startedBy()
        //historyService.createHistoricTaskInstanceQuery().taskOwner()
        //taskService.setOwner();
        taskService.setAssignee("8ed7e64a95224980ba6d6fdba95b297d","123");
        //Group group = new GroupEntity();
        //group.setId("1");
        //group.setName("财务组");
        //group.setType("G");
        //identityService.saveGroup(group);
    }


    /**
     * 性能测试数据
     */
    @Test
    public void testPer() {
        int num = 100000;
        for (int i = 0; i < num; i++) {
            //启动流程
            //选择流程执行
            String processDefinitionKey = "repair";
            //流程参数填充
            HashMap<String, Object> variables = new HashMap<>();
            variables.put("client", "客户"+i);
            ProcessInstance instance = runtimeService.startProcessInstanceByKey(processDefinitionKey, variables);
        }
    }

    /**
     * 全部流程测试
     */
    @Test
    public void testAll(){
        //启动流程
        start();
        //客户提交报修
        subRepair();
        //指定上门人员
        arrange();
        //上门维修
        maintain();
        //客户确认 维修失败
        confirm();
        //重新  指定上门人员
        arrangeAgain();
        //重新 上门维修
        maintainAgain();
        //客户确认 维修成功
        confirmAgain();
        //查询流程状态（正在执行 or 已经执行结束）
        processState();
        //查任务历史
        historyTaskInstanceList();
    }


    /**
     * 启动流程
     */
    @Test
    public void start() {
        //选择流程执行
        String processDefinitionKey = "repair";
        //流程参数填充
        HashMap<String, Object> variables = new HashMap<>();
        variables.put("client", "客户");
        //设置流程发起人
        Authentication.setAuthenticatedUserId("系统");
        ProcessInstance instance = runtimeService.startProcessInstanceByKey(processDefinitionKey, variables);
        //这个方法最终使用一个ThreadLocal类型的变量进行存储，也就是与当前的线程绑定，所以流程实例启动完毕之后，需要设置为null，防止多线程的时候出问题。
        Authentication.setAuthenticatedUserId(null);
        System.out.println("流程实例ID:" + instance.getId());
        System.out.println("流程定义ID:" + instance.getProcessDefinitionId());
        processInstanceId = instance.getId();
        
    }

    String processInstanceId = "187501";

    /**
     * 客户提交报修
     */
    @Test
    public void subRepair() {
        List<Task> list = taskService.createTaskQuery()//创建任务查询对象
                                     .taskAssignee("客户").list();
        //任务ID
        String taskId = list.get(0).getId();
        Map<String, Object> variables = new HashMap<>();
        variables.put("报修情况", "机器坏了");
        //变量和当前任务关联
        taskService.setVariablesLocal(taskId, variables);
        taskService.complete(taskId);
        System.out.println("完成任务：任务ID：" + taskId);
    }

    /**
     * 指定上门人员
     */
    @Test
    public void arrange() {
        List<Task> list = taskService.createTaskQuery()//创建任务查询对象
                                     .processInstanceId(processInstanceId).active().list();
        //任务ID
        String taskId = list.get(0).getId();
        String assignee = "维修人员-1";
        taskService.claim(taskId, assignee);
    }

    /**
     * 上门维修
     */
    @Test
    public void maintain() {
        List<Task> list = taskService.createTaskQuery()//创建任务查询对象
                                     .taskAssignee("维修人员-1").list();
        //任务ID
        String taskId = list.get(0).getId();
        Map<String, Object> variables = new HashMap<>();
        variables.put("维修情况", "经过一番努力，机器修好了");
        //变量和当前任务关联
        taskService.setVariablesLocal(taskId, variables);
        taskService.complete(taskId);
        System.out.println("完成任务：任务ID：" + taskId);
    }

    /**
     * 客户确认 维修失败
     */
    @Test
    public void confirm() {
        List<Task> list = taskService.createTaskQuery()//创建任务查询对象
                                     .taskAssignee("客户").list();
        //任务ID
        String taskId = list.get(0).getId();
        Map<String, Object> variables = new HashMap<>();
        variables.put("status", "失败");
        //维修失败
        taskService.setVariablesLocal(taskId, variables);
        taskService.complete(taskId, variables);
        System.out.println("完成任务：任务ID：" + taskId);
    }

    /**
     * 重新  指定上门人员
     */
    @Test
    public void arrangeAgain() {
        List<Task> list = taskService.createTaskQuery()//创建任务查询对象
                                     .processInstanceId(processInstanceId).active().list();
        //任务ID
        String taskId = list.get(0).getId();
        String assignee = "维修人员-2";
        taskService.claim(taskId, assignee);
    }

    /**
     * 重新 上门维修
     */
    @Test
    public void maintainAgain() {
        List<Task> list = taskService.createTaskQuery()//创建任务查询对象
                                     .taskAssignee("维修人员-2").list();
        //任务ID
        String taskId = list.get(0).getId();
        Map<String, Object> variables = new HashMap<>();
        variables.put("维修情况", "经过一番努力，机器终于修好了");
        //变量和当前任务关联
        taskService.setVariablesLocal(taskId, variables);
        taskService.complete(taskId);
        System.out.println("完成任务：任务ID：" + taskId);
    }

    /**
     * 客户确认 维修成功
     */
    @Test
    public void confirmAgain() {
        List<Task> list = taskService.createTaskQuery()//创建任务查询对象
                                     .taskAssignee("客户").list();
        //任务ID
        String taskId = list.get(0).getId();
        Map<String, Object> variables = new HashMap<>();
        variables.put("status", "成功");
        //维修成功
        taskService.setVariablesLocal(taskId, variables);
        taskService.complete(taskId, variables);
        System.out.println("完成任务：任务ID：" + taskId);
    }

    /**
     * 查询流程状态（正在执行 or 已经执行结束）
     */
    @Test
    public void processState() {
        ProcessInstance pi = runtimeService // 获取运行时Service
                                            .createProcessInstanceQuery() // 创建流程实例查询
                                            .processInstanceId(processInstanceId) // 用流程实例id查询
                                            .singleResult();
        if (pi != null) {
            System.out.println("流程正在执行！");
        } else {
            System.out.println("流程已经执行结束！");
        }
    }

    /**
     * 查任务历史
     */
    @Test
    public void historyTaskInstanceList() {

        List<HistoricTaskInstance> list = historyService // 历史相关Service
                                                         .createHistoricTaskInstanceQuery() // 创建历史活动实例查询
                                                         //.processInstanceId(processInstanceId) // 执行流程实例id
                                                         .orderByHistoricTaskInstanceStartTime().asc().list();
        for (HistoricTaskInstance hai : list) {
            System.out.println("任务ID:" + hai.getId());
            System.out.println("流程实例ID:" + hai.getProcessInstanceId());
            System.out.println("任务名称：" + hai.getName());
            System.out.println("办理人：" + hai.getAssignee());
            System.out.println("开始时间：" + hai.getStartTime());
            System.out.println("结束时间：" + hai.getEndTime());

            List<HistoricVariableInstance> list1 = historyService.createHistoricVariableInstanceQuery()
                                                                 .taskId(hai.getId()).list();
            for (HistoricVariableInstance v : list1) {
                System.out.println("任务变量：" + v.getVariableName() + " = " + v.getValue());
            }
            System.out.println("=================================");
        }
    }

    /**
     * 查流程历史
     */
    @Test
    public void historyProcessInstanceList() {

        List<HistoricProcessInstance> list = historyService // 历史相关Service
                                                            .createHistoricProcessInstanceQuery() // 创建历史活动实例查询
                                                            .unfinished()
                                                            .list();
        for (HistoricProcessInstance hai : list) {
            System.out.println("=================================");
            System.out.println("流程实例所属流程定义id：" + hai.getProcessDefinitionId());
            System.out.println("流程实例id："+ hai.getId());
            System.out.println("业务标识：" + hai.getBusinessKey());
            System.out.println("开始执行时间：" + hai.getStartTime());
            System.out.println("结束执行时间：" + hai.getEndTime());
            System.out.println("执行时长：" + hai.getDurationInMillis());

            List<HistoricVariableInstance> list1 = historyService.createHistoricVariableInstanceQuery()
                                                                 .processInstanceId(hai.getId()).list();
            for (HistoricVariableInstance v : list1) {
                System.out.println("流程变量：" + v.getVariableName() + " = " + v.getValue());
            }
            System.out.println("=================================");
        }
    }


    /**
     * 取消流程
     */
    @Test
    public void cancelProcess() {
        runtimeService.deleteProcessInstance(processInstanceId, "不修了");
    }

    /**
     * 查询全部任务
     */
    @Test
    public void findAllTask() {
        List<Task> list = taskService.createTaskQuery()//创建任务查询对象
                                     .list();
        if (list != null && list.size() > 0) {
            for (Task task : list) {
                System.out.println("任务ID:" + task.getId());
                System.out.println("任务名称:" + task.getName());
                System.out.println("任务的创建时间:" + task.getCreateTime());
                System.out.println("任务的办理人:" + task.getAssignee());
                System.out.println("流程实例ID：" + task.getProcessInstanceId());
                System.out.println("执行对象ID:" + task.getExecutionId());
                System.out.println("流程定义ID:" + task.getProcessDefinitionId());
                System.out.println("getOwner:" + task.getOwner());
                System.out.println("getCategory:" + task.getCategory());
                System.out.println("getDescription:" + task.getDescription());
                System.out.println("getFormKey:" + task.getFormKey());
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

    /**
     * 下载资源
     */
    @Test
    public void downResource() {

        //获取查询器
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        //设置查询条件
        processDefinitionQuery.processDefinitionKey("repair");

        //执行查询操作，得到想要流程定义信息
        ProcessDefinition processDefinition = processDefinitionQuery.singleResult();

        //通过流程定义信息得到部署id
        String deploymentId = processDefinition.getDeploymentId();

        //实现读写bpmn文件信息
        InputStream png = repositoryService
                .getResourceAsStream(deploymentId, processDefinition.getDiagramResourceName());

        InputStream xml = repositoryService
                .getResourceAsStream(deploymentId, processDefinition.getResourceName());

        //构建输出流
        //File file = new File();

        FileOutputStream pngFile = null;
        FileOutputStream xmlFile = null;
        try {
            pngFile = new FileOutputStream(
                    "C:\\Users\\EDZ\\Desktop\\新建文件夹\\" + processDefinition.getDiagramResourceName());
            xmlFile = new FileOutputStream(
                    "C:\\Users\\EDZ\\Desktop\\新建文件夹\\" + processDefinition.getResourceName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //输入流，输出流的转换
        try {
            IOUtils.copy(png, pngFile);
            IOUtils.copy(xml, xmlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
