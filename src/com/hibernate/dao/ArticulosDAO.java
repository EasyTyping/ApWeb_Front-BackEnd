package com.hibernate.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.hibernate.dto.Articulos;


/** 
* ****************** DATA ACCESS OBJECT (DAO) *********************
* 
* Proporciona persistencia y soporte de b�squedas a las entidades Articulos.
* Reliza Control de transacciones para las operaciones save(), update() && delete() 
* 
* 
*  @see com.hibernate.dto.Articulos
*  @author Jos� Bejarano */


//Debe tener el mismo nombre q el set para el dao en la fachada, es decir, que mtdo accesor para spring 
@Repository("articulo_dao")
@Scope("prototype")
public class ArticulosDAO {
	
	//Propiedades 
	private static final Logger log = Logger.getLogger(ArticulosDAO.class);
	private SessionFactory sessionFactory;
	
	//M�todo accesor del session factory para spring
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	//M�todo que devuelve la sesi�n actual
	protected Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
	
	protected void initDao() {}
	
	
// PROCESOS CRUD 
	
	/** 
	 * *********** M�TODO PARA CONSULTAR LA LISTA DE ARTICULOS ************
	 * 
	 * @return List<Articulos>
	 */
	
	public List<Articulos> consultarArticulo(){
		
		log.debug("\n------------>>>>>>>>> Encontrando todas las instancias de articulos");
		try {		
			return (List<Articulos>) getCurrentSession().createQuery("from Articulos order by descripcion").list();		
		} catch (RuntimeException re) {
			log.error("ERROR ENCONTRANDO LOS ARTICULOS!!!", re);
			throw re;
		}
	}
	
	// M�TODO DE CONSULTA DE ART�CULO POR SU ID 
	public Articulos consultarArtByID(Integer id){
		log.debug("\n------------>>>>>>>>> Cargando instancia de Articulo con id: " + id);
		try {
			Articulos art = (Articulos) getCurrentSession().get(
					"com.hibernate.dto.Articulos", id);
			return art;
		} catch (RuntimeException re) {
			log.error("ERROR ENCONTRANDO EL ARTICULO POR SU ID!!!", re);
			throw re;
		}
	}

	// M�TODO QUE DA DE ALTA UN NUEVO ART�CULO 
	public void altaArticulo(Articulos IntanciaEnTransito) {
			
		log.debug("\n------------>>>>>>>>> Insertando Art�culo en la BD...");
			try {	
				getCurrentSession().saveOrUpdate(IntanciaEnTransito);
			} catch (RuntimeException re) {
				log.error("NO SE HA PODIDO INSERTAR EL ARTICULO", re);
				throw re;
			}
	}
		
	// M�TODO QUE DA DE BAJA UN ART�CULO 	 
		public void bajaArticulo(Articulos instanciaPersistente) {
		log.debug("\n------------>>>>>>>>> Dando de baja al Art�culo " 
				+ instanciaPersistente.getCodigoArticulo() + " en la BD...");
		try {
			getCurrentSession().delete(instanciaPersistente);	
		} catch (RuntimeException re) {
			log.error("NO SE HA PODIDO DAR DE BAJA EL ARTICULO", re);
			throw re;
		}
	}
	
	// M�TODO DE MODIFICACI�N ART�CULO 
	public void modificarArticulo(Articulos instancia){
		log.debug("\n------------>>>>>>>>> Modificando Art�culo...");
		try {
			getCurrentSession().update(instancia);	
		} catch (RuntimeException re) {
			log.error("NO SE HA PODIDO MODIFICAR EL ARTICULO", re);
			throw re;
		}
	
	}
}
	
	

