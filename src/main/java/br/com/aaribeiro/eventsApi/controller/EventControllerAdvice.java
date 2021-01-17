package br.com.aaribeiro.eventsApi.controller;

import br.com.aaribeiro.eventsApi.exception.InvalidPasswordException;
import br.com.aaribeiro.eventsApi.model.ErrorMessageDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class EventControllerAdvice {

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> genericException(Exception e){
        ErrorMessageDTO errorMessageDTO = new ErrorMessageDTO("Exception generic.", LocalDate.now().toString(), e.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessageDTO);
    }

    @ExceptionHandler(value = {InvalidPasswordException.class})
    public ResponseEntity<Object> authenticateException(InvalidPasswordException e){
        ErrorMessageDTO errorMessageDTO = new ErrorMessageDTO("Exception authenticate.", LocalDate.now().toString(), e.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessageDTO);
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public ResponseEntity<Object> validationException(HttpMessageNotReadableException e) {
        ErrorMessageDTO errorMessageDTO = new ErrorMessageDTO(
                "Exception found during validation.", LocalDate.now().toString(), this.returnsMessageAsException(e.getLocalizedMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessageDTO);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Object> validationException(MethodArgumentNotValidException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(this.getErrors(e));
    }

    private List<ErrorMessageDTO> getErrors(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new ErrorMessageDTO("Exception found during validation.", LocalDate.now().toString(), error.getDefaultMessage()))
                .collect(Collectors.toList());
    }

    private String returnsMessageAsException(String error){
        if (error.contains("DateTimeParseException")){
            return "The dateOfEvent Field must be in the format dd/MM/yyyy";
        }
        if (error.contains("MismatchedInputException")){
            return "PATCH object is in incompatible format. Ex.: [{op: replace, path: /name, value: Andrei Antonio Ribeiro}]";
        }
        return "Error converting data.";
    }
}
