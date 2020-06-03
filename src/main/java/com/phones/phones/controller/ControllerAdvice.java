package com.phones.phones.controller;

import com.phones.phones.dto.ErrorDto;
import com.phones.phones.exception.call.CallNotExistException;
import com.phones.phones.exception.city.CityAlreadyExistException;
import com.phones.phones.exception.city.CityNotExistException;
import com.phones.phones.exception.invoice.InvoiceNotExistException;
import com.phones.phones.exception.line.LineNotExistException;
import com.phones.phones.exception.line.LineNumberAlreadyExistException;
import com.phones.phones.exception.province.ProviceAlreadyExistException;
import com.phones.phones.exception.province.ProvinceNotExistException;
import com.phones.phones.exception.user.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ValidationException;
import java.text.ParseException;

@RestControllerAdvice
public class ControllerAdvice extends ResponseEntityExceptionHandler {

    /* City exceptions */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CityAlreadyExistException.class)
    public ErrorDto handleCityAlreadyExistException(CityAlreadyExistException e) {
        return new ErrorDto(1, "City name already exists");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CityNotExistException.class)
    public ErrorDto handleCityNotExistException(CityNotExistException e) {
        return new ErrorDto(2, "City id do not exists");
    }


    /* Invoice exceptions */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(InvoiceNotExistException.class)
    public ErrorDto handleInvoiceNotExistException(InvoiceNotExistException e) {
        return new ErrorDto(3, "Invoice id do not exists");
    }


    /* Line exceptions */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(LineNumberAlreadyExistException.class)
    public ErrorDto handleLineNumberAlreadyExistException(LineNumberAlreadyExistException e) {
        return new ErrorDto(4, "Line number already exists");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(LineNotExistException.class)
    public ErrorDto handleLineNotExistException(LineNotExistException e) {
        return new ErrorDto(5, "Line id do not exists");
    }


    /* Province exceptions */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ProviceAlreadyExistException.class)
    public ErrorDto handleProvinceAlreadyExistException(ProviceAlreadyExistException e) {
        return new ErrorDto(6, "Province name already exists");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProvinceNotExistException.class)
    public ErrorDto handleProvinceNotExistException(ProvinceNotExistException e) {
        return new ErrorDto(7, "Province id do not exists");
    }


    /* User exceptions */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UsernameAlreadyExistException.class)
    public ErrorDto handleUsernameAlreadyExistException(UsernameAlreadyExistException e) {
        return new ErrorDto(8, "Username already exists");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserAlreadyExistException.class)
    public ErrorDto handleUsernameAlreadyExistException(UserAlreadyExistException e) {
        return new ErrorDto(9, "User dni already exists");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserAlreadyDisableException.class)
    public ErrorDto handleUserAlreadyDisableException(UserAlreadyDisableException e) {
        return new ErrorDto(10, "User is already delete");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotExistException.class)
    public ErrorDto handleUserNotExistException(UserNotExistException e) {
        return new ErrorDto(11, "User id do not exists");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ParseException.class)
    public ErrorDto handleParseException() {
        return new ErrorDto(12, "Not valid dates");
    }

    /* User Login exceptions */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UserInvalidLoginException.class)
    public ErrorDto handleLoginException(UserInvalidLoginException e) {
        return new ErrorDto(13, "Invalid login");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public ErrorDto handleValidationException(ValidationException e) {
        return new ErrorDto(14, e.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(UserSessionNotExistException.class)
    public ErrorDto handleUserSessionNotExistException(UserSessionNotExistException e) {
        return new ErrorDto(15, "Token has expired");
    }


    /* Calls exception */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CallNotExistException.class)
    public ErrorDto handleCallNotExistException(CallNotExistException e) {
        return new ErrorDto(16, "Call id do not exists");
    }

}
