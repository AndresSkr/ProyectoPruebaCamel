package com.bmind.example.entity;

import javax.persistence.Column;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@CsvRecord(separator ="," )
public class CrearEmpleadoCsvEntity {
	

	@DataField(pos=1)
	private String nombres;
	
	@DataField(pos=2)
	private String apellidos;
	
	@DataField(pos=3)
	private String direccion;
	
	@DataField(pos=4)
	private String telefono;
	
	@DataField(pos=5)
	private String email;
	
	@DataField(pos=6)
	private String cargo;
	
	@DataField(pos=7)
	private double salario;
	
	

	public CrearEmpleadoCsvEntity() {
		super();
	}

	public CrearEmpleadoCsvEntity(String nombres, String apellidos, String direccion, String telefono, String email,
			String cargo, double salario) {
		super();
		this.nombres = nombres;
		this.apellidos = apellidos;
		this.direccion = direccion;
		this.telefono = telefono;
		this.email = email;
		this.cargo = cargo;
		this.salario = salario;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public double getSalario() {
		return salario;
	}

	public void setSalario(double salario) {
		this.salario = salario;
	}
	
	


}
