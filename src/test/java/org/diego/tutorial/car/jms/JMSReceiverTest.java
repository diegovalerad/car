package org.diego.tutorial.car.jms;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.diego.tutorial.car.model.Car;
import org.diego.tutorial.car.model.service.CarService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Set of tests for the {@link JMSReceiver} class
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class JMSReceiverTest {
	@InjectMocks
	private JMSReceiver jmsReceiver;
	
	@Mock
	private CarService carService;

	@Test
	public void testOnMessageCreate() throws JMSException {
		Car car = Mockito.mock(Car.class);
		String operationString = JMSOperations.CREATE.toString();
		ObjectMessage message = Mockito.mock(ObjectMessage.class);
		
		Mockito.when(message.getStringProperty(JMSGroups.GLOBAL.toString()))
				.thenReturn(operationString);
		
		Mockito.when(message.getObject())
				.thenReturn(car);
		
		jmsReceiver.onMessage(message);
		
		Mockito.verify(carService)
				.addCar(car);
	}
	
	@Test
	public void testOnMessageUpdate() throws JMSException {
		Car car = Mockito.mock(Car.class);
		String operationString = JMSOperations.UPDATE.toString();
		ObjectMessage message = Mockito.mock(ObjectMessage.class);
		
		Mockito.when(message.getStringProperty(JMSGroups.GLOBAL.toString()))
				.thenReturn(operationString);
		
		Mockito.when(message.getObject())
				.thenReturn(car);
		
		jmsReceiver.onMessage(message);
		
		Mockito.verify(carService)
				.updateCar(car);
	}
	
	@Test
	public void testOnMessageDelete() throws JMSException {
		Car car = Mockito.mock(Car.class);
		String operationString = JMSOperations.DELETE.toString();
		ObjectMessage message = Mockito.mock(ObjectMessage.class);
		
		Mockito.when(message.getStringProperty(JMSGroups.GLOBAL.toString()))
				.thenReturn(operationString);
		
		Mockito.when(message.getObject())
				.thenReturn(car);
		
		Mockito.when(car.getId())
				.thenReturn(1L);
		
		jmsReceiver.onMessage(message);
		
		Mockito.verify(carService)
				.removeCar(car.getId());
	}
	
	@Test(expected = ClassCastException.class)
	public void testOnMessageWrongMessageClass() {
		TextMessage message = Mockito.mock(TextMessage.class);
		
		jmsReceiver.onMessage(message);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testOnMessageWrongStringProperty() throws JMSException {
		ObjectMessage message = Mockito.mock(ObjectMessage.class);
		
		Mockito.when(message.getStringProperty(Mockito.anyString()))
				.thenReturn(Mockito.anyString());
		
		jmsReceiver.onMessage(message);
	}
	
	@Test(expected = ClassCastException.class)
	public void testOnMessageWrongObject() throws JMSException {
		String operationString = JMSOperations.CREATE.toString();
		ObjectMessage message = Mockito.mock(ObjectMessage.class);
		
		Mockito.when(message.getStringProperty(JMSGroups.GLOBAL.toString()))
				.thenReturn(operationString);
		
		Mockito.when(message.getObject())
				.thenReturn(Mockito.anyString());
		
		jmsReceiver.onMessage(message);
	}
	
	

}
