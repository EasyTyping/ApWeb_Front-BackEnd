package com.hibernate.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;


import com.hibernate.dto.LineaPedido;


/** 
*						DATA ACCESS OBJECT (DAO)
* Proporciona persistencia y soporte de b�squedas a las entidades LineaPedido.
* Reliza Control de transacciones para las operaciones save(), update() && delete() y
* consultas 
* 
* 
*  @see com.hibernate.dto.LineaPedido
*  @author Jos� Bejarano 
*  @since 2018
*  @version 1.0
*  */


//DEBE TENER EL MISMO NOMBRE Q EL SET PARA EL DAO EN LA FACHADA, ES DECIR, QUE MTDO ACCESOR PARA SPRING 
@Repository("lineaPedido_dao")
@Scope("prototype")
public class LineaPedidoDAO  {
	
	//PROPIEDADES 
  
	private static final Logger log = Logger.getLogger(LineaPedidoDAO.class);
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
	
	protected void initDao() {}
	
	//PROCESOS CRUD
	
	//CONSULTA DE LINEAS DE PEDIDOS
	
		public List<LineaPedido> consultarLineaPedido(){
			log.debug("\n------------>>>>>>>>> Encontrando l�neas de pedido....");
			try {
				return  getCurrentSession().createQuery("from LineaPedido").list();
			} catch (RuntimeException re) {
				log.error("ERROR AL CONSULTAR L�NEA DE PEDIDO ", re);
				throw re;
			}
		}
		
		//CONSULTA BY ID  
		
		public LineaPedido consultarLineaByID(Integer id){
			log.debug("\n------------>>>>>>>>> Cargando instancia L�nea de Pedido con id: " + id);
			try {
				LineaPedido linea = (LineaPedido) getCurrentSession().get(
						"com.hibernate.dto.LineaPedido", id);
				return linea;
			} catch (RuntimeException re) {
				log.error("ERROR ENCONTRANDO LINEA POR SU ID!!!", re);
				throw re;
			}
		}
		
	//	CONSULTA PERSONALIZADA: LINEAS DE PEDIDO CON PRECIO/ARTICULO MAYOR DE 500
		
		public List<LineaPedido> consultarLineaQuery(int ref){
			log.debug("\n------------>>>>>>>>> Encontrando L�neas de Pedido....");
		try {	
			return  getCurrentSession().createQuery("from LineaPedido where precioUnidad" + ref).list();
		} catch (RuntimeException re) {
			log.error("ERROR AL CONSULTAR L�NEA DE PEDIDO ", re);
			throw re;
		}
	}	
	
	
	//ALTA DE LINEA DE PEDIDO
	
		public void altaLinea(LineaPedido lineaNueva){
			log.debug("\n------------>>>>>>>>> Insertando L�nea de Pedido...");
			try{
				/* Aunque la idea es solo dar de alta no uso 'save' a secas pq la BD
				 * es compartida y en caso de q la l�nea de pedido 
				 * existiera nos dar�a un error por consola */
				getCurrentSession().saveOrUpdate(lineaNueva);
			} catch (RuntimeException re) {
				log.error("ERROR INSERTANDO NUEVA LINEA DE PEDIDO", re);
				throw re;
			}
		}
		
	
	
	// BAJA DE LINEA DE PEDIDO 
		
		public void bajaLinea(LineaPedido instanciaPersistente) {			
			try {
				log.debug("\n------------>>>>>>>>> Dando de baja la L�nea de Pedido");
				getCurrentSession().delete(instanciaPersistente);			
			} catch (RuntimeException re) {
				log.error("NO SE HA PODIDO DAR DE BAJA LA L�NEA DE PEDIDO ", re);
				throw re;
			}
		}
		
		
	// MODIFICACI�N L�NEA DE PEDIDO 
		
		public void modificarLinea(LineaPedido instancia){
			try {
				log.debug("\n------------>>>>>>>>> Modificando l�nea de pedido");
				//USAMOS SOLO 'UPDATE', NO NOS INTERESA CREAR NUEVAS L�NEAS EN EL CASO CONCRETO
				getCurrentSession().update(instancia);			
			} catch (RuntimeException re) {
				log.error("NO SE HA PODIDO MODIFICAR LA L�NEA DE PEDIDO ", re);
				throw re;
			}	
		}	
}