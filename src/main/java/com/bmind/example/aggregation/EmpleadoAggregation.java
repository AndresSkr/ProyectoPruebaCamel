package com.bmind.example.aggregation;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

import com.bmind.example.entity.EmpleadoEntity;

public class EmpleadoAggregation implements AggregationStrategy{

	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		
		EmpleadoEntity e = null;
		
		if (oldExchange==null) {
			//e = newExchange.getIn().getBody(EmpleadoEntity.class);
			String json = newExchange.getIn().getBody(String.class);
			newExchange.getIn().setBody(json);
		} else {
			//e = newExchange.getIn().getBody(EmpleadoEntity.class);
			String json = newExchange.getIn().getBody(String.class);
			String text = oldExchange.getIn().getBody(String.class);
			
			newExchange.getIn().setBody(text.concat(json));
		}
		
		return newExchange;
	}
	
	private String getLine(EmpleadoEntity e) {
		
		StringBuilder sb = new StringBuilder();
		
		sb
		.append(e.getNombres())
		.append(";")
		.append(e.getApellidos())
		.append(";")
		.append(e.getDireccion())
		.append(";")
		.append(e.getTelefono())
		.append(";")
		.append(e.getEmail())
		.append(";")
		.append(e.getCargo())
		.append(";")
		.append(e.getSalario())
		.append("|OK\n");;
		
		return sb.toString();
	}

}
