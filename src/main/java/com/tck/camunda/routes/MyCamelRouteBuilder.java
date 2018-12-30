package com.tck.camunda.routes;

import com.tck.camunda.CamundaCamelException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.springframework.stereotype.Component;


@Component
public class MyCamelRouteBuilder extends RouteBuilder {

	public static String dropFolder = System.getProperty("user.home") + System.getProperty("file.separator")
			+ "camunda-bpm-demo-camel";


	@Override
	public void configure() {
		log.info("=======================");
		log.info("Configuring Routes");

		// ################################
		// Drop folder starts via none start event
		from("file://" + dropFolder) // use drop folder
				.routeId("file") //
				.convertBodyTo(String.class) //
				.to("log:com.tck.camunda?level=INFO&showAll=true&multiline=true") // logging
				.to("camunda-bpm:start?processDefinitionKey=camundaCamel"); // and start process instance

		// ################################
		// Synchronous Service calles from process
		from("direct://syncService") // service name in memory
				.routeId("syncService") //
				.to("log:com.tck.camunda.routes?level=INFO&showAll=true&multiline=true") // logging
				.onException(CamundaCamelException.class) // map exception to BPMN error
//				.onException(Exception.class) // map exception to BPMN error
//				.throwException(new BpmnError("camel.error","!!!GOT ERROR!!!")) //
				.throwException(new BpmnError("camel.error"))
				.maximumRedeliveries(0)//
				.handled(true) // TODO: Check how we can avoid logging on console
				.end().process(new Processor() {
					@Override
					public void process(Exchange exchange) throws Exception {
						// always throwing error to demonstrate error event
						throw new CamundaCamelException("some error occured in service");
					}
				});

	}

}
