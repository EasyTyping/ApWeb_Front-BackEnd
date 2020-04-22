package com.hibernate.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;


import com.hibernate.dto.Clientes;

/** 
* ****************** DATA ACCESS OBJECT (DAO) *********************
* 
* Proporciona persistencia y soporte de búsquedas a las entidades Clientes.
* Reliza Control de transacciones para las operaciones save(), update() && delete() 
* 
* 
*  @see com.hibernate.dto.Clientes
*  @author José Bejarano */

@Repository("cliente_dao")
@Scope("prototype")
public class ClientesDAO {
	
	
	//PROPIEDADES 		
	private static final Logger log = Logger.getLogger(ClientesDAO.class);
	private SessionFactory sessionFactory;
	
	//MÉTODO ACCESOR DEL SESSION FACTORY PARA SPRING
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	//MÉTODO QUE DEVUELVE LA SESIÓN ACTUAL
	protected Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
	
	protected void initDao() {}
	
	
	/** 
	 * *********** MÉTODO PARA CONSULTAR LA LISTA DE CLIENTES ************
	 * @return List<Clientes>
	 */
	public List<Clientes> consultarClientes() {		
		log.debug("\n------------>>>>>>>>> Encontrando todas las instancias de clientes");
		try {		
			return (List<Clientes>) getCurrentSession().createQuery("from Clientes order by codigoCliente").list();
		} catch (RuntimeException re) {
			log.error("ERROR ENCONTRANDO LOS ARTICULOS!!!", re);
			throw re;
		}	
	}

	//MÉTODO QUE DA DE ALTA UN NUEVO CLIENTE 	
		public void altaCliente(Clientes cliente) {
			log.debug("saving Clientes instance");
			try {
				getCurrentSession().saveOrUpdate(cliente);
				log.debug("\n-->EL CLIENTE HA SIDO DADO DE ALTA CON EXITO");
			} catch (RuntimeException re) {
				log.error("save failed", re);
				throw re;
			}
		}
	//METODO PARA DAR DE BAJA
	public void bajaCliente(Clientes persistentInstance) {
		log.debug("\n------------>>>>>>>>> Borrando instancia de Cliente");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("\n-->EL CLIENTE HA SIDO BORRADO CON EXITO");
		} catch (RuntimeException re) {
			log.error("ERROR BORRANDO EL CLIENTE!!!", re);
		}
	}

	// MÉTODO DE CONSULTA DE ARTÍCULO POR SU ID 
		public Clientes consultarClienteByID(Integer id){
			log.debug("\n------------>>>>>>>>> Cargando instancia de Cliente con id: " + id);
			try {
				Clientes client = (Clientes) getCurrentSession().get(
						"com.hibernate.dto.Clientes", id);
				return client;
			} catch (RuntimeException re) {
				log.error("ERROR ENCONTRANDO EL CLIENTE POR SU ID!!!", re);
				throw re;
			}
		}
	
	
    //METODO PARA MODIFICAR
	public void modificacionCliente(Clientes instance) {
		log.debug("\n--->Actualizando el cliente");
		try {
			getCurrentSession().update(instance);
			log.debug("Cliente Actualizado");
		} catch (RuntimeException re){ 
			log.error("ERROR ACTUALIZANDO EL CLIENTE!!!", re);
		}
	}

}