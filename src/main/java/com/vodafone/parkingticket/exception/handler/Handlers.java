package com.vodafone.parkingticket.exception.handler;

import com.vodafone.parkingticket.exception.AbstractCustomRuntimeException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolationException;
import javax.validation.UnexpectedTypeException;
import java.util.HashMap;
import java.util.Map;

public class Handlers {

    private static final Map<Class<? extends Throwable>, CustomExceptionHandler> HANDLERS = new HashMap<>();

    static {
        HANDLERS.put(AbstractCustomRuntimeException.class, new CustomRuntimeExceptionHandler());
        HANDLERS.put(ConstraintViolationException.class, new ConstraintViolationExceptionHandler());
        HANDLERS.put(UnexpectedTypeException.class, new UnexpectedTypeExceptionHandler());
        HANDLERS.put(MethodArgumentNotValidException.class, new MethodArgumentNotValidExceptionHandler());
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
