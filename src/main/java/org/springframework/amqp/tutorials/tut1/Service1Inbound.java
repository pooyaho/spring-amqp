package org.springframework.amqp.tutorials.tut1;

public class Service1Inbound {
    private String name;

    public Service1Inbound() {
    }

    public Service1Inbound(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
