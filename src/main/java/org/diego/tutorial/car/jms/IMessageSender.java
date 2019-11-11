package org.diego.tutorial.car.jms;

import javax.ejb.Local;
import javax.jms.JMSException;

/**
 * Interface of the sending bean
 *
 */

public interface IMessageSender {
	/**
	 * Method that sends a message to a queue
	 * @param message Message to send
	 * @throws JMSException Exception thrown if there is any error using the queue
	 */
	void sendMessage(String message);
}
