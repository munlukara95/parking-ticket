package com.vodafone.parkingticket.exception.handler;

import com.vodafone.parkingticket.exception.AbstractCustomRuntimeException;

import java.util.HashMap;
import java.util.Map;

public class Handlers {

    private static final Map<Class<? extends Throwable>, CustomExceptionHandler> HANDLERS = new HashMap<>();

    static {
        HANDLERS.put(AbstractCustomRuntimeException.class, new CustomRuntimeExceptionHandler());
    }

    public static CustomExceptionHandler getHandler(final Class<? extends Throwable> exceptionClass) {
        if (Exception.class.equals(exceptionClass)) {
            return null;
        }
        final CustomExceptionHandler handler = HANDLERS.get(exceptionClass);
        if (handler == null) {
            return getHandler((Class<? extends Throwable>) exceptionClass.getSuperclass());
        }
        return handler;
    }
}
