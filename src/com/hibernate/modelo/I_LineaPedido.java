package com.hibernate.modelo;

import java.util.List;

import com.hibernate.dao.LineaPedidoDAO;
import com.hibernate.dto.LineaPedido;

public interface I_LineaPedido {
	   
		public List<LineaPedido> consultarLineaPedido();
		public LineaPedido consultarLineaByID(Integer id);
		public void altaLinea(LineaPedido linea_nueva);
		public void bajaLinea(LineaPedido linea);
		public List<LineaPedido> consultarLineaQuery(int ref);
		public void modificarLinea(LineaPedido linea);
}
