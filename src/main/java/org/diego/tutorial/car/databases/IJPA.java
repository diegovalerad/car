package org.diego.tutorial.car.databases;

import java.util.List;

/**
 * Interface with the generic methods to implement by an
 * JPA implementation
 *
 */
public interface IJPA {
	/**
	 * Method that gets all the objects of certain type.
	 * @param <T> Class of the object
	 * @param type Object
	 * @return List of T objects
	 */
	public <T> List<T> getAll(Class<T> type);
	
	/**
	 * Method that get an object of certain type.
	 * @param <T> Class of the object
	 * @param type Object
	 * @param id Identifier of the object to get.
	 * @return T object or null if it does not exist
	 */
	public <T> T get(Class<T> type, Object id);
	
	/**
	 * Method that adds an object to the database
	 * @param <T> Class of the object
	 * @param entity Object to add 
	 * @return Added object 
	 */
	public <T> T add(T entity);
	
	/**
	 * Method that updates an object on the database
	 * @param <T> Class of the object
	 * @param entity Object to update
	 * @return Updated object
	 */
	public <T> T update(T entity);
	
	/**
	 * Method that deletes an object on the database
	 * @param <T> Class of the object
	 * @param entity Object to delete
	 * @return Deleted object
	 */
	public <T> T delete(T entity);
}
