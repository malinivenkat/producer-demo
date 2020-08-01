/*
 * Copyright (c) 2016-2018 Pivotal Software Inc, All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.demo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.SenderRecord;

/**
 * Sample producer application using Reactive API for Kafka. To run sample
 * producer
 * <ol>
 * <li>Start Zookeeper and Kafka server
 * <li>Update {@link #BOOTSTRAP_SERVERS} and {@link #TOPIC} if required
 * <li>Create Kafka topic {@link #TOPIC}
 * <li>Run {@link SampleProducer} as Java application with all dependent jars in
 * the CLASSPATH (eg. from IDE).
 * <li>Shutdown Kafka server and Zookeeper when no longer required
 * </ol>
 */
public class SampleProducer {

	private static final Logger log = LoggerFactory.getLogger(SampleProducer.class.getName());

	private static final String BOOTSTRAP_SERVERS = "localhost:9092";
	private static final String TOPIC = "order";

	private final KafkaSender<Integer, Order> sender;
	private final SimpleDateFormat dateFormat;
	Map<String, Object> props = new HashMap<>();

	private static int orderNo = 0;

	public SampleProducer(String bootstrapServers) {
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ProducerConfig.CLIENT_ID_CONFIG, "sample-producer");
		props.put(ProducerConfig.ACKS_CONFIG, "all");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, OrderSerializer.class);
		SenderOptions<Integer, Order> senderOptions = SenderOptions.create(props);

		sender = KafkaSender.create(senderOptions);
		dateFormat = new SimpleDateFormat("HH:mm:ss:SSS z dd MMM yyyy");
	}

	public void sendMessages(String topic, int count, CountDownLatch latch) throws InterruptedException {
		sender.<Integer>send(
				Flux.range(1, count).map(i -> SenderRecord.create(new ProducerRecord<>(topic, i, generateOrder()), i)))
				.doOnError(e -> log.error("Send failed", e)).subscribe(r -> {
					RecordMetadata metadata = r.recordMetadata();
					System.out.printf("Message %d sent successfully, topic-partition=%s-%d offset=%d timestamp=%s\n",
							r.correlationMetadata(), metadata.topic(), metadata.partition(), metadata.offset(),
							dateFormat.format(new Date(metadata.timestamp())));
					latch.countDown();
				});
	}

	public void close() {
		sender.close();
	}

	public Order generateOrder() {
		List<Product> pList = new ArrayList<Product>();
		//TODO make this persistable later
		++orderNo;
		Product p1 = new Product("P" + orderNo, new Random().nextInt(10), 2, 3, 3, "E");
		Product p2 = new Product("P" + (orderNo + 100), new Random().nextInt(5), 2, 5, 1, "X");
		pList.add(p1);
		pList.add(p2);
		Order o1 = new Order(orderNo, pList);

		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(o1);
			System.out.println(json);
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

//		return json; 
		return o1;
	}

	public static void main(String[] args) throws Exception {
		int count = 20;
		int loop = 0;
		CountDownLatch latch = new CountDownLatch(count);
		SampleProducer producer = new SampleProducer(BOOTSTRAP_SERVERS);
		while (loop < 1) {
			producer.sendMessages(TOPIC, count, latch);
			latch.await(10, TimeUnit.SECONDS);

			loop++;
			Thread.sleep(5000);
		}
		producer.close();
	}
}