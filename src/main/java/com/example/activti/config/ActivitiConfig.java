package com.example.activti.config;

import javax.sql.DataSource;

import org.activiti.engine.*;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.history.HistoryLevel;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import cn.hutool.core.util.IdUtil;

/**
 * ActivitiConfig
 *
 * @author Joyuce
 * @date 2020年06月05日
 */
@Configuration
public class ActivitiConfig {

    @Autowired
    DataSource dataSource;

    //流程配置，与spring整合采用SpringProcessEngineConfiguration这个实现
    @Bean
    public ProcessEngineConfiguration processEngineConfiguration(PlatformTransactionManager transactionManager){
        SpringProcessEngineConfiguration processEngineConfiguration = new SpringProcessEngineConfiguration();
        processEngineConfiguration.setDataSource(dataSource);
        processEngineConfiguration.setDatabaseSchemaUpdate("true");
        processEngineConfiguration.setDatabaseType("mysql");

        processEngineConfiguration.setTransactionManager(transactionManager);
        //主键生成替换为 uuid
        processEngineConfiguration.setIdGenerator(IdUtil::fastSimpleUUID);

        //流程图字体
        processEngineConfiguration.setActivityFontName("宋体");
        processEngineConfiguration.setAnnotationFontName("宋体");
        processEngineConfiguration.setLabelFontName("宋体");
        //启用定时任务
        processEngineConfiguration.setJobExecutorActivate(true);
        //这段代码表示是否使用activiti自带用户组织表，如果是，这里为true
        // 如果不是，这里为false。由于本项目使用了视图的方式代替了原有的用户组织表，
        // 所以这里设置为false，这样启动就不用去检查用户组织表是否存在。
        processEngineConfiguration.setDbIdentityUsed(false);
        //databaseSchemaUpdate配置项可以设置流程引擎启动和关闭时数据库执行的策略。 databaseSchemaUpdate有以下四个值：
        //false：false为默认值，设置为该值后，Activiti在启动时，会对比数据库表中保存的版本，如果没有表或者版本不匹配时，将在启动时抛出异常。
        //true：设置为该值后，Activiti会对数据库中所有的表进行更新，如果表不存在，则Activiti会自动创建。
        //create-drop：Activiti启动时，会执行数据库表的创建操作，在Activiti关闭时，执行数据库表的删除操作。
        //drop-create：Activiti启动时，执行数据库表的删除操作在Activiti关闭时，会执行数据库表的创建操作。
        processEngineConfiguration.setDatabaseSchemaUpdate("true");
        //history-level对于历史数据，保存到何种粒度，Activiti提供了history-level属性对其进行配置。history-level属性有点像log4j的日志输出级别，该属性有以下四个值：
        //none：不保存任何的历史数据，因此，在流程执行过程中，这是最高效的。
        //activity：级别高于none，保存流程实例与流程行为，其他数据不保存。
        //audit：除activity级别会保存的数据外，还会保存全部的流程任务及其属性。audit为history的默认值。
        //full：保存历史数据的最高级别，除了会保存audit级别的数据外，还会保存其他全部流程相关的细节数据，包括一些流程参数等。
        processEngineConfiguration.setHistoryLevel(HistoryLevel.FULL);
        //db-history-used为true表示使用历史表，如果不配置，则工程启动后可以检查数据库，只建立了17张表，历史表没有建立，则流程图及运行节点无法展示
        processEngineConfiguration.setDbHistoryUsed(true);
        return processEngineConfiguration;
    }

    //流程引擎，与spring整合使用factoryBean
    @Bean
    public ProcessEngineFactoryBean processEngine(ProcessEngineConfiguration processEngineConfiguration){
        ProcessEngineFactoryBean processEngineFactoryBean = new ProcessEngineFactoryBean();
        processEngineFactoryBean.setProcessEngineConfiguration((ProcessEngineConfigurationImpl) processEngineConfiguration);
        return processEngineFactoryBean;
    }

    //八大接口
    @Bean
    public RepositoryService repositoryService(ProcessEngine processEngine){
        return processEngine.getRepositoryService();
    }

    @Bean
    public RuntimeService runtimeService(ProcessEngine processEngine){
        return processEngine.getRuntimeService();
    }

    @Bean
    public TaskService taskService(ProcessEngine processEngine){
        return processEngine.getTaskService();
    }

    @Bean
    public HistoryService historyService(ProcessEngine processEngine){
        return processEngine.getHistoryService();
    }

    @Bean
    public FormService formService(ProcessEngine processEngine){
        return processEngine.getFormService();
    }

    @Bean
    public IdentityService identityService(ProcessEngine processEngine){
        return processEngine.getIdentityService();
    }

    @Bean
    public ManagementService managementService(ProcessEngine processEngine){
        return processEngine.getManagementService();
    }

    @Bean
    public DynamicBpmnService dynamicBpmnService(ProcessEngine processEngine){
        return processEngine.getDynamicBpmnService();
    }

    //八大接口 end
}
