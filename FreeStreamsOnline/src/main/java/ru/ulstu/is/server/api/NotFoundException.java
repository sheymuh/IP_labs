package ru.ulstu.is.server.api;

public class NotFoundException extends RuntimeException {
    public NotFoundException(Class<?> clazz, int id) {
        super(String.format("%s with id %s is not found", clazz.getSimpleName(), id));
    }
}
