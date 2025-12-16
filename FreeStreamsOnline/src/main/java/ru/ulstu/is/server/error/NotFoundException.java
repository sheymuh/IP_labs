package ru.ulstu.is.server.error;

public class NotFoundException extends RuntimeException {
    public <T> NotFoundException(Class<T> entClass, Long id) {
        super(String.format("%s with id %s is not found", entClass.getSimpleName(), id));
    }

    public <T> NotFoundException(Class<T> entClass, Long id1, Long id2) {
        super(String.format("%s with id [%s, %s] is not found", entClass.getSimpleName(), id1, id2));
    }
}
