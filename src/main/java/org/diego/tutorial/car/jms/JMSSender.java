package org.diego.tutorial.car.jms;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.log4j.Logger;

/**
 * Class that creates a connection to the queue and sends a message
 * through it.
 *
 */
@Stateless
public class JMSSender {
	
	@Resource(name = "jms/carsConnectionFactory")
	private ConnectionFactory connectionFactory;
	
	@Resource(mappedName = "jms/carsQueue")
	private Queue queue;
	
	private final static Logger LOGGER = Logger.getLogger(JMSSender.class);
	
	public void sendMessage(String message) {
		Connection connection = null;
		Session session = null;
		try {
			LOGGER.info("Trying to send a new message: " + message);
			
			connection = connectionFactory.createConnection();
			session = connection.createSession(true, 0);
			MessageProducer producer = session.createProducer(queue);
			
			producer.send(session.createTextMessage(message));
			
			LOGGER.info("Message sent: " + message);
		}catch (JMSException e) {
			LOGGER.info("JMSException: " + e.getMessage());
		}finally {
			try {
				if (connection != null)
					connection.close();
				if (session != null)
					session.close();
			} catch (JMSException e) {
				LOGGER.info("JMSException: " + e.getMessage());
			}
		}
	}

}
