package com.hibernate.dao.ext;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;

import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;


import com.hibernate.dao.PedidosDAO;
import com.hibernate.dto.Pedidos;

/**
 * ******* CLASE CON CRITERIA PARA CONSULTAS PERSONALIZADAS ************
 * 				
 * 
 * @author JOSÉ F. BEJARANO GARCÍA
 * @since 2018
 * @see com.hibernate.dao.PedidosDAO
 * @see https://docs.jboss.org/hibernate/orm/3.5/reference/es-ES/html/querycriteria.html
 */


//DEBE TENER EL MISMO NOMBRE Q EL SET PARA EL DAO EN LA FACHADA, ES DECIR, QUE MTDO ACCESOR PARA SPRING 
@Repository("pedido_dao")
@Scope("prototype")
public class PedidosDAOEXT extends PedidosDAO{

/**
 * ** MTDO QUE RESUELVE LA RELACIÓN ENTRE PEDIDOS, LINEAS DE PEDIDO Y ARTICULOS
 *  CONSULTA CON CRITERIA DE LA RELACIÓN PEDIDO->LINEAS DE PEDIDOS->ARTICULOS
 * 
 * @param id_pedido
 * @return Objeto de Persistencia resultante de la consulta
 */
	
	public Pedidos consultarConLineasByID(Integer id_pedido) {
		
		// ABRIMOS LA CONSULTA
		Criteria consulta= getCurrentSession().createCriteria(Pedidos.class);
	
		/* RESOLUCION DE LA RELACION ENTRE ENTIDADES */
		
		//Resolvemos la relación entre Pedido y las líneas de pedido a las q esta asociado
		consulta.setFetchMode("lineaPedido", FetchMode.JOIN); 
		//Resolvemos la relación entre la linea de pedido y la entidad Articulos asociada
		consulta.setFetchMode("lineaPedido.articulo", FetchMode.JOIN);
		// CONDICION DE IGUALDAD POR CLAVE PRIMARIA 'ideq'
		consulta.add(Restrictions.idEq(id_pedido));		
		// RESOLUCION DE PRODUCTO CARTESIANO
		consulta.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		//DEVOLVEMOS EL RESULTADO
		return (Pedidos) consulta.uniqueResult();
		
	}
	
	
	
	
}

