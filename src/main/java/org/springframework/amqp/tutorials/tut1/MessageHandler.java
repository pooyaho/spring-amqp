package org.springframework.amqp.tutorials.tut1;

import org.springframework.core.GenericTypeResolver;

public abstract class MessageHandler<I,O> {
    private Class<I> typeI = (Class<I>) GenericTypeResolver.resolveTypeArguments(this.getClass(), MessageHandler.class)[0];
    private Class<I> typeO = (Class<I>) GenericTypeResolver.resolveTypeArguments(this.getClass(),
            MessageHandler.class)[1];
    public abstract O execute(I input);

    public Class<I> getTypeI() {
        return typeI;
    }

    public Class<I> getTypeO() {
        return typeO;
    }
}
