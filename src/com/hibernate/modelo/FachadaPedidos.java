package com.hibernate.modelo;

import java.util.List;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hibernate.dao.ext.PedidosDAOEXT;
import com.hibernate.dto.Pedidos;


/**
 * Fachada para el DAO de Pedidos
 * 
 * @author José F. Bejarano
 * @version 1.0
 * @since 2018
 *
 */

@Component ("fachada_pedidos")
@Scope ("prototype")
public class FachadaPedidos implements I_Pedidos{
	
// LA CLASE "PedidosDAOEXT" HEREDA DEL DAO DE PEDIDOS, ASI Q HEREDA SUS MTDOS
	private PedidosDAOEXT pedido_dao;
	
	
	//ACCESOR PARA SPRING
	public void setPedido_dao(PedidosDAOEXT pedido_dao) {
			this.pedido_dao = pedido_dao;
		}
	
	
//*******************LLAMADA A MTDOS PARA LOS PROCESOS CRUD**************************
		
	//PROCESO DE CONSULTA DE TODOS LOS ARTICULOS
	@Override
	@Transactional (readOnly=true)
	public List<Pedidos> consultarPedidos(){
		//OBTENEMOS Y DEVOLVEMOS LA LISTA DE PEDIDOS LLAMANDO AL MÉTODO DEL DAO	
		return pedido_dao.consultarPedidos();
	}

	//PROCESO DE ALTA
	@Override
	@Transactional 
	public void altaPedido(Pedidos pedido) {	
		pedido_dao.altaPedido(pedido);	
	}
	
	//PROCESO DE BAJA
	@Override
	@Transactional 
	public void bajaPedido(Pedidos pedido) {	
		pedido_dao.bajaPedido(pedido);	
	}
	
	
	// CONSULTA CON CRITERIA DE LA RELACIÓN PEDIDO->LINEAS DE PEDIDOS->ARTICULOS
	@Transactional (readOnly=true)
	public Pedidos consultarConLineasByID(Integer id_pedido) {		
		return pedido_dao.consultarConLineasByID(id_pedido);
	}
	
	@Override
	@Transactional 
	public void modificarPedido (Pedidos pedido){		
		pedido_dao.modificarPedido (pedido);
	}
		
}



