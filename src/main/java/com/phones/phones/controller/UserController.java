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
import org.springframework.beans.factory.annotation.Autowired;
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


    public User createUser(@RequestHeader("Authorization") final String sessionToken,
                           @RequestBody @Valid final User user) throws UsernameAlreadyExistException, UserAlreadyExistException, UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        return userService.create(user);
    }

    public List<User> findAllUsers(@RequestHeader("Authorization") final String sessionToken) throws UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        return userService.findAll();
    }

    public User findUserById(@RequestHeader("Authorization") final String sessionToken,
                                             @PathVariable final Long id) throws UserDoesNotExistException, UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        return userService.findById(id);
    }

    public boolean deleteUserById(@RequestHeader("Authorization") final String sessionToken,
                                  @PathVariable final Long id) throws UserDoesNotExistException, UserAlreadyDisableException, UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        return userService.disableById(id);
    }

    public User updateUserById(@RequestHeader("Authorization") final String sessionToken,
                               @RequestBody @Valid final UserDto updatedUser,
                               @PathVariable final Long id) throws UserDoesNotExistException, UsernameAlreadyExistException, UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        return userService.updateById(id, updatedUser);
    }

    public List<Call> findCallsByUserId(@RequestHeader("Authorization") String sessionToken,
                                        @PathVariable final Long id) throws UserDoesNotExistException, UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        return callService.findByUserId(id);
    }

    public List<Call> findCallsByUserSessionBetweenDates(@RequestHeader("Authorization") final String sessionToken,
                                                         @RequestParam(name = "from") final String from,
                                                         @RequestParam(name = "to") final String to) throws ParseException, UserDoesNotExistException, UserSessionDoesNotExistException {
        if (from == null || to == null) {
            throw new ValidationException("Date 'from' and date 'to' must have a value");
        }
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(from);
        Date toDate = new SimpleDateFormat("dd/MM/yyyy").parse(to);
        return callService.findByUserIdBetweenDates(currentUser.getId(), fromDate, toDate);
    }

    public List<Line> findLinesByUserId(@RequestHeader("Authorization") final String sessionToken,
                                        @PathVariable final Long id) throws UserDoesNotExistException, UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        return lineService.findByUserId(id);
    }

    public List<Line> findLinesByUserSession(@RequestHeader("Authorization") final String sessionToken) throws UserDoesNotExistException, UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        return lineService.findByUserId(currentUser.getId());
    }

    public List<Invoice> findInvoicesByUserSessionBetweenDates(@RequestHeader("Authorization") final String sessionToken,
                                                               @RequestParam(name = "from") final String from,
                                                               @RequestParam(name = "to") final String to) throws ParseException, UserDoesNotExistException, UserSessionDoesNotExistException {
        if (from == null || to == null) {
            throw new ValidationException("Date 'from' and date 'to' must have a value");
        }
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        return findInvoicesById(currentUser.getId(), from, to);
    }

    public List<Invoice> findInvoicesByUserIdBetweenDates(@RequestHeader("Authorization") final String sessionToken,
                                                          @PathVariable final Long id,
                                                          @RequestParam(name = "from") final String from,
                                                          @RequestParam(name = "to") final String to) throws ParseException, UserDoesNotExistException, UserSessionDoesNotExistException {
        if (from == null || to == null) {
            throw new ValidationException("Date 'from' and date 'to' must have a value");
        }
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        return findInvoicesById(id, from, to);
    }

    private List<Invoice> findInvoicesById(final Long id, String from, String to) throws UserDoesNotExistException, ParseException {
        Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(from);
        Date toDate = new SimpleDateFormat("dd/MM/yyyy").parse(to);
        return invoiceService.findByUserIdBetweenDates(id, fromDate, toDate);
    }

    public List<CityTopDto> findTopCitiesCallsByUserSession(@RequestHeader("Authorization") final String sessionToken) throws UserDoesNotExistException, UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        return cityService.findTopCitiesCallsByUserId(currentUser.getId());
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
