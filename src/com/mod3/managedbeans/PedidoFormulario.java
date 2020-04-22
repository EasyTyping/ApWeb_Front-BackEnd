package com.mod3.managedbeans;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.springframework.orm.hibernate4.HibernateOptimisticLockingFailureException;

import com.hibernate.dto.Articulos;
import com.hibernate.dto.LineaPedido;
import com.hibernate.dto.Pedidos;
import com.hibernate.dto.Clientes;
import com.hibernate.modelo.I_Articulos;
import com.hibernate.modelo.I_Pedidos;
import com.hibernate.modelo.I_Clientes;

/**
 *  MANAGEDBEAN DE LA VISTA DEL FORMULARIO DE PEDIDOS
 * 	QUE PERMITE AÑADIR LINEAS DE PEDIDO NUEVAS
 * 
 * @author José F. Bejarano García
 * @version 1.0
 * @since 2019
 * 
 */


@ManagedBean(name = "pedido_formulario")
@ViewScoped
public class PedidoFormulario implements Serializable{
		
	//PARA LOG4
	private static final Logger log = Logger.getLogger(PedidoFormulario.class);

	// FACHADAS DE LA CAPA MODELO 
	@ManagedProperty(value = "#{fachada_pedidos}")
	private I_Pedidos gestion_pedido;
	@ManagedProperty(value = "#{fachada_articulo}")
	private I_Articulos gestion_articulo;
	@ManagedProperty(value = "#{fachada_clientes}")
	private I_Clientes gestion_clientes;
				
	//OBJETO DE LA CLASE DEL DTO
	private Pedidos pedido_dto;
	private LineaPedido linea_dto;
	private Articulos art_dto;
	private Clientes cliente_dto;
	
	// LISTA PARA EL CONTENIDO DE LOS COMBOS
	private List<SelectItem> lista_articulos;
	private List<SelectItem> lista_clientes;
	//LISTA PARA LAS LINEAS DE PEDIDO
	private List<LineaPedido> lista_lineas;
	//Lista para almacenar los totales de las lineas de pedido
	private List <Float> lista_totales;
	
	/**PROPIEDADES BOOLEANAS PARA LOS HABILITAR/DESHABILITAR 
	 * LOS CAMPOS DEL FORMULARIO*/
	private Boolean disabled, requerido, sololectura; 
	
	//OTRAS PROPIEDADES
	private String rb_selected;
	private Float total;
	private HtmlInputText inputext;
	
				
	//CONSTRUCTOR DEL MANAGEDBEAN
	public PedidoFormulario() {		
		log.trace("\n-->Soy el constructor del managedbean pedido_formulario");
		pedido_dto= new Pedidos();
		linea_dto= new LineaPedido();
		art_dto= new Articulos();
		cliente_dto= new Clientes();
		rb_selected="alta";	
		requerido=true;
		lista_lineas= new ArrayList <LineaPedido>();
		lista_totales = new ArrayList <Float>();
		sololectura=false;
		inputext= new HtmlInputText();
		total=0f;
		pedido_dto.setFecha(new Date());
	}

	// PARA CONTROLAR EL CICLO DE VIDA
	@PreDestroy
	public void destruccion() {log.trace("\n-->Managedbean articulo_formulario destruido!!");}
	
	
	//METODO DE CREACION DE LA LISTAS DE LOS COMBOS
	@PostConstruct
	public void crear_Listas() {	
		log.trace("\n-->Soy el metodo de creacion de listas...");
		SelectItem opcion_nueva;
		
		// CONSULTA DE LA LISTA DE LOS CLIENTES A LA BD
		try {
			List<Clientes> lista = gestion_clientes.consultarClientes();
			// LISTA DE OBJS SELECTITEM
			lista_clientes = new ArrayList<>();
			SelectItem op;
			// CARGA DE DATOS EN EL  COMBO
			for (Clientes cliente : lista) {
				op= new SelectItem();
				op.setValue(cliente.getCodigoCliente().toString());
				op.setLabel(cliente.getCodigoCliente().toString());
				lista_clientes.add(op);
			}
		} catch (Exception e) {
			log.error("No se ha podido crear la lista de clientes", e);
		}		
			
		// CONSULTA DE LA LISTA DE ARTICULOS A LA BD
		try {
			List<Articulos> lista2 = gestion_articulo.consultarArticulos();
			// LISTA DE OBJS SELECTITEM
			lista_articulos = new ArrayList<>(0);
			// CARGA DE DATOS EN EL  COMBO
			for (Articulos art : lista2) {
				opcion_nueva = new SelectItem();
				opcion_nueva.setValue(art.getCodigoArticulo().toString());
				opcion_nueva.setLabel(art.getDescripcion());
				lista_articulos.add(opcion_nueva);
			}
		}catch (Exception e) {
			log.error("No se ha podido crear la lista de articulos", e);
		}		
	}
	
