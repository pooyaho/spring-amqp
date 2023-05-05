package org.springframework.amqp.tutorials.tut1;

import org.springframework.stereotype.Service;

@Service
@Supports("service1")
public class Service1 extends MessageHandler<Service1Inbound,Service1Outbound>{
    @Override
    public Service1Outbound execute(Service1Inbound input) {
        Service1Outbound service1Outbound = new Service1Outbound();
        service1Outbound.setResponse("Hello " + input.getName());
        return service1Outbound;
    }
}
