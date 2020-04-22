package com.hibernate.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hibernate.dto.Articulos;
import com.hibernate.dto.Pedidos;



/**
 * CLASE DE PERSISTENCIA PARA LA TABLA 'LINEA_PEDIDO'
 * @author José Bejarano
 * @version 1.0
 * @since 2018
 *
 */


@Entity
@Table(name = "LINEA_PEDIDO")
public class LineaPedido implements java.io.Serializable {

//************** PROPIEDADES ********************
	
		private Integer codigoLineaPedido;
		private Float precioUnidad;
		private Integer numeroUnidades;
		private Float descuento;
			
		private Float total;//Mirar comentario de la anotación
		
		/*Pueden existir muchas líneas de pedido por cada artículo y por cada número de pedido,
		 * pero cada una de ellas contendrá un solo artículo y un solo número de pedido.
		 * se trata de relaciones  MANY TO ONE*/
		
		private Pedidos pedido;
		private Articulos articulo;

//****************** CONSTRUCTORES ***************
			
		public LineaPedido() {}
		

		public LineaPedido(Integer codigoLineaPedido, Pedidos pedido, Articulos articulo) {		
			this.codigoLineaPedido = codigoLineaPedido;
			this.pedido = pedido;
			this.articulo = articulo;
		}
			
	       public LineaPedido(Integer codigoLineaPedido, Pedidos pedido, Articulos articulo, 
	    		   Float precioUnidad) {
				
				this.codigoLineaPedido = codigoLineaPedido;
				this.pedido=pedido;
				this.articulo = articulo;
				this.precioUnidad= precioUnidad;
			}

		public LineaPedido(Integer codigoLineaPedido, Float precioUnidad, Integer numeroUnidades,
				Float descuento, Pedidos pedido, Articulos articulo) {
			super();
			this.codigoLineaPedido = codigoLineaPedido;
			this.precioUnidad = precioUnidad;
			this.numeroUnidades = numeroUnidades;
			this.descuento = descuento;
			this.pedido = pedido;
			this.articulo = articulo;
		
		}

		//************* GETTERS & SETTERS ***************
		/**Uso EAGER (solo son dos objetos) para trabajar comodo con el lado del 
		 * cliente
		 */
		@ManyToOne(fetch = FetchType.EAGER) 
		@JoinColumn(name = "NUMERO_PEDIDO")
		public Pedidos getPedido() {
			return pedido;
		}

		public void setPedido(Pedidos pedido) {
			this.pedido = pedido;
		}
		
	
		@ManyToOne(fetch = FetchType.EAGER)
		@JoinColumn(name = "CODIGO_ARTICULO")
		public Articulos getArticulo() {
			return articulo;
		}

		public void setArticulo(Articulos articulo) {
			this.articulo = articulo;
		}

		@Id
		@Column(name = "CODIGO_LINEA_PEDIDO", unique = true, nullable = false, precision=10)
		public Integer getCodigoLineaPedido() {
			return codigoLineaPedido;
		}

		public void setCodigoLineaPedido(Integer codigoLineaPedido) {
			this.codigoLineaPedido = codigoLineaPedido;
		}

		@Column(name = "PRECIO_UNIDAD_ARTICULO", precision = 11, scale = 2)
		public Float getPrecioUnidad() {
			return precioUnidad;
		}

		public void setPrecioUnidad(Float precioUnidad) {
			this.precioUnidad = precioUnidad;
		}
		@Column(name = "NUMERO_UNIDADES_ARTICULO", precision = 5)
		public Integer getNumeroUnidades() {
			return numeroUnidades;
		}

		public void setNumeroUnidades(Integer numeroUnidades) {
			this.numeroUnidades = numeroUnidades;
		}
		@Column(name = "PORCENTAJE_DESCUENTO", precision = 4, scale=2)
		public Float getDescuento() {
			return descuento;
		}

		public void setDescuento(Float descuento) {
			this.descuento = descuento;
		}
		
		/*Ttransient para q hibernate lo ignore y no lo mapee, 
		 * se usará en los managedbean para calcular el total de linea */
		@Transient
		public Float getTotal() {
			return total;
		}

		public void setTotal(Float total) {
			this.total = total;
		}
		
		
}
