package com.mod3.managedbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.springframework.orm.hibernate4.HibernateOptimisticLockingFailureException;

import com.hibernate.dto.Articulos;
import com.hibernate.modelo.I_Articulos;

/**
 *  MANAGEDBEAN DE LA VISTA DE FORMULARIO
 * 
 * @author José F. Bejarano García
 * @version 1.0
 * @since 2018
 * 
 */
@ManagedBean(name = "articulo_formulario")
@ViewScoped
public class ArticuloFormulario implements Serializable{
	
	
	//PARA LOG4
	private static final Logger log = Logger.getLogger(ArticuloFormulario.class);

	// FACHADAS DE LA CAPA MODELO (Con inyección de dependencia-> Fachada Articulo)
	@ManagedProperty(value = "#{fachada_articulo}")
	private I_Articulos gestion_articulo;
	
	//OBJETO DE LA CLASE DEL DTO
	private Articulos art_dto;
	
	//OTROS 
	public String rb_selected;
	private char euro;
	
	//PROPIEDADES BOOLEANAS PARA LOS HABILITAR/DESHABILITAR LOS CAMPOS DEL FORMULARIO
	private Boolean disabled, requerido;

	// LISTA PARA EL CONTENIDO DEL COMBO
	private List<SelectItem> lista_articulos;
	
	
	//CONSTRUCTOR DEL MANAGEDBEAN
	public ArticuloFormulario() {		
		log.trace("\n-->Soy el constructor del managedbean articulo_formulario");
		art_dto= new Articulos();
		requerido=true;
		rb_selected="alta";			
	}

	//METODO QUE GENERA LA LISTA DE ARTICULOS EN EL COMBO
	@PostConstruct
		public void crear_Lista() {		
			log.trace("\n-->Soy el metodo de creacion de lista");
			// CONSULTA A LA BASE DE DATOS
			try{
				List<Articulos> lista = gestion_articulo.consultarArticulos();
				lista_articulos = new ArrayList<>(0);// Lista de objs SelectItem
				SelectItem opcion_nueva;
				// CARGA DE DATOS EN EL  COMBO
				for (Articulos art : lista) {
					opcion_nueva = new SelectItem();
					opcion_nueva.setValue(art.getCodigoArticulo().toString());
					opcion_nueva.setLabel(art.getDescripcion());
					lista_articulos.add(opcion_nueva);
				}
			}catch(Exception e){
				log.error("\n--->ERROR CREANDO LA LISTA DE ARTICULOS!!!!!!", e);
			}	
		}
	// PARA CONTROLAR EL CICLO DE VIDA
	@PreDestroy
	public void destruccion() {
		log.trace("\n-->Managedbean articulo_formulario destruido!!");
	}
	
		
	//METODOS
	/**METODO QUE ALMACENA EL RADIOBUTTOM SELECCIONADO Y HABILITA/DESHABILITA 
	 * LOS CAMPOS EN FUNCIÓN DE LA SELECCIÓN**/
	public void mtoSeleccion(ValueChangeEvent e){
	log.trace("\n---> Soy el metodo de seleccion del radio button");
	// OBTENEMOS EL VALOR SELECCIONADO DEL EVENTO 
	rb_selected = e.getNewValue().toString();
	log.trace("Soy rb seleccionado " + rb_selected);
	//CAMBIAMOS EL PARAMETRO "DISABLE" DE LOS INPUTTEXT 
		if(rb_selected.equals("baja")){					
			log.trace("\n->Entro en deshabilitar");	
			disabled=true;	//parametro disable
			requerido=false; //parametro require
		}else{	
			log.trace("\n->Entro en NO deshabilitar");			
			disabled=false;		//parametro disable
			requerido=true;//parametro required				
		}	
		this.art_dto = new Articulos();
	}
			
	//METODO QUE RESUELVE LAS CONSULTAS		
	public void consultaById(ValueChangeEvent evento){
		log.trace("\n-->Soy el metodo de consulta");	
		if (evento.getNewValue()!= null){
		//Realizamos la consulta a la bd extrayendo el valor seleccionado (id del art) del evento			
			art_dto  = gestion_articulo.consultarArtByID((Integer) evento.getNewValue());
		//El else es solo x si no selecciona ningun articulo sino la opcion por defecto
		}else{
			this.art_dto=new Articulos();
		}	
	}

	//METODO QUE EJECUTA LA ACCION 
	public void accion(ActionEvent ev){		
		log.trace("\n---> Soy el metodo de accion");
		try{
		switch(rb_selected){
			case "alta":			
				gestion_articulo.altaArticulo(art_dto);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage
				(FacesMessage.SEVERITY_INFO,"ARTICULO DADO DE ALTA CON EXITO!!", null));
				break;
			case "baja":
					gestion_articulo.bajaArticulo(art_dto);	
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage (FacesMessage.SEVERITY_INFO,
					"SE HA DADO DE BAJA EL ARTICULO Y LAS LINEAS DE PEDIDO ASOCIADAS A EL", null));
				break;
			case "modificacion":
				try{
					gestion_articulo.modificarArticulo(art_dto);
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage (FacesMessage.SEVERITY_INFO,
					"EL ARTICULO Y LAS LINEAS ASOCIADAS HAN SIDO MODIFICADAS CON EXITO", null));
				}catch(HibernateOptimisticLockingFailureException e){
					log.error(e);
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage (FacesMessage.SEVERITY_ERROR,
					"EL ARTICULO QUE INTENTA MODIFICAR NO EXISTE", null));
				}
				break;			
		}		
			}catch(Exception e){
				log.error(e);
				error();
			}
		/*Después de cualquier operación actualizo la lista 
		y creo un nuevo obj para trabajar, dejando el anterior al colector de basura*/ 
		crear_Lista();
		art_dto = new Articulos();
	}		
	public void error(){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage (FacesMessage.SEVERITY_ERROR,
				"HA HABIDO UN ERROR, NO SE HA PODIDO REALIZAR LA OPERACIÓN", null));
	}
							
	// ACCESORES PARA JSF
	public String getRb_selected() {
		return rb_selected;
	}
	
	public void setRb_selected(String rb_selected) {
		this.rb_selected = rb_selected;
	}
	
	public Articulos getArt_dto() {
		return art_dto;
	}

	public void setArt_dto(Articulos art_dto) {
		this.art_dto = art_dto;
	}
			
	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public Boolean getRequerido1() {
		return requerido;
	}
	public void setRequerido1(Boolean requerido) {
		this.requerido = requerido;
	}

	public char getEuro() {
		return euro;
	}

	public void setEuro(char euro) {
		this.euro = euro;
	}

	public List<SelectItem> getLista_articulos() {
		return lista_articulos;
	}

	public void setLista_articulos(List<SelectItem> lista_articulos) {
		this.lista_articulos = lista_articulos;
	}

	public I_Articulos getGestion_articulo() {
		return gestion_articulo;
	}

	public Boolean getRequerido() {
		return requerido;
	}

	public void setRequerido(Boolean requerido) {
		this.requerido = requerido;
	}

	// Accesor para spring
	public void setGestion_articulo(I_Articulos gestion_articulo) {
		this.gestion_articulo = gestion_articulo;
	}

}
		