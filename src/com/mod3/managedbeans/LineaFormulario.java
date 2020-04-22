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
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateOptimisticLockingFailureException;

import com.hibernate.dto.Articulos;
import com.hibernate.dto.LineaPedido;
import com.hibernate.dto.Pedidos;
import com.hibernate.modelo.I_Articulos;
import com.hibernate.modelo.I_LineaPedido;
import com.hibernate.modelo.I_Pedidos;


/**
 *  MANAGEDBEAN DE LA VISTA DE LINEA DE PEDIDO
 * 
 * @author José F. Bejarano García
 * @version 1.0
 * @since 2018
 * 
 */


@ManagedBean(name = "linea_formulario")
@ViewScoped
public class LineaFormulario implements Serializable{
		
	//PARA LOG4
	private static final Logger log = Logger.getLogger(LineaFormulario.class);

	// FACHADAS DE LA CAPA MODELO 
	@ManagedProperty(value = "#{fachada_linea_pedido}")
	private I_LineaPedido gestion_linea;
	@ManagedProperty(value = "#{fachada_articulo}")
	private I_Articulos gestion_articulo;
	@ManagedProperty(value = "#{fachada_pedidos}")
	private I_Pedidos gestion_pedido;
				
	//OBJETO DE LA CLASE DEL DTO
	private LineaPedido linea_dto;
	private Articulos art_dto;
	private Pedidos pedido_dto;
		
	// LISTA PARA EL CONTENIDO DE LOS COMBOS
	private List<SelectItem> lista_pedidos;
	private List<SelectItem> lista_articulos;
	
	private String rb_selected, mensaje, color;
				
	/**PROPIEDADES BOOLEANAS PARA LOS HABILITAR/DESHABILITAR 
	 * LOS CAMPOS DEL FORMULARIO
	 * La propiedad ¨requerido¨ es para indicar q campos
	 * no son obligatorios y visibilidad para visualizar o no los mensajes del dialog*/
	private Boolean  disabled, noread, requerido; 
					
	// LISTA 
	private List<SelectItem> lista_lineas;
				
	//CONSTRUCTOR DEL MANAGEDBEAN
	public LineaFormulario() {		
		log.trace("\n-->Soy el constructor del managedbean linea_formulario");
		linea_dto=new LineaPedido();
		art_dto= new Articulos();
		pedido_dto=new Pedidos();
		rb_selected="alta";		
		requerido=true;		
	}
		
		// PARA CONTROLAR EL CICLO DE VIDA
		@PreDestroy
		public void destruccion() {
			log.trace("\n-->Managedbean articulo_formulario destruido!!");
		}
		
		//METODOS
		
		//METODOS QUE GENERAN LAS LISTAS EN EL COMBO
		@PostConstruct
		public void crear_Listas() {	
			log.trace("\n-->Soy el metodo de creacion de listas...");
			SelectItem opcion_nueva;
			
			// CONSULTA A LA BASE DE DATOS
		try{
			List<Pedidos> lista = gestion_pedido.consultarPedidos();
			lista_pedidos = new ArrayList<>(0);// Lista de objs SelectItem
			
			// CARGA DE DATOS EN EL  COMBO
			for (Pedidos ped : lista) {
				opcion_nueva = new SelectItem();
				opcion_nueva.setValue(ped.getNumeroPedido().toString());
				opcion_nueva.setLabel(ped.getNumeroPedido().toString());
				lista_pedidos.add(opcion_nueva);	
			}
		}catch(Exception e){
			log.error("\n--->ERROR CREANDO LA LISTA DE PEDIDOS!!!",e);
		}	
			// CONSULTA A LA BASE DE DATOS
		try{
			List<Articulos> lista2 = gestion_articulo.consultarArticulos();
			lista_articulos = new ArrayList<>(0);// Lista de objs SelectItem
			// CARGA DE DATOS EN EL  COMBO
			for (Articulos art : lista2) {
				opcion_nueva = new SelectItem();
				opcion_nueva.setValue(art.getCodigoArticulo().toString());
				opcion_nueva.setLabel(art.getDescripcion());
				lista_articulos.add(opcion_nueva);
			}	
		}catch(Exception e){
			log.error("\n--->ERROR CREANDO LA LISTA DE CLIENTES!!!",e);
		}		
		}	
				
