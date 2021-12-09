package com.dhmall.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorModel> handleRequestException(MethodArgumentNotValidException exception) {

        StringBuilder errorMessages = new StringBuilder();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            errorMessages.append(errorMessage);
            errorMessages.append(System.getProperty("line.separator"));
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorModel(ErrorCode.CLIENT_VALIDATION_ERROR, errorMessages.toString()));
    }

    @ExceptionHandler(SagoApiException.class)
    public ResponseEntity<ErrorModel> handleApiException(SagoApiException exception) {
        return ResponseEntity.status(exception.getStatus())
                .body(new ErrorModel(exception.getCode(), exception.getMessage()));
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<ErrorModel> handleTokenException(TokenException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorModel(ErrorCode.CLIENT_INVALID_TOKEN_ERROR, exception.getMessage()));
    }
}
