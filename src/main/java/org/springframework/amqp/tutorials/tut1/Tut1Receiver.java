/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.amqp.tutorials.tut1;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Gary Russell
 * @author Scott Deeg
 * @author Wayne Lund
 */
@RabbitListener(queues = "hello")
public class Tut1Receiver {


	@Autowired
	private Queue queue;
	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private List<MessageHandler> handlers;
	private Map<String, MessageHandler> messageHandlerMap=new HashMap<>();
	private ObjectMapper objectMapper = new ObjectMapper();


	@PostConstruct
	public void init() {
		for (MessageHandler handler : handlers) {
			messageHandlerMap.put(handler.getClass().getAnnotation(Supports.class).value(), handler);
		}
	}

	@RabbitListener(queues = "tut.rpc.requests")
	public Message receive(Object in) throws ServiceNotFoundException, IOException {
		if (in instanceof Message) {
			String serviceName = String.valueOf(((Message) in).getMessageProperties().getHeaders().get("serviceName"));
			MessageHandler messageHandler = messageHandlerMap.get(serviceName);
			if (messageHandler == null) {
				throw new ServiceNotFoundException();
			}
			String body = new String(((Message) in).getBody());
			Object o = objectMapper.readValue(body, messageHandler.getTypeI());
			Message message = new Message(objectMapper.writeValueAsBytes(messageHandler.execute(o)),
					new MessageProperties());
			return message;
//			rabbitTemplate.convertAndSend(queue.getName(),message,new CorrelationData(correlationId));
		}
		return null;
	}

//	@RabbitHandler
//	public void receive(Message2 in) {
//		System.out.println(" [x] Received '" + in + "'");
//	}
}
