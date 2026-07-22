package team6.BW_5.exceptions;

import org.springframework.data.core.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import team6.BW_5.responseDTO.ErrorsDTO;
import team6.BW_5.responseDTO.ErrorsWithListDTO;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorsHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorsDTO handleNotFoundException(NotFoundException e) {
        return new ErrorsDTO(e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(RecordAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsDTO handleRecordAlreadyExsistException(RecordAlreadyExistsException e) {
        return new ErrorsDTO(e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorsDTO handleUnauthorizedException(UnauthorizedException e) {
        return new ErrorsDTO(e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsDTO handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return new ErrorsDTO(e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(PropertyReferenceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsDTO handlePropertyReferenceException(PropertyReferenceException e) {
        return new ErrorsDTO(e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsDTO handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return new ErrorsDTO(message, LocalDateTime.now());

    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsWithListDTO handleValidationException(ValidationException e) {
        return new ErrorsWithListDTO(e.getMessage(), LocalDateTime.now(), e.getErrors());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorsDTO handleGenericException(Exception e) {
        return new ErrorsDTO("Al momento il server non risponde", LocalDateTime.now());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsDTO handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        return new ErrorsDTO(
                "Formato della data non valido. Inserisci la data nel formato europeo GG/MM/AAAA (es. 21/07/2026)",
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorsDTO handleRuntimeException(RuntimeException e) {
        return new ErrorsDTO(e.getMessage(), LocalDateTime.now());
    }


}
