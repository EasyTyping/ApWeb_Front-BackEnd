package com.hibernate.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hibernate.dto.Pedidos;
/**
 * CLASE DE PERSISTENCIA PARA LA TABLA CLIENTES
 * 
 * Se ha simplificado el POJO incluyendo exclusivamente aquellos campos 
 * de la tabla que se utilizarán en la aplicación
 * 
 * @author JOSÉ BEJARANO
 * @version 1.0
 * @since 2018
 *
 */
@Entity
@Table(name = "CLIENTES")
public class Clientes implements Serializable {

	//PROPIEDADES
	private Integer codigoCliente;
	private String nombreCliente;
	//Este campo no puede ir vacio es nullable = false 
	private String estadoCliente;	
	//Relación One To Many
	private Set<Pedidos> pedidos = new HashSet(0); 

	//Constructores
	public Clientes(){}

	public Clientes(Integer codigoCliente) {
		this.codigoCliente = codigoCliente;
		//le asigno un valor, por defecto no voy a trabajar con este campo
		estadoCliente= "1";  
	}
	
	public Clientes(Integer codigoCliente, String nombreCliente) {
		this.codigoCliente = codigoCliente;
		this.nombreCliente = nombreCliente;
		//le asigno un valor, por defecto no voy a trabajar con este campo
		estadoCliente= "1"; 
	}
	
	//************* GETTERS & SETTERS ***************
	@OneToMany(fetch = FetchType.LAZY, cascade= CascadeType.ALL, mappedBy = "cliente")
	public Set<Pedidos> getPedidos() {
		return pedidos;
	}	
	public void setPedidos(Set<Pedidos> pedidos) {
		this.pedidos = pedidos;
	}
	
	@Id
	@Column(name = "CODIGO_CLIENTE", unique = true, nullable = false, precision=5)
	public Integer getCodigoCliente() {
		return this.codigoCliente;
	}
	public void setCodigoCliente(Integer codigoCliente) {
		this.codigoCliente = codigoCliente;
	}
	
	@Column(name = "NOMBRE_CLIENTE", length=35)
	public String getNombreCliente() {
		return this.nombreCliente;
	}
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	@Column(name = "ESTADO_CLIENTE", length=9)
	public String getEstadoCliente() {
		return this.estadoCliente;
	}

	public void setEstadoCliente(String estadoCliente) {
		this.estadoCliente = estadoCliente;
	}

	
}