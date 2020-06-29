package com.phones.phones.controller.web;

import com.phones.phones.controller.LineController;
import com.phones.phones.controller.RateController;
import com.phones.phones.controller.UserController;
import com.phones.phones.dto.LineDto;
import com.phones.phones.dto.RateDto;
import com.phones.phones.dto.UserDto;
import com.phones.phones.exception.line.LineAlreadyDisabledException;
import com.phones.phones.exception.line.LineDoesNotExistException;
import com.phones.phones.exception.line.LineNumberAlreadyExistException;
import com.phones.phones.exception.user.*;
import com.phones.phones.model.Call;
import com.phones.phones.model.Invoice;
import com.phones.phones.model.Line;
import com.phones.phones.model.User;
import com.phones.phones.utils.RestUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/backoffice")
public class BackOfficeController {

    private final UserController userController;
    private final LineController lineController;
    private final RateController rateController;

    public BackOfficeController(final UserController userController,
                                final LineController lineController,
                                final RateController rateController) {
        this.userController = userController;
        this.lineController = lineController;
        this.rateController = rateController;
    }


    /* CRUD Clients */
    @PostMapping("/users/")
    public ResponseEntity createUser(@RequestHeader("Authorization") final String sessionToken,
                                     @RequestBody @Valid final User user) throws UsernameAlreadyExistException, UserAlreadyExistException, UserSessionDoesNotExistException {
        User newUser = userController.createUser(sessionToken, user);
        return ResponseEntity.created(RestUtils.getLocation(newUser.getId())).build();
    }

    @GetMapping("/users/")
    public ResponseEntity<List<User>> findAllUsers(@RequestHeader("Authorization") final String sessionToken) throws UserSessionDoesNotExistException {
        List<User> users = userController.findAllUsers(sessionToken);
        return (users.size() > 0) ? ResponseEntity.ok(users) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> findUserById(@RequestHeader("Authorization") final String sessionToken,
                                             @PathVariable final Long id) throws UserDoesNotExistException, UserSessionDoesNotExistException {
        User user = userController.findUserById(sessionToken, id);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity deleteUserById(@RequestHeader("Authorization") final String sessionToken,
                                         @PathVariable final Long id) throws UserDoesNotExistException, UserAlreadyDisableException, UserSessionDoesNotExistException {
        boolean deleted = userController.deleteUserById(sessionToken, id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PutMapping("/users/{id}")
    public ResponseEntity updateUserById(@RequestHeader("Authorization") final String sessionToken,
                                         @RequestBody @Valid final UserDto updatedUser,
                                         @PathVariable final Long id) throws UserDoesNotExistException, UsernameAlreadyExistException, UserSessionDoesNotExistException {
        User user = userController.updateUserById(sessionToken, updatedUser, id);
        return ResponseEntity.ok().build();
    }


    /* CRUD Lines */
    @PostMapping("/lines/")
    public ResponseEntity createLine(@RequestHeader("Authorization") String sessionToken,
                                     @RequestBody @Valid final Line line) throws LineNumberAlreadyExistException, UserSessionDoesNotExistException {
        Line newLine = lineController.createLine(sessionToken, line);
        return ResponseEntity.created(RestUtils.getLocation(newLine.getId())).build();
    }

    @GetMapping("/lines/")
    public ResponseEntity<List<Line>> findAllLines(@RequestHeader("Authorization") String sessionToken) throws UserSessionDoesNotExistException {
        List<Line> lines = lineController.findAllLines(sessionToken);
        return (lines.size() > 0) ? ResponseEntity.ok(lines) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<Line> findLineById(@RequestHeader("Authorization") String sessionToken,
                                             @PathVariable final Long id) throws LineDoesNotExistException, UserSessionDoesNotExistException {
        Line line = lineController.findLineById(sessionToken, id);
        return ResponseEntity.ok(line);
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity deleteLineById(@RequestHeader("Authorization") final String sessionToken,
                                         @PathVariable final Long id) throws LineDoesNotExistException, UserSessionDoesNotExistException, LineAlreadyDisabledException {
        int deleted = lineController.deleteLineById(sessionToken, id);
        return (deleted > 0) ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity updateLineByIdLine(@RequestHeader("Authorization") final String sessionToken,
                                             @RequestBody @Valid final LineDto updatedLine,
                                             @PathVariable final Long id) throws LineDoesNotExistException, UserSessionDoesNotExistException {
        boolean updated = lineController.updateLineByIdLine(sessionToken, updatedLine, id);
        return ResponseEntity.ok().build();
    }


    /* Rates */
    @GetMapping("/rates")
    public ResponseEntity<List<RateDto>> findAllRates(@RequestHeader("Authorization") final String sessionToken) throws UserSessionDoesNotExistException {
        List<RateDto> rates = rateController.findAllRates(sessionToken);
        return (rates.size() > 0) ? ResponseEntity.ok(rates) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    /* Clients calls */
    @GetMapping("/users/{id}/calls")
    public ResponseEntity<List<Call>> findCallsByUserId(@RequestHeader("Authorization") String sessionToken,
                                                        @PathVariable final Long id) throws UserDoesNotExistException, UserSessionDoesNotExistException {
        List<Call> calls = userController.findCallsByUserId(sessionToken, id);
        return (calls.size() > 0) ? ResponseEntity.ok(calls) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    /* Clients invoices */
    @GetMapping("/users/{id}/invoices")
    public ResponseEntity<List<Invoice>> findInvoicesByUserIdBetweenDates(@RequestHeader("Authorization") final String sessionToken,
                                                                          @PathVariable final Long id,
                                                                          @RequestParam(name = "from") final String from,
                                                                          @RequestParam(name = "to") final String to) throws ParseException, UserDoesNotExistException, UserSessionDoesNotExistException {
        List<Invoice> invoices = userController.findInvoicesByUserIdBetweenDates(sessionToken, id, from, to);
        return (invoices.size() > 0) ? ResponseEntity.ok(invoices) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
