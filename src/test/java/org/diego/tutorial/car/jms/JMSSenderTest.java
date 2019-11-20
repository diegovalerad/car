package org.diego.tutorial.car.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;

import org.diego.tutorial.car.model.Car;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Set of tests for the {@link JMSSender} class
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class JMSSenderTest {
	
	@InjectMocks
	private JMSSender jmsSender;
	
	@Mock
	private ConnectionFactory connectionFactory;
	@Mock
	private Queue queue;
	
	private static Car car;
	
	private ObjectMessage message;
	private MessageProducer producer;
	
	@BeforeClass
	public static void onlyOnce() {
		car = Mockito.mock(Car.class);
	}
	
	@Before
	public void setUp() throws JMSException{
		Connection connection = Mockito.mock(Connection.class);
		Session session = Mockito.mock(Session.class);
		message = Mockito.mock(ObjectMessage.class);
		producer = Mockito.mock(MessageProducer.class);
		
		Mockito.when(connectionFactory.createConnection())
				.thenReturn(connection);
		Mockito.when(connection.createSession(true, 0))
				.thenReturn(session);
		Mockito.when(session.createObjectMessage(car))
				.thenReturn(message);
		Mockito.when(session.createProducer(queue))
				.thenReturn(producer);
	}

	@Test
	public void testSendCreateCar() throws JMSException {
		jmsSender.sendCreateCar(car);
		
		Mockito.verify(producer)
				.send(message);
	}

	@Test
	public void testSendUpdateCar() throws JMSException {
		jmsSender.sendUpdateCar(car);
		
		Mockito.verify(producer)
				.send(message);
	}
	
	@Test
	public void testSendDeleteCar() throws JMSException {
		jmsSender.sendRemoveCar(car);
		
		Mockito.verify(producer)
				.send(message);
	}
	
	@Test(expected = NullPointerException.class)
	public void testSendNullObject() {
		jmsSender.sendRemoveCar(null);
	}
}
