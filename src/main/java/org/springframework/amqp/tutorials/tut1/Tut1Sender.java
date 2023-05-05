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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;

/**
 * @author Gary Russell
 * @author Scott Deeg
 */
public class Tut1Sender {

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private DirectExchange exchange;

    @PostConstruct
    public void init() {
        template.setReceiveTimeout(10000L);

    }

    private ObjectMapper objectMapper = new ObjectMapper();
    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send() throws JsonProcessingException {

        Service1Inbound message = new Service1Inbound("John");
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeader("serviceName","service1");
        Message m = new Message(objectMapper.writeValueAsBytes(message), messageProperties);
//        MessageDto2 message2 = new MessageDto2("John", "Doe");


        Message receive = this.template.sendAndReceive(exchange.getName(), "rpc",m);
        System.out.println(new String(receive.getBody()));

//        this.template.convertAndSend(queue.getName(), message2);
        System.out.println(" [x] Sent '" + message + "'");
    }

}

class MessageDto {
    private String name;
    private String family;

    public MessageDto() {
    }

    public MessageDto(String name, String family) {
        this.name = name;
        this.family = family;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }
}

class MessageDto2 {
    private String name;
    private String family;

    public MessageDto2() {
    }

    public MessageDto2(String name, String family) {
        this.name = name;
        this.family = family;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }
}