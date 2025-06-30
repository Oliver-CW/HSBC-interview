package hsbc.interview.transaction.exception;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(HttpServletRequest req, Exception e) {
        log.warn("Caught IllegalArgumentException: {}", e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<String> handleTransactionException(HttpServletRequest req, Exception e) {
        log.error("Caught TransactionException:", e);
        return ResponseEntity.internalServerError().body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnknownException(HttpServletRequest req, Exception e) {
        log.error("Caught Exception:", e);
        return ResponseEntity.internalServerError().body(e.getMessage());
    }
}
