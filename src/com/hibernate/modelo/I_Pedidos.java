package com.hibernate.modelo;

import java.util.List;

import com.hibernate.dto.Articulos;
import com.hibernate.dto.Pedidos;

public interface I_Pedidos {
	
	public List<Pedidos> consultarPedidos();
	public Pedidos consultarConLineasByID(Integer id_pedido);
	public void altaPedido(Pedidos pedido);
	public void bajaPedido(Pedidos pedido);	
	public void modificarPedido (Pedidos pedido);
}