		/**METODO QUE ALMACENA EL RADIOBUTTOM SELECCIONADO Y HABILITA/DESHABILITA 
		 * LOS CAMPOS EN FUNCIÓN DE LA SELECCIÓN**/
		public void mtoSeleccion(ValueChangeEvent e){
		log.trace("\n-->Soy el metodo de seleccion del radio button");	
		// OBTENEMOS EL VALOR SELECCIONADO DEL EVENTO 
		rb_selected =  e.getNewValue().toString();
		log.trace("\n----> Soy rb seleccionado: " + rb_selected);
		//CAMBIAMOS EL PARAMETRO "DISABLE"
			if(rb_selected.equals("baja") || rb_selected.equals("consulta")){					
				log.trace("->Entro en deshabilitar");	
				disabled=true;	//parametro disabled
				requerido=false;
				noread= true;
			}else{	
				log.trace("->Entro en NO deshabilitar");			
				disabled=false;	
				requerido=true;
				noread= false;
			}
			clear();
		}
				
		//METODO DE REINICIO 
		public void clear(){
			this.linea_dto = new LineaPedido();	
			this.art_dto = new Articulos();
			this.pedido_dto = new Pedidos();
		//crear_Listas();
		}
	
	//METODO QUE EJECUTA LA ACCION SELECCIONADA 
	public void accion(ActionEvent ev){		
		log.trace("\n--->Ejecutando la accion");		
		switch(rb_selected){
			case "alta":
				try{
					linea_dto.setPrecioUnidad (linea_dto.getArticulo().getPrecioUnidad());
					gestion_linea.altaLinea(linea_dto);	
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage
					(FacesMessage.SEVERITY_INFO,"LINEA PEDIDO DADA DE ALTA CON EXITO!!", null));
					clear();
					break;
				}catch(DataIntegrityViolationException ex)	{
					log.error("\nYA EXISTE UNA LINEA DE PEDIDO CON ESE ID!!", ex);	
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage (FacesMessage.SEVERITY_ERROR,
					"YA EXISTE UNA LINEA DE PEDIDO CON ESE ID!!", null));
				}catch (Exception e) {
					log.error(e);	
					error();
				}	
				break;	
			case "baja":
				try{					
					gestion_linea.bajaLinea(linea_dto);	
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage (FacesMessage.SEVERITY_INFO,
							"SE HA DADO LA LINEA DE PEDIDO CON EXITO", null));
				}catch (HibernateOptimisticLockingFailureException hi){			
					log.error("\n--->LA LINEA DE PEDIDO NO EXISTE EN LA BD !!!!!!", hi);
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage (FacesMessage.SEVERITY_ERROR,
					"LA LINEA DE PEDIDO NO EXISTE EN LA BD !!!!!!", null));
				}catch(Exception e){
					log.error(e);
					error();
				}	
				break;
			case "modificacion":
				try{	
					linea_dto.setPrecioUnidad (linea_dto.getArticulo().getPrecioUnidad());
					gestion_linea.modificarLinea(linea_dto);
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage (FacesMessage.SEVERITY_INFO,
					"LAS LINEAS ASOCIADAS HAN SIDO MODIFICADAS CON EXITO", null));
				}catch(Exception e){
					log.error(e);
					error();
				}	
				break;	
			case "consulta":
				try{
					linea_dto = gestion_linea.consultarLineaByID(linea_dto.getCodigoLineaPedido());
					art_dto= linea_dto.getArticulo();
					pedido_dto= linea_dto.getPedido();	
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage (FacesMessage.SEVERITY_INFO,
					"LINEA DE PEDIDO HA SIDO CARGADAS", null));
					//Desactivo para presentar el resultado y que se vea claro
					disabled=false;
				}catch(NullPointerException e){
					log.error(e);
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage (FacesMessage.SEVERITY_ERROR,
					"LA LINEA DE PEDIDO QUE QUIERE CONSULTAR NO EXISTE EN LA BD", null));		
				}catch(Exception e){
					log.error(e);
					error();
					clear();			
				}
				break;
		}		
	}		
	public void error(){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage (FacesMessage.SEVERITY_ERROR,
				"HA HABIDO UN ERROR, NO SE HA PODIDO REALIZAR LA OPERACIÓN", null));}
	
		
		//METODOS QUE RESUELVEN LAS CONSULTAS PARA LAS LISTAS
		public void consultaArtById(ValueChangeEvent evento){
			log.trace("\n-->Soy el metodo de consulta de Articulo");		
			//REALIZAMOS LA CONSULTA A LA BD extrayendo el valor seleccionado (ID del art) del evento				
				linea_dto.setArticulo(gestion_articulo.consultarArtByID((Integer) evento.getNewValue()));
				log.trace("\n-->Articulo cargado: " + linea_dto.getArticulo().getCodigoArticulo());	
		}
		public void consultaPedidoById(ValueChangeEvent evento){
			log.trace("\n-->Soy el metodo de consulta de Pedido");		
		//REALIZAMOS LA CONSULTA A LA BD extrayendo el valor seleccionado (ID del art) del evento				
			linea_dto.setPedido (gestion_pedido.consultarConLineasByID((Integer) evento.getNewValue()));
			log.trace("\n-->Pedido cargado: " + linea_dto.getPedido().getNumeroPedido());
		}
				
		//ACCESORES PARA JSF		
		public LineaPedido getLinea_dto() {
			return linea_dto;
		}

		public void setLinea_dto(LineaPedido linea_dto) {
			this.linea_dto = linea_dto;
		}

		public Articulos getArt_dto() {
			return art_dto;
		}

		public void setArt_dto(Articulos art_dto) {
			this.art_dto = art_dto;
		}

		public Pedidos getPedido_dto() {
			return pedido_dto;
		}

		public void setPedido_dto(Pedidos pedido_dto) {
			this.pedido_dto = pedido_dto;
		}

		public List<SelectItem> getLista_pedidos() {
			return lista_pedidos;
		}

		public void setLista_pedidos(List<SelectItem> lista_pedidos) {
			this.lista_pedidos = lista_pedidos;
		}

		public List<SelectItem> getLista_articulos() {
			return lista_articulos;
		}

		public void setLista_articulos(List<SelectItem> lista_articulos) {
			this.lista_articulos = lista_articulos;
		}

		public Boolean getDisabled() {
			return disabled;
		}

		public void setDisabled(Boolean disabled) {
			this.disabled = disabled;
		}

		public List<SelectItem> getLista_lineas() {
			return lista_lineas;
		}

		public void setLista_lineas(List<SelectItem> lista_lineas) {
			this.lista_lineas = lista_lineas;
		}

		public String getRb_selected() {
			return rb_selected;
		}

		public void setRb_selected(String rb_selected) {
			this.rb_selected = rb_selected;
		}
	

		public Boolean getRequerido() {
			return requerido;
		}

		public void setRequerido(Boolean requerido) {
			this.requerido = requerido;
		}
		
		public String getMensaje() {
			return mensaje;
		}

		public void setMensaje(String mensaje) {
			this.mensaje = mensaje;
		}

		public String getColor() {
			return color;
		}

		public void setColor(String color) {
			this.color = color;
		}
		
		public Boolean getNoread() {
			return noread;
		}

		public void setNoread(Boolean noread) {
			this.noread = noread;
		}

		//ACCESORES PARA SPRING
		public void setGestion_linea(I_LineaPedido gestion_linea) {
			this.gestion_linea = gestion_linea;
		}
		
		public void setGestion_articulo(I_Articulos gestion_articulo) {
			this.gestion_articulo = gestion_articulo;
		}

		public void setGestion_pedido(I_Pedidos gestion_pedido) {
			this.gestion_pedido = gestion_pedido;
		}
}

				
				
				
				
				
				
				
						
				