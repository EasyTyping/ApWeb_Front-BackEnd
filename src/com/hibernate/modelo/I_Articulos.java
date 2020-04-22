package com.hibernate.modelo;

import java.util.List;

import com.hibernate.dto.Articulos;


public interface I_Articulos {
	
	public List<Articulos> consultarArticulos();
	public void altaArticulo(Articulos articulo);
	public void bajaArticulo(Articulos articulo);
	public Articulos consultarArtByID(Integer id);
	public void modificarArticulo(Articulos articulo);

}
