package hu.webuni.airport.jms;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import hu.webuni.airport.websocket.DelayMessage;

@Component
public class DelayMessageConsumer {

	//ez a komponens + annotáció intézi, hogy a "delay" nevű queue/topic-ra figyeljen
	@JmsListener(destination = "delay", containerFactory="myFactory")
	public void onDelayMessage(DelayMessage delayMessage) {
		
		System.out.println(delayMessage);
		
	}
	
	//a szerver projektből át kell másolni:
	// - DelayMessage osztály-t
	// - JmsConfig-ot, de abból csak a konverter kell, bróker nem
	
}
