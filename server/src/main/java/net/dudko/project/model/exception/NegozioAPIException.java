package net.dudko.project.model.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class NegozioAPIException extends RuntimeException {

    public NegozioAPIException(String message) {
        super(message);
    }

}