	/**METODO QUE HABILITA/DESHABILITA 
	 * LOS CAMPOS EN FUNCIÓN DE LA SELECCIÓN**/
	public void mtoSeleccion(ValueChangeEvent e){
	log.trace("-->Soy el metodo de seleccion del radio button");
	// OBTENEMOS EL VALOR SELECCIONADO DEL EVENTO 
	rb_selected =  e.getNewValue().toString();
	log.trace("\n---->Soy rb seleccionado: " + rb_selected);
	//CAMBIAMOS EL PARAMETRO "DISABLE" DE LOS INPUTTEXT 
		if(rb_selected.equals("baja") || rb_selected.equals("consulta")){					
			log.trace("Entro en deshabilitar");	
			disabled=true;	//parametro disable
			requerido= false;
			sololectura=true;	
		}else{	
			log.trace("Entro en NO deshabilitar");			
			disabled=false;	//parametro disable;
			requerido=true;
			sololectura=false;
		}	
		clear();	
	}

	public void clear(){
		//"Reinicio" la lista de lineas y los pojos
		this.lista_lineas= new ArrayList <LineaPedido>();
		this.pedido_dto=new Pedidos();
		this.cliente_dto=new Clientes();
		pedido_dto.setFecha(new Date());
		total=0f;
	}
	
