package com.example.demo;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
public class ProducerDemoApplication {

	private static final String BOOTSTRAP_SERVERS = "localhost:9092";
	private static final String TOPIC = "order";

	@Autowired
	KafkaTemplate<String, Order> KafkaJsontemplate;
	String TOPIC_NAME = "order";
	
	public static void main(String[] args) {
		SpringApplication.run(ProducerDemoApplication.class, args);

		int count = 20;
		CountDownLatch latch = new CountDownLatch(count);

		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
