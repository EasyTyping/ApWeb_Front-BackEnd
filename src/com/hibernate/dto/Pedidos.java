package com.hibernate.dto;

import java.util.Date;
import java.util.HashSet;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javax.persistence.CascadeType;
import com.hibernate.dto.Clientes;

/**
 * CLASE DE PERSISTENCIA PARA LA TABLA 'PEDIDOS'
 * 
 * PATRÓN JAVABEAN
 * @author José Bejarano
 * @version 1.0
 * @since 2018
 * 
 */
@Entity
@Table(name = "PEDIDOS")
public class Pedidos implements java.io.Serializable {
	
	
//************** PROPIEDADES ********************
	
	private Integer numeroPedido;
	private Date fecha;

	private Float porcentajeIva;
	private Float ivaPedido;
	private Float totalFactura;
	private Clientes cliente;
	
	
	private Set<LineaPedido> lineaPedido = new HashSet(0);
	
	
//****************** CONSTRUCTORES ***************
		
	public Pedidos() {}
		
	public Pedidos(Integer numeroPedido){
		this.numeroPedido = numeroPedido;
	}
	
	public Pedidos(Integer numeroPedido, Date fecha, Clientes cliente) {
		this.numeroPedido = numeroPedido;
		this.fecha=fecha;
		this.cliente= cliente;
	}
	
	
//************* GETTERS & SETTERS ***************	
	
	@OneToMany(fetch = FetchType.LAZY, cascade= CascadeType.ALL, mappedBy = "pedido")
	public Set<LineaPedido> getLineaPedido() {
		return lineaPedido;
	}
	
	public void setLineaPedido(Set<LineaPedido> lineaPedido) {
		this.lineaPedido = lineaPedido;
	}

	@Id
	@Column(name = "NUMERO_PEDIDO", unique = true, nullable = false, precision=5)
	public Integer getNumeroPedido() {
		return numeroPedido;
	}
	public void setNumeroPedido(Integer numeroPedido) {
		this.numeroPedido = numeroPedido;
	}
	/*Eager para realizar
	la misma operacion varias veces seguidas*/
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CODIGO_CLIENTE")
	public Clientes getCliente() {
		return cliente;
	}
	public void setCliente(Clientes cliente) {
		this.cliente = cliente;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_PEDIDO")
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	@Column(name = "PORCENTAJE_IVA_PEDIDO", precision = 2,  scale=2)
	public Float getPorcentajeIva() {
		return porcentajeIva;
	}
	public void setPorcentajeIva(Float porcentajeIva) {
		this.porcentajeIva = porcentajeIva;
	}
	@Column(name = "IVA_PEDIDO", precision = 11, scale=2)
	public Float getIvaPedido() {
		return ivaPedido;
	}
	public void setIvaPedido(Float ivaPedido) {
		this.ivaPedido = ivaPedido;
	}
	@Column(name = "TOTAL_FACTURA_PEDIDO", precision = 11, scale=2)
	public Float getTotalFactura() {
		return totalFactura;
	}
	public void setTotalFactura(Float totalFactura) {
		this.totalFactura = totalFactura;
	}

}