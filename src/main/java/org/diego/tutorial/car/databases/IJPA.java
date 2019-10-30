package org.diego.tutorial.car.databases;

import java.util.List;

import org.diego.tutorial.car.exceptions.DAOException;

public interface IJPA {
	public <T> List<T> getAll(Class<T> type);
	
	public <T> T get(Class<T> type, long id) throws DAOException;
	
	public <T> T add(T entity) throws DAOException;
	
	public <T> T update(T entity);
	
	public <T> T delete(T entity);
}
