package lk.ijse.gdse.traditionalexperiencebackend.advisor;

import lk.ijse.gdse.traditionalexperiencebackend.util.ResponseUtil;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseUtil> handleGenericRunTimeException(Exception e) {
        ResponseUtil responseUtil = new ResponseUtil();
        responseUtil.setCode(500);
        responseUtil.setMsg(e.getMessage());
        return new ResponseEntity<>(responseUtil, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IdNotFoundException.class)
    public ResponseEntity<ResponseUtil> handleIdNotFoundException(MethodArgumentNotValidException e) {
        ResponseUtil responseUtil = new ResponseUtil();
        responseUtil.setCode(500);
        responseUtil.setMsg(e.getMessage());
        return new ResponseEntity<>(responseUtil, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(IdAllReadyExist.class)
    public ResponseEntity<ResponseUtil> handleIdAllReadyExist(IdAllReadyExist e) {
        ResponseUtil responseUtil = new ResponseUtil();
        responseUtil.setCode(500);
        responseUtil.setMsg(e.getMessage());
        return new ResponseEntity<>(responseUtil, HttpStatus.FOUND);
    }

}
