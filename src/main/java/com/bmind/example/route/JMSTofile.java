package com.bmind.example.route;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.component.jacksonxml.JacksonXMLDataFormat;
import org.apache.camel.language.xpath.XPathBuilder;
import org.apache.camel.model.dataformat.BindyType;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

import com.bmind.example.aggregation.EmpleadoAggregation;
import com.bmind.example.dto.EmpleadoDto;
import com.bmind.example.entity.CrearEmpleadoCsvEntity;
import com.bmind.example.entity.EmpleadoEntity;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Component
public class JMSTofile extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		
		from("activemq:queue:bmindQueueInTest?testConnectionOnStartup=true")
		.tracing()
			.log(LoggingLevel.INFO, "Mensaje recibido desde la cola ${body}")
			.split(jsonpath("$"), new EmpleadoAggregation())
			.log("Body 1 : ${body}")
			.marshal()
			.json(JsonLibrary.Jackson)
				.log(LoggingLevel.INFO, "Empleado Json:  ${body}")
				.unmarshal(new JacksonDataFormat(EmpleadoDto.class))
			.log("Body 2: ${body}")
			.process(new Processor() {
				
				@Override
				public void process(Exchange exchange) throws Exception {
					EmpleadoDto dto = exchange.getIn().getBody(EmpleadoDto.class);
					exchange.getIn().setBody(new CrearEmpleadoCsvEntity(dto.getNombres(),dto.getApellidos(),dto.getDireccion(),dto.getTelefono(),dto.getEmail(),dto.getCargo(),dto.getSalario()));
				}
			})
			.marshal()
			.bindy(BindyType.Csv,CrearEmpleadoCsvEntity.class)
			.log("Body 2 : ${body}")
			.to("file:C:/Users/jhohan.hoyos.meneses/OneDrive - Accenture/Desktop?fileName=Empleados.csv&fileExist=Append");

		/*XPathBuilder xPathBuilder = new XPathBuilder("//Empleados/Empleado");
		JacksonXMLDataFormat jacksonFormat = new JacksonXMLDataFormat();
		
		jacksonFormat.setUnmarshalType(EmpleadoEntity.class);
		jacksonFormat.setInclude(Include.NON_NULL.name());
		
		from("activemq:queue:bmindQueueInTest?testConnectionOnStartup=true")
		.tracing()
			.log(LoggingLevel.INFO, "Mensaje recibido desde la cola ${body}")
		.split(xPathBuilder, new EmpleadoAggregation())
			.log(LoggingLevel.INFO, "Persona XML: ${body}")
			.unmarshal(jacksonFormat)
			.log(LoggingLevel.INFO, "Perosna (Objeto): ${body}")
		//	.to("sql:classpath:sql/insert-data.sql")
			.to("activemq:queue:bmindQueueOutputTest")
		.end()
		.log(LoggingLevel.INFO, "Archivo: ${body}")
		.to("file:src/main/resources/files/out?fileName=empleados.csv");*/
	
	}

}
