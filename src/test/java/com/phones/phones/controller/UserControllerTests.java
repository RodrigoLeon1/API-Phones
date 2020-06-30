package com.phones.phones.controller;

import com.phones.phones.TestFixture;
import com.phones.phones.dto.CityTopDto;
import com.phones.phones.dto.UserDto;
import com.phones.phones.exception.user.*;
import com.phones.phones.model.Call;
import com.phones.phones.model.Invoice;
import com.phones.phones.model.Line;
import com.phones.phones.model.User;
import com.phones.phones.service.*;
import com.phones.phones.session.SessionManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.validation.ValidationException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserControllerTests {
    UserController userController;

    @Mock
    UserService userService;
    @Mock
    CallService callService;
    @Mock
    LineService lineService;
    @Mock
    CityService cityService;
    @Mock
    InvoiceService invoiceService;
    @Mock
    SessionManager sessionManager;

    @Before
    public void setUp() {
        initMocks(this);
        userController = new UserController(userService, callService, lineService, cityService, invoiceService, sessionManager);
    }


    @Test
    public void createUserOk() throws UserSessionDoesNotExistException, UserAlreadyExistException, UsernameAlreadyExistException {
        User loggedUser = TestFixture.testUser();
        User newUser = TestFixture.testClientUser();

        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(userService.create(newUser)).thenReturn(newUser);

        User createdUser = userController.createUser("123", newUser);

        assertEquals(loggedUser.getName(), createdUser.getName());
        assertEquals(loggedUser.getDni(), createdUser.getDni());
    }



    /**
     *
     * findAllUsers
     *
     * */


    @Test
    public void findAllUsersOk() throws UserSessionDoesNotExistException {
        User loggedUser = TestFixture.testUser();
        List<User> users = TestFixture.testListOfUsers();
        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(userService.findAll()).thenReturn(users);

        List<User> returnedUsers = userController.findAllUsers("123");

        assertEquals(users.size(), returnedUsers.size());
        assertEquals(users.get(0).getDni(), returnedUsers.get(0).getDni());
    }

    /**
     *
     * findUserById
     *
     * */

    @Test
    public void findUserByIdOk() throws UserSessionDoesNotExistException, UserDoesNotExistException {
        User loggedUser = TestFixture.testUser();
        User user = TestFixture.testClientUser();

        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(userService.findById(1L)).thenReturn(user);

        User returnedUser = userController.findUserById("123", 1L);

        assertEquals(user.getId(), returnedUser.getId());
        assertEquals(user.getDni(), returnedUser.getDni());
        assertEquals(user.getSurname(), returnedUser.getSurname());
        assertEquals(1L, returnedUser.getId());
    }

    /**
     *
     * deleteUserById
     *
     * */

    @Test
    public void deleteUserByIdOk() throws UserSessionDoesNotExistException, UserDoesNotExistException, UserAlreadyDisableException {
        User loggedUser = TestFixture.testUser();

        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(userService.disableById(1L)).thenReturn(true);

        Boolean deleted = userController.deleteUserById("123", 1L);

        assertEquals(true, deleted);
    }

    /**
     *
     * updateUserById
     *
     * */


    @Test
    public void updateUserByIdOk() throws UserSessionDoesNotExistException, UserDoesNotExistException, UsernameAlreadyExistException {
        User loggedUser = TestFixture.testUser();
        User updatedUser = TestFixture.testClientUser();
        UserDto userUpdate = TestFixture.testUserDto();

        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(userService.updateById(1L, userUpdate)).thenReturn(updatedUser);

        User updated = userController.updateUserById("123", userUpdate, 1L);

        assertEquals(updatedUser.getName(), updated.getName());
        assertEquals(updatedUser.getDni(), updated.getDni());
    }


    /**
     *
     * findCallsByUserId
     *
     * */



    @Test
    public void findCallsByUserIdOk() throws UserSessionDoesNotExistException, UserDoesNotExistException {
        User loggedUser = TestFixture.testUser();
        List<Call> testCalls = TestFixture.testListOfCalls();

        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(callService.findByUserId(1L)).thenReturn(testCalls);

        List<Call> returnedCalls = userController.findCallsByUserId("123", 1L);

        assertEquals(testCalls.size(), returnedCalls.size());
        assertEquals(testCalls.get(0).getOriginNumber(), returnedCalls.get(0).getOriginNumber());
    }

    /**
     *
     * findCallsByUserSessionBetweenDates
     *
     * */


    @Test
    public void findCallsByUserSessionBetweenDatesOk() throws UserSessionDoesNotExistException, UserDoesNotExistException, ParseException {
        User loggedUser = TestFixture.testUser();
        List<Call> testCalls = TestFixture.testListOfCalls();
        Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse("05/01/2020");
        Date toDate = new SimpleDateFormat("dd/MM/yyyy").parse("19/06/2020");
        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(callService.findByUserIdBetweenDates(1L, fromDate, toDate)).thenReturn(testCalls);

        List<Call> returnedCalls = userController.findCallsByUserSessionBetweenDates("123", "05/01/2020", "19/06/2020");

        assertEquals(testCalls.size(), returnedCalls.size());
        assertEquals(testCalls.get(0).getOriginNumber(), returnedCalls.get(0).getOriginNumber());
    }

    @Test( expected = ValidationException.class)
    public void findCallsByUserSessionBetweenDatesNullParam() throws UserSessionDoesNotExistException, UserDoesNotExistException, ParseException {
        User loggedUser = TestFixture.testUser();
        Date fromDate = null;

        userController.findCallsByUserSessionBetweenDates("123", null, "19/06/2020");
    }


    @Test(expected =ValidationException.class)
    public void findCallsByUserSessionBetweenDatesInvalidParam() throws ValidationException, ParseException, UserSessionDoesNotExistException, UserDoesNotExistException {
        String from = null;
        String to = null;
        userController.findCallsByUserSessionBetweenDates("123", from, to);
    }


    /**
     *
     * findLinesByUserId
     *
     * */


    @Test
    public void findLinesByUserIdOk() throws UserSessionDoesNotExistException, UserDoesNotExistException {
        User loggedUser = TestFixture.testUser();
        List<Line> testLines = TestFixture.testListOfLines();

        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(lineService.findByUserId(1L)).thenReturn(testLines);

        List<Line> returnedCalls = userController.findLinesByUserId("123", 1L);

        assertEquals(testLines.size(), returnedCalls.size());
        assertEquals(testLines.get(0).getUser(), returnedCalls.get(0). getUser());
        assertEquals(testLines.get(0).getUser().getUsername(), returnedCalls.get(0). getUser().getUsername());
        assertEquals("rl", returnedCalls.get(0). getUser().getUsername());
        assertEquals("404040", returnedCalls.get(0). getUser().getDni());
    }

    /**
     *
     * findLinesByUserSession
     *
     * */


    @Test
    public void findLinesByUserSessionOk() throws UserSessionDoesNotExistException, UserDoesNotExistException {
        User loggedUser = TestFixture.testUser();
        List<Line> testLines = TestFixture.testListOfLines();

        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(lineService.findByUserId(loggedUser.getId())).thenReturn(testLines);

        List<Line> returnedLines = userController.findLinesByUserSession("123");

        assertEquals(testLines.size(), returnedLines.size());
        assertEquals(testLines.get(0).getUser(), returnedLines.get(0). getUser());
        assertEquals(testLines.get(0).getUser().getUsername(), returnedLines.get(0). getUser().getUsername());
        assertEquals("rl", returnedLines.get(0). getUser().getUsername());
        assertEquals("404040", returnedLines.get(0). getUser().getDni());
    }

    /**
     *
     * findInvoicesByUserSessionBetweenDates
     *
     * */


    @Test
    public void findInvoicesByUserSessionBetweenDatesOk() throws UserSessionDoesNotExistException, UserDoesNotExistException, ParseException {
        User loggedUser = TestFixture.testUser();
        List<Invoice> testInvoices = TestFixture.testListOfInvoices();
        Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse("05/01/2020");
        Date toDate = new SimpleDateFormat("dd/MM/yyyy").parse("19/06/2020");
        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(invoiceService.findByUserIdBetweenDates(loggedUser.getId(), fromDate, toDate)).thenReturn(testInvoices);

        List<Invoice> returnedInvoices = userController.findInvoicesByUserSessionBetweenDates("123", "05/01/2020", "19/06/2020");

        assertEquals(testInvoices.size(), returnedInvoices.size());
        assertEquals(testInvoices.get(0).getTotalPrice(), returnedInvoices.get(0).getTotalPrice());
    }


    @Test(expected =ValidationException.class)
    public void findInvoicesByUserSessionBetweenDatesNoInvalidParam() throws ValidationException, ParseException, UserSessionDoesNotExistException, UserDoesNotExistException {
        String from = null;
        String to = null;
        userController.findInvoicesByUserSessionBetweenDates("123", from, to);
    }


    /**
     *
     * findInvoicesByUserIdBetweenDates
     *
     * */


    @Test
    public void findInvoicesByUserIdBetweenDatesBetweenDatesOk() throws UserSessionDoesNotExistException, UserDoesNotExistException, ParseException {
        User loggedUser = TestFixture.testUser();
        List<Invoice> testInvoices = TestFixture.testListOfInvoices();
        Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse("05/01/2020");
        Date toDate = new SimpleDateFormat("dd/MM/yyyy").parse("19/06/2020");
        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(invoiceService.findByUserIdBetweenDates(1L, fromDate, toDate)).thenReturn(testInvoices);

        List<Invoice> returnedInvoices = userController.findInvoicesByUserIdBetweenDates("123", 1L,"05/01/2020", "19/06/2020");

        assertEquals(testInvoices.size(), returnedInvoices.size());
        assertEquals(testInvoices.get(0).getTotalPrice(), returnedInvoices.get(0).getTotalPrice());
    }



    @Test(expected =ValidationException.class)
    public void findInvoicesByUserIdBetweenDatesInvalidParam() throws ValidationException, ParseException, UserSessionDoesNotExistException, UserDoesNotExistException {
        String from = null;
        String to = null;
        userController.findInvoicesByUserIdBetweenDates("123", 1L,from, to);
    }


    /**
     *
     * findTopCitiesCallsByUserSession
     *
     * */


    @Test
    public void findTopCitiesCallsByUserSessionOk() throws UserSessionDoesNotExistException, UserDoesNotExistException {
        User loggedUser = TestFixture.testUser();
        List<CityTopDto> testCities = TestFixture.testListOfCityTop();

        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(cityService.findTopCitiesCallsByUserId(loggedUser.getId())).thenReturn(testCities);

        List<CityTopDto> returnedCities = userController.findTopCitiesCallsByUserSession("123");

        assertEquals(testCities.size(), returnedCities.size());
        assertEquals(testCities.get(0).getName(), returnedCities.get(0).getName());
        assertEquals(testCities.get(0).getQuantity(), returnedCities.get(0).getQuantity());
        assertEquals("Capital Federal", returnedCities.get(0).getName());
        assertEquals(15, returnedCities.get(1).getQuantity());
    }



/*    public ResponseEntity<List<CityTopDto>> findTopCitiesCallsByUserSession(@RequestHeader("Authorization") final String sessionToken) throws UserDoesNotExistException, UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        List<CityTopDto> citiesTops = cityService.findTopCitiesCallsByUserId(currentUser.getId());
        return (citiesTops.size() > 0) ? ResponseEntity.ok(citiesTops) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }*/



/*    @Test
    public void findTopCitiesCallsByUserSessionNoCallsFound() throws UserSessionDoesNotExistException, UserDoesNotExistException {
        User loggedUser = TestFixture.testClientUser();
        List<Line> emptyLines = new ArrayList<>();

        ResponseEntity response = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(lineService.findByUserId(loggedUser.getId())).thenReturn(emptyLines);

        ResponseEntity<List<Line>> returnedLines = userController.findLinesByUserSession("123");

        assertEquals(response.getStatusCode(), returnedLines.getStatusCode());
    }*/



    /**
     *
     * login
     *
     * */

    @Test
    public void testLoginOk() throws UserInvalidLoginException {
        User loginUser = TestFixture.testUser();
        String username = "rl";
        String password = "123";

        when(userService.login(username, password)).thenReturn(Optional.ofNullable(loginUser));

        Optional<User> loggedUser = userController.login(username, password);

        assertTrue(loggedUser.isPresent());
        assertEquals(loginUser.getUsername(), loggedUser.get().getUsername());
        assertEquals(loginUser.getDni(), loggedUser.get().getDni());
    }


    @Test (expected = ValidationException.class)
    public void testLoginValidationException() throws UserInvalidLoginException, ValidationException {
        User loginUser = TestFixture.testUser();
        String username = "rl";
        String password = null;

        userController.login(username, password);
    }

    @Test (expected = ValidationException.class)
    public void testLoginOneNullParam() throws UserInvalidLoginException, ValidationException {
        User loginUser = TestFixture.testUser();
        String username = null;
        String password = null;

        userController.login(username, password);
    }

}