	//METODO QUE ENVIA LA CONSULTA SELECCIONADA AL DAO
	public void accion(ActionEvent ev){	
		/*Tengo q ponerlo false aqui tb por si se produce 
		 * una excepcion que no vuelva a saltar el mensaje
		 * si hacemos varias consultas seguidas */	
	if(pedido_dto.getTotalFactura()!= 0 && pedido_dto.getTotalFactura()!=null){
		log.trace("\n---->Ejecutando accion");	
		log.trace(pedido_dto.getNumeroPedido());
		switch(rb_selected){
			case "alta":			
				/*Convierto la lista en una colección Set para almacenarla 
				en el DTO*/
				Set<LineaPedido> lineas = new HashSet<LineaPedido>(lista_lineas);
				//Establezco la coleccion de lineas en el pedido
				pedido_dto.setLineaPedido(lineas);
				//Doy de alta al pedido
				gestion_pedido.altaPedido(pedido_dto);		
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage
				(FacesMessage.SEVERITY_INFO,"PEDIDO DADO DE ALTA CON EXITO!!", null));
				break;
			case "baja":
				try{
					//Borro el pedido y sus lineas usando la consulta criteria de pedido con lineas
					gestion_pedido.bajaPedido(gestion_pedido.consultarConLineasByID(pedido_dto.getNumeroPedido()));	
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage (FacesMessage.SEVERITY_INFO,
					"SE HA DADO DE BAJA AL PEDIDO Y LAS LINEAS DE PEDIDO ASOCIADAS A EL", null));
				}catch(IllegalArgumentException ia){	
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage (FacesMessage.SEVERITY_ERROR,
					"EL PEDIDO QUE INTENTA DAR DE BAJA NO EXISTE", null));
					log.error(ia);
				}catch(Exception e){
					error();
					log.error(e);
				}
				break;
			case "modificacion":			
				Set<LineaPedido> lineas2 = new HashSet<LineaPedido>(lista_lineas);
				try{	
					//Establezco la coleccion de lineas en el pedido
					pedido_dto.setLineaPedido(lineas2);	
					gestion_pedido.modificarPedido(pedido_dto);	
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage (FacesMessage.SEVERITY_INFO,
					"EL PEDIDO Y LAS LINEAS ASOCIADAS HAN SIDO MODIFICADAS CON EXITO", null));
				}catch (HibernateOptimisticLockingFailureException hi){	
					log.error("\n--->TE PILLE !!!!!!", hi);
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage (FacesMessage.SEVERITY_ERROR,
					"EL PEDIDO QUE INTENTA MODIFICAR NO EXISTE", null));
				}catch(Exception e){
					log.error(e);
					error();
				}
				break;		
			case "consulta":
				try{		
					//log.trace(pedido_dto.getNumeroPedido());
					pedido_dto=gestion_pedido.consultarConLineasByID(pedido_dto.getNumeroPedido());
					cliente_dto= pedido_dto.getCliente();
					//Tranformo en un List para mostrar las lienas con jsf
					lista_lineas= new ArrayList<LineaPedido> (pedido_dto.getLineaPedido());	
					//Desactivo para presentar el resultado y que se vea claro
					disabled=false;			
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage (FacesMessage.SEVERITY_INFO,
					"EL PEDIDO Y LAS LINEAS  HAN SIDO CARGADAS", null));
				}catch(NullPointerException ep){
					log.error("\n---> EL PEDIDO QUE QUERÍA CONSULTAR NO ESTÁ EN LA BD !!!!!!", ep);	
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage (FacesMessage.SEVERITY_ERROR,
					"EL PEDIDO QUE INTENTA CONSULTAR NO EXISTE", null));
					//Como devuelve un nullpointer hay que limpiar
					clear();
				}catch(Exception e){
					log.error(e);
				}
				break;
		}	
	}else
	 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage (FacesMessage.SEVERITY_WARN,
			 "DEBE AÑADIR AL MENOS UNA LÍNEA DE PEDIDO", null));
	}
	private void error(){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage (FacesMessage.SEVERITY_ERROR,
				"HA HABIDO UN ERROR, NO SE HA PODIDO REALIZAR LA OPERACIÓN", null));}
	
	
	
	//METODOS QUE RESUELVEN EL ARTICULO SELECIONADO Y EL CLIENTE
	public void consultaArtById(ValueChangeEvent evento){
		log.trace("\n-->Soy el metodo de consulta de Articulo");		
		//REALIZAMOS LA CONSULTA A LA BD extrayendo el valor seleccionado (ID del art) del evento				
		linea_dto.setArticulo(gestion_articulo.consultarArtByID((Integer) evento.getNewValue()));
		linea_dto.setPrecioUnidad (linea_dto.getArticulo().getPrecioUnidad());
		log.trace("\n-->Articulo cargado: " + linea_dto.getArticulo().getCodigoArticulo());	
	}
	public void consultaClienteById(ValueChangeEvent evento){
		log.trace("\n-->Soy el metodo de consulta de Cliente");		
		//REALIZAMOS LA CONSULTA A LA BD extrayendo el valor seleccionado (ID del art) del evento
		try{
			pedido_dto.setCliente(gestion_clientes.consultarClienteByID((Integer) evento.getNewValue()));		
		}catch(Exception e){
			log.error("Error al consultar el cliente por su id ", e);				
		}
	}
	
	
	//METODO QUE AÑADE LINEAS DE PEDIDO AL PEDIDO ACTUAL
	public void agregarLinea(ActionEvent ev){		
		//"Vinculo" el pedido a la linea que voy a almacenar en la lista
		linea_dto.setPedido(pedido_dto);
		if(repetidos())
	        log.error("\nYA EXISTE UNA LINEA CON ESE ID!!!");
		else{		
			calcular();
			lista_lineas.add(linea_dto);	
		}	
		//Instancio nuevos objetos ("reinicio" o "limpio")
		this.linea_dto = new LineaPedido();	
		this.art_dto = new Articulos();		
	}

	/**
	 * METODO QUE DETECTA CUALQUIER CAMBIO EN EL CAMPO PORCENTAJE IVA
	 * 	En caso de que haya un valor en el campo y en el total llamara
	 * al mto calcular iva
	 * @param ev
	 */	
	public void hayIva(AjaxBehaviorEvent ev){
		log.trace("\n-----> Cargando iva del pedido");	
		inputext= (HtmlInputText)ev.getSource();
		if (inputext.getValue()!= null && pedido_dto.getTotalFactura()!=null)
			calcularIva();
		else{
			pedido_dto.setTotalFactura(total);
			pedido_dto.setIvaPedido(0.0f);
		}
	}
	
	//METODO QUE CALCULA LOS TOTALES DE CADA LINEA DE PEDIDO Y LOS SUMA
	private void calcular(){
		log.trace("\n--> Soy el metodo calcular los totales");
		//Si no se ha puesto nada en unidades le asignamos el valor 1
		if (linea_dto.getNumeroUnidades() == null)
			linea_dto.setNumeroUnidades(1);
		//Multiplicamos la cantidad por el precio
		total= linea_dto.getPrecioUnidad() * linea_dto.getNumeroUnidades(); 		
		log.trace("1. Total  " + total);
		//Aplicamos el porcentaje de descuento si existe
		if(linea_dto.getDescuento()!= null)
			total = total - (total * (linea_dto.getDescuento() / 100));	
		//Añado el total a la linea
		linea_dto.setTotal(total);
		//Lo almaceno en la lista de totales de las lineas
		lista_totales.add(linea_dto.getTotal());
		//Sumo los totales de la lista de lineas
		log.trace("2. Tamaño lista  " + lista_totales.size());
		if (lista_totales.size()>1){
			total=0f;
			for(int i=0 ; i < lista_totales.size(); i++){
				log.trace (lista_totales.get(i));		
				total = total + lista_totales.get(i);	
			}
		}
		//Si hay algo en el campo porcentaje iva llamo al mto calcular iva
		if (inputext.getValue() != null)		
			calcularIva();
		else
			//Si no hay nada simplemente guardo el resultado en el Total del Pedido
			pedido_dto.setTotalFactura(total);
	}
	
	/*METODO PARA CALCULAR EL IVA DEL TOTAL, 
	 * ES LLAMADO TANTO AL CAMBIAR EL VALOR DEL PORCENTAJE 
	 * COMO AL CALCULAR UN NUEVO TOTAL DE FACTURA
	 */
	private void calcularIva(){		
		log.trace("\n--->> Calculando IVA si el campo tiene algún valor");
		pedido_dto.setIvaPedido(total * ((Float) inputext.getValue() / 100));	
		log.trace("El IVA del pedido en este punto es: " + pedido_dto.getIvaPedido());
		/*Guardo el resultado en una variable auxiliar pq me interesa conservar el total sin iva 
		 * por si quiero operar después, x ej. si cambio el IVA*/
		Float total_con_iva = total + pedido_dto.getIvaPedido();
		pedido_dto.setTotalFactura(total_con_iva);		
		log.trace("TOTAL sin IVA: " + total + " IVA: " + pedido_dto.getIvaPedido() 
		+ " TOTAL con IVA: " + total_con_iva);
}
	
	
	//METODO QUE COMPRUEBA QUE NO HAY ELEMENTOS REPETIDOS EN LA LISTA DE LINEAS
	private boolean repetidos(){
		if(!lista_lineas.isEmpty())
			for(LineaPedido linea_dto2: lista_lineas)
				if(linea_dto2.getCodigoLineaPedido() == linea_dto.getCodigoLineaPedido())
					return true;			
		return false;	
	}

		
	//ACCESORES PARA JSF	
	public Boolean getSololectura() {
		return sololectura;
	}
	public void setSololectura(Boolean sololectura) {
		this.sololectura = sololectura;
	}
	
	public List<LineaPedido> getLista_lineas() {
		return lista_lineas;
	}
	public void setLista_lineas(List<LineaPedido> lista_lineas) {
		this.lista_lineas = lista_lineas;
	}

	public LineaPedido getLinea_dto() {
		return linea_dto;
	}
	public void setLinea_dto(LineaPedido linea_dto) {
		this.linea_dto = linea_dto;
	}

	public List<SelectItem> getLista_articulos() {
		return lista_articulos;
	}
	public void setLista_articulos(List<SelectItem> lista_articulos) {
		this.lista_articulos = lista_articulos;
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

	public Boolean getDisabled() {
		return disabled;
	}
	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
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
	
	public Clientes getCliente_dto() {
		return cliente_dto;
	}
	public void setCliente_dto(Clientes cliente_dto) {
		this.cliente_dto = cliente_dto;
	}
	
	public List<SelectItem> getLista_clientes() {
		return lista_clientes;
	}
	public void setLista_clientes(List<SelectItem> lista_clientes) {
		this.lista_clientes = lista_clientes;
	}

	//ACCESOR PARA SPRING
	public void setGestion_pedido(I_Pedidos gestion_pedido) {
		this.gestion_pedido = gestion_pedido;
	}
	public void setGestion_articulo(I_Articulos gestion_articulo) {
		this.gestion_articulo = gestion_articulo;
	}
	public void setGestion_clientes(I_Clientes gestion_clientes) {
		this.gestion_clientes = gestion_clientes;
	}		
				
}

