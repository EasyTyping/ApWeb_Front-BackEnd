package com.hibernate.modelo;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hibernate.dao.LineaPedidoDAO;
import com.hibernate.dto.LineaPedido;


@Component ("fachada_linea_pedido")
@Scope ("prototype")
public class FachadaLineaPedido implements I_LineaPedido{
	// DAO DE ACCESO A ARTICULOS
	private LineaPedidoDAO lineaPedido_DAO;
	
	
   //ACCESOR PARA SPRING	
	public void setLineaPedido_dao(LineaPedidoDAO lineaPedido_DAO) {
		this.lineaPedido_DAO = lineaPedido_DAO;}
	
	
	//CONSULTA DE LINEAS DE PEDIDO
	@Override
	@Transactional (readOnly=true)
	public List<LineaPedido> consultarLineaPedido(){
	 return lineaPedido_DAO.consultarLineaPedido();
	}
	
	//CONSULTA BY ID  
	@Override
	@Transactional (readOnly=true)
	public LineaPedido consultarLineaByID(Integer id){
		return lineaPedido_DAO.consultarLineaByID(id);
	}


	//ALTA
	@Override
	@Transactional 
	public void altaLinea(LineaPedido linea_nueva){
		lineaPedido_DAO.altaLinea(linea_nueva);}
	
	//BAJA 
	@Override
	@Transactional 
	public void bajaLinea(LineaPedido linea){
		lineaPedido_DAO.bajaLinea(linea);}
	
	// CONSULTA BY PROPERTY
	@Override
	@Transactional (readOnly=true)
	public List<LineaPedido> consultarLineaQuery(int ref){	
		return lineaPedido_DAO.consultarLineaQuery(ref);
	}
	//MODIFICAR
	@Override
	@Transactional 
	public void modificarLinea(LineaPedido instancia){
		lineaPedido_DAO.modificarLinea(instancia);		
	}


}