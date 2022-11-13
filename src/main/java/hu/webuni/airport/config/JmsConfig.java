package hu.webuni.airport.config;

import javax.jms.ConnectionFactory;

import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class JmsConfig {
	
	@Bean
	public MessageConverter jacksonJmsMessageConverter(ObjectMapper objectMapper) {
		
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setObjectMapper(objectMapper);
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("_type");
		return converter;
		
	}
	
	//queue esetében, ha a kliens lekapcsolt állapotában érkeznek üzenetek a queue-ba, akkor újraindulást követően a kliens megkapja azokat
	//topic esetében azonban ez csak akkor történik meg, ha a kliens feliratkozása "tartós" (durable)
	//ehhez létre kell hozni egy JmsContainerListenerFactory-t, beállítani, majd a messageConsumer @JmsListener annotációjában beállítani
	
	@Bean
	public JmsListenerContainerFactory<?> myFactory (ConnectionFactory connectionFactory, DefaultJmsListenerContainerFactoryConfigurer configurer) {
		
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		//...úgyhogy a kliens azonosító beállítása átkerült ide:
		((SingleConnectionFactory) connectionFactory).setClientId("customer-service");		
		
		configurer.configure(factory, connectionFactory);
		//tartós feliratkozás bekapcsolása
		factory.setSubscriptionDurable(true);
		//kliens azonosítója, szerver ebből fogja tudni, hogy adott kliens megkapta-e már az üzenetet (egy lekapcsolt állapotot követő újrainduláskor)
		//factory.setClientId("customer-service");
		//de ezt a megoldást nem ette meg :D ...
		return factory;
		
	}
	

}
