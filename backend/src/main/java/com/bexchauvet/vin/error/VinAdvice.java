package com.bexchauvet.vin.error;

import com.bexchauvet.vin.error.dto.ErrorDTO;
import com.bexchauvet.vin.error.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;

@ControllerAdvice
public class VinAdvice {
    @ExceptionHandler(value = {BadLoginUnauthorizedException.class, UsernameNotFoundException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    protected ResponseEntity<ErrorDTO> handleUnauthorized(RuntimeException ex) {
        return new ResponseEntity<>(new ErrorDTO(ex.getMessage(), new ArrayList<>()),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {SearchWineDTOBadRequestException.class, WishListDTOBadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    protected ResponseEntity<ErrorDTO> handleBadRequest(RuntimeException ex) {
        return new ResponseEntity<>(new ErrorDTO(ex.getMessage(), new ArrayList<>()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {WineNotFoundException.class, WishListNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    protected ResponseEntity<ErrorDTO> handleNotFound(RuntimeException ex) {
        return new ResponseEntity<>(new ErrorDTO(ex.getMessage(), new ArrayList<>()),
                HttpStatus.NOT_FOUND);
    }


}
