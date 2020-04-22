package com.hibernate.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;


import com.hibernate.dto.Pedidos;



/** 
*						DATA ACCESS OBJECT (DAO)
* Proporciona persistencia y soporte de b�squedas a las entidades Pedidos.
* Reliza Control de transacciones para las operaciones save(), update() && delete() 
* 
* 
*  @see com.hibernate.dto.Pedidos
*  @author Jos� Bejarano 
*  @since 2018
*  @version 1.0
*  */


public class PedidosDAO {

//PROPIEDADES 
private static final Logger log = Logger.getLogger(PedidosDAO.class);
private SessionFactory sessionFactory;


//METODO ACCESOR DEL SESSION FACTORY PARA SPRING
public void setSessionFactory(SessionFactory sessionFactory) {
	this.sessionFactory = sessionFactory;
}
//M�TODO QUE DEVUELVE LA SESI�N ACTUAL
protected Session getCurrentSession() {
	return sessionFactory.getCurrentSession();
}

protected void initDao() {}


//******************* MTDOS PARA PROCESOS CRUD **************************

/** 
 * *********** M�TODO PARA CONSULTAR LA LISTA DE PEDIDOS ************
 * 
 * @return List<Pedidos>
 */

@SuppressWarnings("unchecked")
public List<Pedidos> consultarPedidos(){
	log.debug("\n------------>>>>>>>>> Buscando todas las instancias de Pedido\n");
	try {		 
		return (List<Pedidos>) getCurrentSession().createQuery("from Pedidos order by fecha").list();
	} catch (RuntimeException re) {
		log.error("ERROR al buscar las instancias de Pedidos...", re);
		throw re;
	}
}


	
/* *********** M�TODO QUE DA DE ALTA UN NUEVO PEDIDO ************ */

public void altaPedido(Pedidos IntanciaEnTransito) {
		

		try {
			/* Aunque la idea es solo dar de alta, no uso 'save' a secas pq la BD
			 * es compartida y la PK es introducida por el usuario y, por tanto,
			 * en caso de q el pedido existiera nos dar�a un error,
			 * se trata de simplificar las cosas a quien pruebe la aplicaci�n*/
			getCurrentSession().saveOrUpdate(IntanciaEnTransito);
			log.debug("\n------------>>>>>>>>> Pedido " 
			+ IntanciaEnTransito.getNumeroPedido()  + " insertado con �xito");
		} catch (RuntimeException re) {
			log.error("NO SE HA PODIDO INSERTAR EL PEDIDO", re);
			throw re;
		}
}
	
/** *********** M�TODO QUE DA DE BAJA UN PEDIDO ************ */

public void bajaPedido(Pedidos instanciaPersistente) {
	
	try {
		getCurrentSession().delete(instanciaPersistente);
		log.debug("\n------------>>>>>>>>> Pedido " 
		+ instanciaPersistente.getNumeroPedido() + " dado de baja con �xito!!!");
	} catch (RuntimeException re) {
		log.error("NO SE HA PODIDO DAR DE BAJA EL PEDIDO!!!", re);
		throw re;
	}
}

/* *********** M�TODO PARA MODIFICAR UN PEDIDO *************/
public void modificarPedido (Pedidos pedido){

	try {
		//USAMOS SOLO 'UPDATE', NO NOS INTERESA CREAR NUEVOS PEDIDOS
		getCurrentSession().update(pedido);
		log.debug("\n------------>>>>>>>>> Se ha modificado con �xito!!!");		
	} catch (RuntimeException re) {
		log.error("NO SE HA PODIDO MODIFICAR EL PEDIDO!!!", re);
		throw re;
	}
}

}//FIN DE CLASE



		

