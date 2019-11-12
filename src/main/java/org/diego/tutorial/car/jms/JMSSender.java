package org.diego.tutorial.car.jms;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.log4j.Logger;
import org.diego.tutorial.car.model.Car;

/**
 * Class that creates a connection to the queue and sends a message
 * through it.
 *
 */
@Stateless
public class JMSSender implements IMessageSender {
	
	@Resource(name = "jms/carsConnectionFactory")
	private ConnectionFactory connectionFactory;
	
	@Resource(mappedName = "jms/carsQueue")
	private Queue queue;
	
	private final static Logger LOGGER = Logger.getLogger(JMSSender.class);
	
	@Override
	public void sendCreateCar(Car car) {
		sendMessage(car, JMSGroups.GLOBAL, JMSOperations.CREATE);
	}
	
	@Override
	public void sendUpdateCar(Car car) {
		sendMessage(car, JMSGroups.GLOBAL, JMSOperations.UPDATE);
	}
	
	@Override
	public void sendRemoveCar(Car car) {
		sendMessage(car, JMSGroups.GLOBAL, JMSOperations.DELETE);
	}
	
	private void sendMessage(Car car, JMSGroups group, JMSOperations operation) {
		Connection connection = null;
		Session session = null;
		try {
			connection = connectionFactory.createConnection();
			session = connection.createSession(true, 0);
			
			ObjectMessage message = session.createObjectMessage(car);
			message.setStringProperty(group.toString(), operation.toString());
			
			MessageProducer producer = session.createProducer(queue);
			producer.send(message);
		}catch (JMSException e) {
			LOGGER.warn("Error in the sender JMS: " + e.getMessage());
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
