package com.mod3.managedbeans;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;


import com.hibernate.dto.LineaPedido;
import com.hibernate.modelo.I_LineaPedido;


/**
 * MANAGEDBEAN DE PARA EL DATATABLE DE ARTICULOS
 * 
 * @author José F. Bejarano
 * @version 1.0
 * @since 2019
 *
 */
@ManagedBean(name="lineasTabla_bean")
@ViewScoped
public class LineasDataTable implements Serializable {
	
	
	@ManagedProperty(value="#{fachada_linea_pedido}")
	private I_LineaPedido gestion_lineas;

	//PROPIEDADES PARA RESOLVER EXPRESIONES DINÁMICAS EN JSF
	private List<LineaPedido> lista_lineas;

	//CONSTRUCTOR
	public LineasDataTable () {
		System.out.println("-->Soy el constructor del DataTable de LineaPedido");
	}

	@PostConstruct
	public void crear_ListaPedidos()  {
		// CARGA DE DATOS EN LA PROPIEDAD DEL MANAGEDBEAN
		lista_lineas = gestion_lineas.consultarLineaPedido();

	}
	
	//ACCESORES PARA JSF
	public List<LineaPedido> getLista_lineas() {
		return lista_lineas;
	}

	public void setLista_lineas(List<LineaPedido> lista_lineas) {
		this.lista_lineas = lista_lineas;
	}

	// ACCESOR PARA SPRING
	public void setGestion_lineas(I_LineaPedido gestion_lineas) {
		this.gestion_lineas = gestion_lineas;
	}
}


