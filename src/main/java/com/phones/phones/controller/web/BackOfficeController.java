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


    //return (invoices.size() > 0) ? ResponseEntity.ok(invoices) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    //return ResponseEntity.created(RestUtils.getLocation(newLine.getId())).build();
    //return ResponseEntity.ok(line);
    //return ResponseEntity.ok().build();
    //return (deleted > 0) ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    //return deleted ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    /* CRUD Clients */
    @PostMapping("/users/")
    public ResponseEntity createUser(@RequestHeader("Authorization") final String sessionToken,
                                     @RequestBody @Valid final User user) throws UsernameAlreadyExistException, UserAlreadyExistException, UserSessionDoesNotExistException {
        return userController.createUser(sessionToken, user);
    }

    @GetMapping("/users/")
    public ResponseEntity<List<User>> findAllUsers(@RequestHeader("Authorization") final String sessionToken) throws UserSessionDoesNotExistException {
        return userController.findAllUsers(sessionToken);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> findUserById(@RequestHeader("Authorization") final String sessionToken,
                                             @PathVariable final Long id) throws UserDoesNotExistException, UserSessionDoesNotExistException {
        return userController.findUserById(sessionToken, id);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity deleteUserById(@RequestHeader("Authorization") final String sessionToken,
                                         @PathVariable final Long id) throws UserDoesNotExistException, UserAlreadyDisableException, UserSessionDoesNotExistException {
        return userController.deleteUserById(sessionToken, id);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity updateUserById(@RequestHeader("Authorization") final String sessionToken,
                                         @RequestBody @Valid final UserDto updatedUser,
                                         @PathVariable final Long id) throws UserDoesNotExistException, UsernameAlreadyExistException, UserSessionDoesNotExistException {
        return userController.updateUserById(sessionToken, updatedUser, id);
    }


    /* CRUD Lines */
    @PostMapping("/lines/")
    public ResponseEntity createLine(@RequestHeader("Authorization") String sessionToken,
                                     @RequestBody @Valid final Line line) throws LineNumberAlreadyExistException, UserSessionDoesNotExistException {
        return lineController.createLine(sessionToken, line);
    }

    @GetMapping("/lines/")
    public ResponseEntity<List<Line>> findAllLines(@RequestHeader("Authorization") String sessionToken) throws UserSessionDoesNotExistException {
        return lineController.findAllLines(sessionToken);
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<Line> findLineById(@RequestHeader("Authorization") String sessionToken,
                                             @PathVariable final Long id) throws LineDoesNotExistException, UserSessionDoesNotExistException {
        return lineController.findLineById(sessionToken, id);
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity deleteLineById(@RequestHeader("Authorization") final String sessionToken,
                                         @PathVariable final Long id) throws LineDoesNotExistException, UserSessionDoesNotExistException, LineAlreadyDisabledException {
        return lineController.deleteLineById(sessionToken, id);
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity updateLineByIdLine(@RequestHeader("Authorization") final String sessionToken,
                                             @RequestBody @Valid final LineDto updatedLine,
                                             @PathVariable final Long id) throws LineDoesNotExistException, UserSessionDoesNotExistException {
        return lineController.updateLineByIdLine(sessionToken, updatedLine, id);
    }


    /* Rates */
    @GetMapping("/rates")
    public ResponseEntity<List<RateDto>> findAllRates(@RequestHeader("Authorization") final String sessionToken) throws UserSessionDoesNotExistException {
        return rateController.findAllRates(sessionToken);
    }


    /* Clients calls */
    @GetMapping("/users/{id}/calls")
    public ResponseEntity<List<Call>> findCallsByUserId(@RequestHeader("Authorization") String sessionToken,
                                                        @PathVariable final Long id) throws UserDoesNotExistException, UserSessionDoesNotExistException {
        return userController.findCallsByUserId(sessionToken, id);
    }


    /* Clients invoices */
    @GetMapping("/users/{id}/invoices")
    public ResponseEntity<List<Invoice>> findInvoicesByUserIdBetweenDates(@RequestHeader("Authorization") final String sessionToken,
                                                                          @PathVariable final Long id,
                                                                          @RequestParam(name = "from") final String from,
                                                                          @RequestParam(name = "to") final String to) throws ParseException, UserDoesNotExistException, UserSessionDoesNotExistException {
        return userController.findInvoicesByUserIdBetweenDates(sessionToken, id, from, to);
    }

}
