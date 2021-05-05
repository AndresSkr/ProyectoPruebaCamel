package com.bmind.example.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.bmind.example.aggregation.EmpleadoAggregation;
import com.bmind.example.dto.EmpleadoDto;


@Component
public class QueueRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		restConfiguration()
		.component("servlet")
		.bindingMode(RestBindingMode.json);
		
		
		rest("empleados")
		.id("creacion-persona-router")
		.post("crear")
		.consumes(MediaType.APPLICATION_JSON_VALUE)
		.route()
		//.split(body(), new EmpleadoAggregation())
		.to("direct:Queue")
		.endRest();
		
		from("direct:Queue")
		.log("LISTO PARA MANDARLOA  ALA COLA ${body}")
		.marshal()
		.json(JsonLibrary.Jackson)
		.to("activemq:queue:bmindQueueInTest?disableReplyTo=true")
		.end();
	}

}
