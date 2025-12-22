package ru.ulstu.is.server.validation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PublicationDateValidator implements ConstraintValidator<PublicationDate, String> {

    private static final String DATE_FORMAT_REGEX = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$";
    private static final Pattern PATTERN = Pattern.compile(DATE_FORMAT_REGEX);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private int minYear;
    private boolean allowFuture;

    @Override
    public void initialize(PublicationDate constraintAnnotation) {
        this.minYear = constraintAnnotation.minYear();
        this.allowFuture = constraintAnnotation.allowFuture();
    }

    @Override
    public boolean isValid(String publicationDate, ConstraintValidatorContext context) {
        if (publicationDate == null || publicationDate.isBlank()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Дата публикации не может быть пустой")
                    .addConstraintViolation();
            return false;
        }

        // Проверка формата YYYY-MM-DD
        if (!PATTERN.matcher(publicationDate).matches()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Неверный формат даты. Используйте YYYY-MM-DD")
                    .addConstraintViolation();
            return false;
        }

        try {
            LocalDate date = LocalDate.parse(publicationDate, DATE_FORMATTER);
            LocalDate today = LocalDate.now();

            // Проверка на будущую дату
            if (!allowFuture && date.isAfter(today)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Дата публикации не может быть в будущем")
                        .addConstraintViolation();
                return false;
            }

            // Проверка на минимальный год
            if (date.getYear() < minYear) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                        String.format("Дата публикации не может быть раньше %d года", minYear))
                        .addConstraintViolation();
                return false;
            }

            // Дополнительная проверка: дата не должна быть слишком старой (более 10 лет)
            if (date.isBefore(today.minusYears(10))) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Дата публикации слишком старая (более 10 лет)")
                        .addConstraintViolation();
                return false;
            }

            return true;

        } catch (DateTimeParseException e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Некорректная дата: " + e.getMessage())
                    .addConstraintViolation();
            return false;
        }
    }
}