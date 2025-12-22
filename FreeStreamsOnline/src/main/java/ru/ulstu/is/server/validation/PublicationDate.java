package ru.ulstu.is.server.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = PublicationDateValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface PublicationDate {
    String message() default "Некорректная дата публикации";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int minYear() default 2020;

    boolean allowFuture() default false;
}