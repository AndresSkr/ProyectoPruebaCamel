package com.bmind.example.route;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.dataformat.BindyType;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

import com.bmind.example.dto.EmpleadoDto;
import com.bmind.example.entity.CrearEmpleadoCsvEntity;
import com.bmind.example.entity.EmpleadoEntity;

@Component
public class EmpleadosCsvToDB extends RouteBuilder 
{

	@Override
	public void configure() throws Exception {
		from("file:C:/Users/jhohan.hoyos.meneses/OneDrive - Accenture/Desktop?fileName=Empleados.csv")
		//.log("Procesando ${file:name}")
		.unmarshal().bindy(BindyType.Csv, CrearEmpleadoCsvEntity.class)
		.log("body1 CSV : ${body}")
		.split()
		.body()
		.log("CSV ${body.nombres}")
		.process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				CrearEmpleadoCsvEntity vsc = exchange.getIn().getBody(CrearEmpleadoCsvEntity.class);
				exchange.setProperty("vsc", vsc);
			}
		})
		.to("direct:MultiplicarSalario")
		.to("direct:DividirSalario")
		.log("RESPONSE CALCULADORA : ${headers.result}")
		.marshal().json(JsonLibrary.Jackson)
		.unmarshal(new JacksonDataFormat(EmpleadoEntity.class))
		.log("JSON: ${body}")
		.to("sql:classpath:sql/insert-data.sql")
		.end();
	
		from("direct:MultiplicarSalario")
		.routeId("calculadora-salario-route")
		.process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				CrearEmpleadoCsvEntity csv =exchange.getIn().getBody(CrearEmpleadoCsvEntity.class);
				exchange.getIn().setHeader("salario", (int) csv.getSalario());
			}
		})
		.log("${headers.salario}")
		.to("velocity://file:src/main/resources/templates/multiply-request.xml?contentCache=true")
		.removeHeader("*")
		.setHeader(Exchange.CONTENT_TYPE, constant("text/xml;charset=UTF-8"))
		.setHeader(Exchange.HTTP_METHOD, simple("POST"))
		.toD("http://www.dneonline.com/calculator.asmx?bridgeEndpoint=true&connectTimeout=3000&socketTimeout=60000")
		.convertBodyTo(String.class)
		.removeHeader("*")
		.setHeader("resulMulti", xpath("//*[local-name()='MultiplyResult']//text()",String.class))
		.end();
		
		from("direct:DividirSalario")
		.routeId("calculadora-salarioTotal-route")
		.to("velocity://file:src/main/resources/templates/divide-request.xml?contentCache=true")
		.removeHeader("*")
		.setHeader(Exchange.CONTENT_TYPE, constant("text/xml;charset=UTF-8"))
		.setHeader(Exchange.HTTP_METHOD, simple("POST"))
		.toD("http://www.dneonline.com/calculator.asmx?bridgeEndpoint=true&connectTimeout=3000&socketTimeout=60000")
		.convertBodyTo(String.class)
		.removeHeader("*")
		.setHeader("resultDivide", xpath("//*[local-name()='DivideResult']//text()",String.class))
		.process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				Double resultadoTotal = Double.valueOf((String)exchange.getIn().getHeader("resultDivide"));
				
				CrearEmpleadoCsvEntity vsc = (CrearEmpleadoCsvEntity)exchange.getProperties().get("vsc");
				vsc.setSalario(vsc.getSalario()+resultadoTotal);
				exchange.getIn().setBody(vsc);
				
			}
		})
		.end();
	}
	
	
	

}
