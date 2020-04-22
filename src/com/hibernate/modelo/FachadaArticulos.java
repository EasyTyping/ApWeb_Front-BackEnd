package com.hibernate.modelo;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hibernate.dao.ArticulosDAO;
import com.hibernate.dto.Articulos;

/** FACHADA DEL DAO ARTICULOS
 * 	@see com.spring.modelo.xml
 */ 

@Component ("fachada_articulo")
@Scope ("prototype")
public class FachadaArticulos implements I_Articulos{
		
	private ArticulosDAO articulo_dao;

	//ACCESOR PARA SPRING
	public void setArticulo_dao(ArticulosDAO articulo_dao) {
			this.articulo_dao = articulo_dao;
		}

//*******************LLAMADA A MTDOS DE PROCESOS CRUD**************************

	//PROCESO DE CONSULTA DE TODOS LOS ARTICULOS
	@Override
	@Transactional (readOnly=true)
	public List<Articulos> consultarArticulos(){
		return articulo_dao.consultarArticulo();
	}
	
	//PROCESO DE ALTA
	@Override
	@Transactional
	public void altaArticulo(Articulos articulo) {	
		articulo_dao.altaArticulo(articulo);	
	}
	
	//PROCESO DE BAJA
	@Override
	@Transactional
	public void bajaArticulo(Articulos articulo) {	
		articulo_dao.bajaArticulo(articulo);	
	}
	
	//PROCESO DE CONSULTA POR ID
	@Override
	@Transactional (readOnly=true)
	public Articulos consultarArtByID(Integer id){
		return articulo_dao.consultarArtByID(id);
	}
	
	//PROCESO MODIFICACIÓN POR ID
	@Override
	@Transactional
	public void modificarArticulo(Articulos articulo){		
		articulo_dao.modificarArticulo(articulo);
	}
	
}
