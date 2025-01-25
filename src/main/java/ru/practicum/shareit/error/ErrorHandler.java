package ru.practicum.shareit.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.NotAllowedException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(final NotFoundException e) {
        log.error("Объект не найден");
        return new ErrorResponse("Not Found", e.getMessage()
        );
    }

    @ExceptionHandler({ValidationException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(final Exception e) {
        log.error("Вылидация не пройдена");
        return new ErrorResponse("Bad Request", e.getMessage()
        );
    }

    @ExceptionHandler({NotAllowedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleForbidden(final Exception e) {
        log.error("Запрещено изменение");
        return new ErrorResponse("Forbidden", e.getMessage()
        );
    }

    @ExceptionHandler({ConflictException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflict(final Exception e) {
        log.error("Ошибка добавления в память");
        return new ErrorResponse("Conflict", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(final Exception e) {
        log.error("Ошибка");
        return new ErrorResponse("Error", e.getMessage()
        );
    }


}
