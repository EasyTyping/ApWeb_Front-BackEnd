package com.mod3.managedbeans;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import com.hibernate.dto.Articulos;
import com.hibernate.modelo.I_Articulos;

/**
 * MANAGEDBEAN DE PARA EL DATATABLE DE ARTICULOS
 * 
 * @author José F. Bejarano
 * @version 1.0
 * @since 2018
 *
 */
@ManagedBean(name="articuloTabla_bean")
@ViewScoped
public class ArticuloDataTable implements Serializable {
	
	
	@ManagedProperty(value="#{fachada_articulo}")
	private I_Articulos gestion_articulo;

	//PROPIEDADES PARA RESOLVER EXPRESIONES DINÁMICAS EN JSF
	private List<Articulos> lista_articulos;

	//CONSTRUCTOR
	public ArticuloDataTable () {
		System.out.println("-->Soy el constructor del DataTable");
	}

	@PostConstruct
	public void crear_ListaArticulos()  {
		// CARGA DE DATOS EN LA PROPIEDAD DEL MANAGEDBEAN
		lista_articulos = gestion_articulo.consultarArticulos();

	}
	
	//ACCESORES PARA JSF
	public List<Articulos> getLista_articulos() {
		return lista_articulos;
	}

	public void setLista_articulos(List<Articulos> lista_articulos) {
		this.lista_articulos = lista_articulos;
	}

	// ACCESOR PARA SPRING
	public void setGestion_articulo(I_Articulos gestion_articulo) {
		this.gestion_articulo = gestion_articulo;
	}
}


