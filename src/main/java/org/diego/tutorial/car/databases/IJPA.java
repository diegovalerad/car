package org.diego.tutorial.car.databases;

import java.util.List;

public interface IJPA {
	public <T> List<T> getAll(Class<T> type);
	
	public <T> T get(Class<T> type, long id);
	
	public <T> T add(T entity);
	
	public <T> T update(T entity);
	
	public <T> T delete(T entity);
}
