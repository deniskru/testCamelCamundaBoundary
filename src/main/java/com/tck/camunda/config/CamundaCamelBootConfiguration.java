package com.tck.camunda.config;

import org.apache.camel.CamelContext;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.camunda.bpm.camel.component.CamundaBpmComponent;
import org.camunda.bpm.camel.spring.CamelServiceImpl;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;

import static org.camunda.bpm.engine.ProcessEngines.getDefaultProcessEngine;

@Configuration
public class CamundaCamelBootConfiguration {
    @Autowired
    CamelContext camelContext;

    @Autowired
//    @Inject
 ProcessEngine processEngine;



    @Bean(name = "camel")
    public CamelServiceImpl camel() {
        CamelServiceImpl camelServiceImpl = new CamelServiceImpl();
        camelContext.setStreamCaching(true);
        camelServiceImpl.setCamelContext(camelContext);
        processEngine.getProcessEngineConfiguration().setEnableExceptionsAfterUnhandledBpmnError(true);
        camelServiceImpl.setProcessEngine(processEngine);
        return camelServiceImpl;
    }

    @Bean
    CamelContextConfiguration nameConfiguration() {
        return new CamelContextConfiguration() {
            @Override
            public void beforeApplicationStart(CamelContext camelContext) {
                processEngine.getProcessEngineConfiguration().setEnableExceptionsAfterUnhandledBpmnError(true);
                CamundaBpmComponent component = new CamundaBpmComponent(processEngine);
                SpringProcessEngineConfiguration config = (SpringProcessEngineConfiguration) processEngine.getProcessEngineConfiguration();
                config.getBeans().put("camel", camel());
                camelContext.addComponent("camunda-bpm", component);
            }

            @Override
            public void afterApplicationStart(CamelContext arg0) {

            }
        };
    }
}
