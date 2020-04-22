package com.mod3.managedbeans;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;


import com.hibernate.dto.Pedidos;
import com.hibernate.modelo.I_Pedidos;

/**
 * MANAGEDBEAN DE PARA EL DATATABLE DE ARTICULOS
 * 
 * @author José F. Bejarano
 * @version 1.0
 * @since 2018
 *
 */
@ManagedBean(name="pedidosTabla_bean")
@ViewScoped
public class PedidosDataTable implements Serializable {
	
	
	@ManagedProperty(value="#{fachada_pedidos}")
	private I_Pedidos gestion_pedidos;

	//PROPIEDADES PARA RESOLVER EXPRESIONES DINÁMICAS EN JSF
	private List<Pedidos> lista_pedidos;

	//CONSTRUCTOR
	public PedidosDataTable () {
		System.out.println("-->Soy el constructor del DataTable de Pedidos");
	}

	@PostConstruct
	public void crear_ListaPedidos()  {
		// CARGA DE DATOS EN LA PROPIEDAD DEL MANAGEDBEAN
		lista_pedidos = gestion_pedidos.consultarPedidos();

	}
	
	//ACCESORES PARA JSF
	public List<Pedidos> getLista_pedidos() {
		return lista_pedidos;
	}

	public void setLista_pedidos(List<Pedidos> lista_pedidos) {
		this.lista_pedidos = lista_pedidos;
	}

	// ACCESOR PARA SPRING
	public void setGestion_pedidos(I_Pedidos gestion_pedidos) {
		this.gestion_pedidos = gestion_pedidos;
	}
}


