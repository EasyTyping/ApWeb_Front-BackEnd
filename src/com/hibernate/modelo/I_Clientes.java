package com.hibernate.modelo;

import java.util.List;

import com.hibernate.dto.Clientes;

/**
* Interface de la fachada para la gestión de clientes
* @author ALVARO
*
*/
public interface I_Clientes {

	public List<Clientes> consultarClientes();
	public Clientes consultarClienteByID(Integer idCliente);
	public void altaCliente(Clientes cliente);
	public void bajaCliente(Clientes cliente);
	public void modificacionCliente(Clientes cliente);

}
