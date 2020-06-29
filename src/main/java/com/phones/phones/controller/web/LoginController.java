package com.phones.phones.controller.web;

import com.phones.phones.controller.UserController;
import com.phones.phones.dto.UserLoginDto;
import com.phones.phones.exception.user.UserInvalidLoginException;
import com.phones.phones.model.User;
import com.phones.phones.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ValidationException;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class LoginController {

    private final UserController userController;
    private final SessionManager sessionManager;

    @Autowired
    public LoginController(final UserController userController,
                           final SessionManager sessionManager) {
        this.userController = userController;
        this.sessionManager = sessionManager;
    }


    @PostMapping("/login")
    public ResponseEntity login(@RequestBody final UserLoginDto userLoginDto) throws UserInvalidLoginException, ValidationException {
        ResponseEntity response;
        Optional<User> u = userController.login(userLoginDto.getUsername(), userLoginDto.getPassword());
        if (u.isEmpty()) {
            throw new UserInvalidLoginException();
        }
        String token = sessionManager.createSession(u.get());
        response = ResponseEntity.ok().headers(createHeaders(token)).build();
        return response;
    }

    @PostMapping("/logout")
    public ResponseEntity logout(@RequestHeader("Authorization") final String token) {
        sessionManager.removeSession(token);
        return ResponseEntity.ok().build();
    }

    private HttpHeaders createHeaders(String token) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization", token);
        return responseHeaders;
    }

}
