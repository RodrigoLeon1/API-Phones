package com.phones.phones.controller;

import com.phones.phones.exception.user.*;
import com.phones.phones.model.*;
import com.phones.phones.projection.CityTop;
import com.phones.phones.service.*;
import com.phones.phones.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
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


    // Arreglar - agrega el usuario pero no sus roles
    @PostMapping("/")
    public ResponseEntity createUser(@RequestHeader("Authorization") final String sessionToken,
                                     @RequestBody @Valid final User user) throws UsernameAlreadyExistException, UserAlreadyExistException, UserSessionNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        if (currentUser.hasRoleEmployee()) {
            User newUser = userService.create(user);
            return ResponseEntity.created(getLocation(newUser)).build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> findAllUsers(@RequestHeader("Authorization") final String sessionToken) throws UserSessionNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        if (currentUser.hasRoleEmployee()) {
            List<User> users = userService.findAll();
            return (users.size() > 0) ? ResponseEntity.ok(users) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findUserById(@RequestHeader("Authorization") final String sessionToken,
                                                @PathVariable final Long id) throws UserNotExistException, UserSessionNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        if (currentUser.hasRoleEmployee()) {
            return ResponseEntity.ok(userService.findById(id));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    // TESTEAR
    @DeleteMapping("/{id}")
    public ResponseEntity deleteUserById(@RequestHeader("Authorization") final String sessionToken,
                                         @PathVariable final Long id) throws UserNotExistException, UserAlreadyDisableException, UserSessionNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        if (currentUser.hasRoleEmployee()) {
            int deleted = userService.disableById(id);
            return (deleted > 0) ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    // TESTEAR
    @PutMapping("/{id}")
    public ResponseEntity updateUserById(@RequestHeader("Authorization") final String sessionToken,
                                         @RequestBody @Valid final User user,
                                         @PathVariable final Long id) throws UserNotExistException, UsernameAlreadyExistException, UserSessionNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        if (currentUser.hasRoleEmployee()) {
            User updatedUser = userService.updateById(id, user);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    // Trae todas las llamadas... arreglar
    @GetMapping("/{id}/calls")
    public ResponseEntity<List<Call>> findCallsByUserId(@RequestHeader("Authorization") String sessionToken,
                                                        @PathVariable final Long id) throws UserNotExistException, UserSessionNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        if (currentUser.hasRoleEmployee()) {
            List<Call> calls = callService.findByUserId(id);
            return (calls.size() > 0) ? ResponseEntity.ok(calls) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/me/calls")
    public ResponseEntity<List<Call>> findCallsByUserSessionBetweenDates(@RequestHeader("Authorization") final String sessionToken,
                                                                         @RequestParam(name = "from") final String from,
                                                                         @RequestParam(name = "to") final String to) throws ParseException, UserNotExistException, UserSessionNotExistException {
        if (from == null || to == null) {
            throw new ValidationException("Date 'from' and date 'to' must have a value");
        }
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(from);
        Date toDate = new SimpleDateFormat("dd/MM/yyyy").parse(to);
        List<Call> calls = callService.findByUserIdBetweenDates(currentUser.getId(), fromDate, toDate);
        return (calls.size() > 0) ? ResponseEntity.ok(calls) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}/lines")
    public ResponseEntity<List<Line>> findLinesByUserId(@RequestHeader("Authorization") final String sessionToken,
                                                        @PathVariable final Long id) throws UserNotExistException, UserSessionNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        if (currentUser.hasRoleEmployee()) {
            List<Line> lines = lineService.findByUserId(id);
            return (lines.size() > 0) ? ResponseEntity.ok(lines) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/me/lines")
    public ResponseEntity<List<Line>> findLinesByUserSession(@RequestHeader("Authorization") final String sessionToken) throws UserNotExistException, UserSessionNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        List<Line> lines = lineService.findByUserId(currentUser.getId());
        return (lines.size() > 0) ? ResponseEntity.ok(lines) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/me/invoices")
    public ResponseEntity<List<Invoice>> findInvoicesByUserSessionBetweenDates(@RequestHeader("Authorization") final String sessionToken,
                                                                               @RequestParam(name = "from") final String from,
                                                                               @RequestParam(name = "to") final String to) throws ParseException, UserNotExistException, UserSessionNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(from);
        Date toDate = new SimpleDateFormat("dd/MM/yyyy").parse(to);
        List<Invoice> invoices = invoiceService.findByUserIdBetweenDates(currentUser.getId(), fromDate, toDate);
        return (invoices.size() > 0) ? ResponseEntity.ok(invoices) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/me/cities/top")
    public ResponseEntity<List<CityTop>> findTopCitiesCallsByUserSession(@RequestHeader("Authorization") final String sessionToken) throws UserNotExistException, UserSessionNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        List<CityTop> citiesTops = cityService.findTopCitiesCallsByUserId(currentUser.getId());
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

    /*
        @GetMapping("/{id}/maxCalled")
        public ResponseEntity<MostCalledDto> findMostCalledById(@PathVariable final Long id) throws UserNotExistException {
            Optional<UserDto currentUser = userService.findById(id);
            MostCalledDto prueba = new MostCalledDto();
            if (currentUser != null) {
                String mostCalled = callService.findMostCalledByOriginId(currentUser.get().getId());

                prueba.setCallerName(currentUser.get().getName());
                prueba.setCallerSurname(currentUser.get().getSurname());
                prueba.setMostCalled(mostCalled);
            }
            return (prueba != null) ? ResponseEntity.ok(prueba) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
     */

    private URI getLocation(User user) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();
    }

}
