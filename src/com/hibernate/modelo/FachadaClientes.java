package com.hibernate.modelo;

import java.util.List;

import org.jboss.logging.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hibernate.dao.ClientesDAO;
import com.hibernate.dto.Clientes;


/**
 * Fachada para el DAO de Clientes
 * 
 * @author José F. Bejarano
 * @version 1.0
 * @since 2018
 *
 */

@Component("fachada_clientes")
@Scope("prototype")
public class FachadaClientes implements I_Clientes {

	// DAO PARA LOS PROCESOS DE CLIENTE
	private ClientesDAO clientesDAO;
	
	
	
	// ACCESOR PARA SPRING
		public void setCliente_dao(ClientesDAO clientesDAO) {
			this.clientesDAO = clientesDAO;
		}
	
	
	// PROCESOS CRUD PARA LAS INSTANCIAS CLIENTE	
	@Override
	@Transactional
	public void altaCliente(Clientes cliente) {
		clientesDAO.altaCliente(cliente);
	}

	@Override
	@Transactional
	public void bajaCliente(Clientes cliente) {
		clientesDAO.bajaCliente(cliente);
	}

	@Override
	@Transactional
	public void modificacionCliente(Clientes cliente) {
		clientesDAO.modificacionCliente(cliente);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Clientes consultarClienteByID(Integer id) {
		return clientesDAO.consultarClienteByID(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Clientes> consultarClientes(){
		return clientesDAO.consultarClientes();
	}



}
