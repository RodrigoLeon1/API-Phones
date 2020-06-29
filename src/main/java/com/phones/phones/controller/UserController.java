package com.phones.phones.controller;

import com.phones.phones.dto.CityTopDto;
import com.phones.phones.dto.UserDto;
import com.phones.phones.exception.user.*;
import com.phones.phones.model.Call;
import com.phones.phones.model.Invoice;
import com.phones.phones.model.Line;
import com.phones.phones.model.User;
import com.phones.phones.service.*;
import com.phones.phones.session.SessionManager;
import com.phones.phones.utils.RestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;
    private final CallService callService;
    private final LineService lineService;
    private final CityService cityService;
    private final InvoiceService invoiceService;
    private final SessionManager sessionManager;

    @Autowired
    public UserController(final UserService userService,
                          final CallService callService,
                          final LineService lineService,
                          final CityService cityService,
                          final InvoiceService invoiceService,
                          final SessionManager sessionManager) {
        this.userService = userService;
        this.callService = callService;
        this.lineService = lineService;
        this.cityService = cityService;
        this.invoiceService = invoiceService;
        this.sessionManager = sessionManager;
    }


    public ResponseEntity createUser(@RequestHeader("Authorization") final String sessionToken,
                                     @RequestBody @Valid final User user) throws UsernameAlreadyExistException, UserAlreadyExistException, UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        User newUser = userService.create(user);
        return ResponseEntity.created(RestUtils.getLocation(newUser.getId())).build();
    }

    public ResponseEntity<List<User>> findAllUsers(@RequestHeader("Authorization") final String sessionToken) throws UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        List<User> users = userService.findAll();
        return (users.size() > 0) ? ResponseEntity.ok(users) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    public ResponseEntity<User> findUserById(@RequestHeader("Authorization") final String sessionToken,
                                             @PathVariable final Long id) throws UserDoesNotExistException, UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        User user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    public ResponseEntity deleteUserById(@RequestHeader("Authorization") final String sessionToken,
                                         @PathVariable final Long id) throws UserDoesNotExistException, UserAlreadyDisableException, UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        boolean deleted = userService.disableById(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    public ResponseEntity updateUserById(@RequestHeader("Authorization") final String sessionToken,
                                         @RequestBody @Valid final UserDto updatedUser,
                                         @PathVariable final Long id) throws UserDoesNotExistException, UsernameAlreadyExistException, UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        User updated = userService.updateById(id, updatedUser);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<List<Call>> findCallsByUserId(@RequestHeader("Authorization") String sessionToken,
                                                        @PathVariable final Long id) throws UserDoesNotExistException, UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        List<Call> calls = callService.findByUserId(id);
        return (calls.size() > 0) ? ResponseEntity.ok(calls) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    public ResponseEntity<List<Call>> findCallsByUserSessionBetweenDates(@RequestHeader("Authorization") final String sessionToken,
                                                                         @RequestParam(name = "from") final String from,
                                                                         @RequestParam(name = "to") final String to) throws ParseException, UserDoesNotExistException, UserSessionDoesNotExistException {
        if (from == null || to == null) {
            throw new ValidationException("Date 'from' and date 'to' must have a value");
        }
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(from);
        Date toDate = new SimpleDateFormat("dd/MM/yyyy").parse(to);
        List<Call> calls = callService.findByUserIdBetweenDates(currentUser.getId(), fromDate, toDate);
        return (calls.size() > 0) ? ResponseEntity.ok(calls) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    public ResponseEntity<List<Line>> findLinesByUserId(@RequestHeader("Authorization") final String sessionToken,
                                                        @PathVariable final Long id) throws UserDoesNotExistException, UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        List<Line> lines = lineService.findByUserId(id);
        return (lines.size() > 0) ? ResponseEntity.ok(lines) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    public ResponseEntity<List<Line>> findLinesByUserSession(@RequestHeader("Authorization") final String sessionToken) throws UserDoesNotExistException, UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        List<Line> lines = lineService.findByUserId(currentUser.getId());
        return (lines.size() > 0) ? ResponseEntity.ok(lines) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    public ResponseEntity<List<Invoice>> findInvoicesByUserSessionBetweenDates(@RequestHeader("Authorization") final String sessionToken,
                                                                               @RequestParam(name = "from") final String from,
                                                                               @RequestParam(name = "to") final String to) throws ParseException, UserDoesNotExistException, UserSessionDoesNotExistException {
        if (from == null || to == null) {
            throw new ValidationException("Date 'from' and date 'to' must have a value");
        }
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        List<Invoice> invoices = findInvoicesById(currentUser.getId(), from, to);
        return (invoices.size() > 0) ? ResponseEntity.ok(invoices) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    public ResponseEntity<List<Invoice>> findInvoicesByUserIdBetweenDates(@RequestHeader("Authorization") final String sessionToken,
                                                                          @PathVariable final Long id,
                                                                          @RequestParam(name = "from") final String from,
                                                                          @RequestParam(name = "to") final String to) throws ParseException, UserDoesNotExistException, UserSessionDoesNotExistException {
        if (from == null || to == null) {
            throw new ValidationException("Date 'from' and date 'to' must have a value");
        }
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        List<Invoice> invoices = findInvoicesById(id, from, to);
        return (invoices.size() > 0) ? ResponseEntity.ok(invoices) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    public List<Invoice> findInvoicesById(final Long id, String from, String to) throws UserDoesNotExistException, ParseException {
        Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(from);
        Date toDate = new SimpleDateFormat("dd/MM/yyyy").parse(to);
        return invoiceService.findByUserIdBetweenDates(id, fromDate, toDate);
    }

    public ResponseEntity<List<CityTopDto>> findTopCitiesCallsByUserSession(@RequestHeader("Authorization") final String sessionToken) throws UserDoesNotExistException, UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        List<CityTopDto> citiesTops = cityService.findTopCitiesCallsByUserId(currentUser.getId());
        return (citiesTops.size() > 0) ? ResponseEntity.ok(citiesTops) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    public Optional<User> login(final String username,
                                final String password) throws ValidationException, UserInvalidLoginException {
        if ((username != null) && (password != null)) {
            return userService.login(username, password);
        } else {
            throw new ValidationException("Username and password must have a value");
        }
    }

}
