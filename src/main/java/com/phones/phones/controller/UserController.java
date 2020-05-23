package com.phones.phones.controller;

import com.phones.phones.dto.UserDto;
import com.phones.phones.exception.user.UserAlreadyExistException;
import com.phones.phones.exception.user.UserNotExistException;
import com.phones.phones.exception.user.UsernameAlreadyExistException;
import com.phones.phones.model.Call;
import com.phones.phones.model.Invoice;
import com.phones.phones.model.Line;
import com.phones.phones.model.User;
import com.phones.phones.service.UserService;
import com.phones.phones.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final CallController callController;
    private final LineController lineController;
    private final SessionManager sessionManager;

    @Autowired
    public UserController(UserService userService,
                          CallController callController,
                          LineController lineController,
                          SessionManager sessionManager) {
        this.userService = userService;
        this.callController = callController;
        this.lineController = lineController;
        this.sessionManager = sessionManager;
    }


    @PostMapping("/")
    public ResponseEntity addUser(@RequestBody @Valid final User user) throws UsernameAlreadyExistException, UserAlreadyExistException {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.add(user));
    }

    @GetMapping("/")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAll();
        return (users.size() > 0) ? ResponseEntity.ok(users) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}")
    public Optional<UserDto> getUserById(@PathVariable final Long id) throws UserNotExistException {
        return userService.getById(id);
    }

    @DeleteMapping("/{id}")
    public int deleteUserById(@PathVariable final Long id) throws UserNotExistException {
        return userService.disableById(id);
    }

    @PutMapping("/{id}")
    public User updateUserById(@RequestBody User user,
                               @PathVariable final Long id) throws UserNotExistException, UsernameAlreadyExistException {
        return userService.updateById(id, user);
    }

    @GetMapping("/{id}/calls")
    public List<Call> getCallsByUserId(@PathVariable final Long id) throws UserNotExistException {
        return callController.getCallsByUserId(id);
    }

    @GetMapping("/me/calls")
    public ResponseEntity<List<Call>> getCallsByUserSession(@RequestHeader("Authorization") String sessionToken,
                                                            @RequestParam(name = "from") String from,
                                                            @RequestParam(name = "to") String to)
                                                            throws UserNotExistException, ParseException {
        if (from == null || to == null) {
            throw new ValidationException("Date from and date to must have a value");
        }

        User currentUser = getCurrentUser(sessionToken);
        Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(from);
        Date toDate = new SimpleDateFormat("dd/MM/yyyy").parse(to);
        List<Call> calls = callController.getCallsByUserIdBetweenDates(currentUser.getId(), fromDate, toDate);
        return (calls.size() > 0) ? ResponseEntity.ok(calls) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}/lines")
    public List<Line> getLinesByUserId(@PathVariable final Long id) {
        return lineController.getLinesByUserId(id);
    }

    @GetMapping("/me/lines")
    public List<Line> getLinesByUserSession() {
        return null;
    }

    // between dates
    @GetMapping("/me/invoices")
    public List<Invoice> getInvoicesByUserSession() {
        return null;
    }

    /*
    @GetMapping("/me/destination/top")
    Usar una projection para este metodo (limit 10)
        {
            "ciudad": x,
            "cantidad de veces": x
        }
     */

    public Optional<User> login(String username, String password) throws UserNotExistException, ValidationException {
        if ((username != null) && (password != null)) {
            return userService.login(username, password);
        } else {
            throw new ValidationException("Username and password must have a value");
        }
    }

    private User getCurrentUser(String sessionToken) throws UserNotExistException {
        return Optional.ofNullable(sessionManager.getCurrentUser(sessionToken)).orElseThrow(() -> new UserNotExistException(""));
    }


}
