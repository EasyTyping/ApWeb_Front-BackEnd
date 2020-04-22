package com.hibernate.dto;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * CLASE DE PERSISTENCIA PARA LA TABLA ARTICULOS
 * @author JOSÉ BEJARANO
 * @version 1.0
 * @since 2018
 *
 */

@Entity
@Table(name = "ARTICULOS")
public class Articulos implements java.io.Serializable{
	
	
//************** PROPIEDADES**************** 
	private Integer codigoArticulo;
	private String descripcion;
	private Float precioUnidad;
	private Integer cantidad;
	
	private Set<LineaPedido> lineaPedido = new HashSet(0);
	
//************CONSTRUCTORES****************
	

	public Articulos(){}
 
	public Articulos(Integer codigoArticulo){
		this.codigoArticulo= codigoArticulo;	
	}

	//Constructor mínimo para altas y modificaciones
	public Articulos(Integer codigoArticulo, String descripcion){
		 this.codigoArticulo= codigoArticulo;
		 this.descripcion= descripcion;
	}
	
	//Constructor completo
	public Articulos(Integer codigoArticulo, String descripcion, 
			Float precioUnidad, Integer cantidad, 
			Set<LineaPedido> lineaPedido){
		
		 this.codigoArticulo= codigoArticulo;
		 this.descripcion=descripcion;
		 this.precioUnidad=precioUnidad;
		 this.cantidad = cantidad;
		 this.lineaPedido = lineaPedido;		
	}
	
	
//**********GETTERS & SETTERS***************
	
	@Id
	@Column(name = "CODIGO_ARTICULO", unique = true, nullable = false, precision=5)
	public Integer getCodigoArticulo() {
		return codigoArticulo;
	}
	
	public void setCodigoArticulo(Integer codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}
	
	@Column(name = "DESCRIPCION_ARTICULO", length=40)
	public String getDescripcion() {
		return descripcion;
	}	
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Column(name = "PRECIO_UNIDAD_ARTICULO", precision = 11, scale=2)
	public Float getPrecioUnidad() {
		return precioUnidad;
	}
	public void setPrecioUnidad(Float precioUnidad) {
		this.precioUnidad = precioUnidad;
	}
	
	@Column(name = "CANTIDAD", precision = 5)
	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
		}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "articulo")
	public Set<LineaPedido> getLineaPedido() {
		return lineaPedido;
	}
	
	public void setLineaPedido(Set<LineaPedido> lineaPedido) {
		this.lineaPedido = lineaPedido;
	}

}
