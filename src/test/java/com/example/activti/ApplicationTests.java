package com.example.activti;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ActivtiApplication.class)
public class ApplicationTests {

	@Autowired
	RepositoryService repositoryService;
	@Autowired
	RuntimeService runtimeService;
	@Autowired
	TaskService taskService;

	/**
	 * 部署流程定义
	 */
	@Test
	public void prepare() {
		Deployment deployment = repositoryService.createDeployment()//创建一个部署对象
												 .name("报修流程")
												 .addClasspathResource("processes/报修流程.bpmn20.xml")
												 .addClasspathResource("processes/报修流程.repair.png")
												 .deploy();
		System.out.println("部署ID："+deployment.getId());
		System.out.println("部署名称："+deployment.getName());
	}
	/**启动流程实例分配任务给个人*/
	@Test
	public void start() {

		String userKey="PTM";//脑补一下这个是从前台传过来的数据
		String processDefinitionKey ="myProcess";//每一个流程有对应的一个key这个是某一个流程内固定的写在bpmn内的
		HashMap<String, Object> variables=new HashMap<>();
		variables.put("userKey", userKey);//userKey在上文的流程变量中指定了
		variables.put("day", 2);
		variables.put("users", "a");

		ProcessInstance instance = runtimeService
				.startProcessInstanceByKey(processDefinitionKey,variables);

		System.out.println("流程实例ID:"+instance.getId());
		System.out.println("流程定义ID:"+instance.getProcessDefinitionId());
	}

	/**查询当前人的个人任务*/
	@Test
	public void findTask(){
		String assignee = "PTM";
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

	@Test
	public void completeTask(){
		//任务ID
		String taskId = "5005";

		HashMap<String, Object> variables=new HashMap<>();
		variables.put("days", 1);//userKey在上文的流程变量中指定了

		taskService.complete(taskId,variables);
		System.out.println("完成任务：任务ID："+taskId);
	}

	/**查询当前人的组任务*/
	@Test
	public void findTaskGroup(){

		String assignee = "PTM";
		List<Task> list = taskService.createTaskQuery()//创建任务查询对象
									 //                .taskCandidateUser("ZJ")//指定组任务查询
									 .taskAssignee(assignee)
									 .list();
		String taskid ="";
		String instanceId ="";
		if(list!=null && list.size()>0){
			for(Task task:list){
				System.out.println("任务ID:"+task.getId());
				System.out.println("任务名称:"+task.getName());
				System.out.println("任务的创建时间:"+task.getCreateTime());
				System.out.println("任务的办理人:"+task.getAssignee());
				System.out.println("流程实例ID："+task.getProcessInstanceId());
				System.out.println("执行对象ID:"+task.getExecutionId());
				System.out.println("流程定义ID:"+task.getProcessDefinitionId());
			}
		}
	}









}
