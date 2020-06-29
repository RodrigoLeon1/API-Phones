package com.phones.phones.controller.web;

import com.phones.phones.TestFixture;
import com.phones.phones.controller.LineController;
import com.phones.phones.controller.RateController;
import com.phones.phones.controller.UserController;
import com.phones.phones.dto.RateDto;
import com.phones.phones.exception.line.LineAlreadyDisabledException;
import com.phones.phones.exception.line.LineDoesNotExistException;
import com.phones.phones.exception.line.LineNumberAlreadyExistException;
import com.phones.phones.exception.user.*;
import com.phones.phones.model.Call;
import com.phones.phones.model.Invoice;
import com.phones.phones.model.Line;
import com.phones.phones.model.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BackOfficeControllerTest {

    BackOfficeController backOfficeController;

    @Mock
    UserController userController;
    @Mock
    LineController lineController;
    @Mock
    RateController rateController;

    @Before
    public void setUp() {
        initMocks(this);
        backOfficeController = new BackOfficeController(userController, lineController, rateController);
    }


    /**
     *
     * USER ENDPOINTS
     * */


    @Test
    public void createUserOk() throws UserSessionDoesNotExistException, UserAlreadyExistException, UsernameAlreadyExistException {
        User user = TestFixture.testUser();
        ResponseEntity<User> response = ResponseEntity.ok(TestFixture.testUser());
        when(userController.createUser("123", user)).thenReturn(response);
        ResponseEntity<User> returnedUser = backOfficeController.createUser("123", user);

        assertEquals(response.getBody().getName(), returnedUser.getBody().getName());
        assertEquals(response.getBody().getDni(), returnedUser.getBody().getDni());

    }

    @Test (expected = UserSessionDoesNotExistException.class)
    public void createUserUserSessionDoesNotExistException() throws UserSessionDoesNotExistException, UserAlreadyExistException, UsernameAlreadyExistException {
        User user = TestFixture.testUser();
        when(userController.createUser("123",user)).thenThrow(new UserSessionDoesNotExistException());
        backOfficeController.createUser("123", user);
    }

    @Test (expected = UsernameAlreadyExistException.class)
    public void createUserUsernameAlreadyExistException() throws UserSessionDoesNotExistException, UserAlreadyExistException, UsernameAlreadyExistException {
        User user = TestFixture.testUser();
        when(userController.createUser("123",user)).thenThrow(new UsernameAlreadyExistException());
        backOfficeController.createUser("123", user);
    }


    @Test (expected = UserAlreadyExistException.class)
    public void createUserUserAlreadyExistException() throws UserSessionDoesNotExistException, UserAlreadyExistException, UsernameAlreadyExistException {
        User user = TestFixture.testUser();
        when(userController.createUser("123",user)).thenThrow(new UserAlreadyExistException());
        backOfficeController.createUser("123", user);
    }

    @Test
    public void findAllUsersOk() throws UserSessionDoesNotExistException {
        ResponseEntity<List<User>> usersList = ResponseEntity.ok(TestFixture.testListOfUsers());
        when(userController.findAllUsers("123")).thenReturn(usersList);
        ResponseEntity<List<User>> returnedUsers = backOfficeController.findAllUsers("123");

        assertEquals(usersList.getBody().size(), returnedUsers.getBody().size());
        assertEquals(usersList.getBody().get(0).getDni(), returnedUsers.getBody().get(0).getDni());

    }

    @Test (expected = UserSessionDoesNotExistException.class)
    public void findAllUsersUserSessionDoesNotExistException() throws UserSessionDoesNotExistException {
        when(userController.findAllUsers("123")).thenThrow(new UserSessionDoesNotExistException());
        backOfficeController.findAllUsers("123");
    }


    @Test
    public void findUserByIdOk() throws UserSessionDoesNotExistException, UserDoesNotExistException {
        ResponseEntity<User> user = ResponseEntity.ok(TestFixture.testUser());
        when(userController.findUserById("123", 1L)).thenReturn(user);
        ResponseEntity<User> returnedUsers = backOfficeController.findUserById("123", 1L);

        assertEquals(user.getBody().getId(), returnedUsers.getBody().getId());
        assertEquals(user.getBody().getDni(), returnedUsers.getBody().getDni());
        assertEquals(user.getBody().getSurname(), returnedUsers.getBody().getSurname());
        assertEquals(1L, returnedUsers.getBody().getId());
    }

    @Test
    public void deleteUserByIdOk() throws UserSessionDoesNotExistException, UserDoesNotExistException, UserAlreadyDisableException {

        when(userController.deleteUserById("123", 1L)).thenReturn((ResponseEntity.ok().build()));
        ResponseEntity deleted = backOfficeController.deleteUserById("123", 1L);

        assertEquals(ResponseEntity.ok().build(), deleted);
    }

    @Test
    public void updateUserById() throws UserSessionDoesNotExistException, UserDoesNotExistException, UserAlreadyDisableException, UsernameAlreadyExistException {

        when(userController.updateUserById("123", TestFixture.testUserDto(), 1L)).thenReturn((ResponseEntity.ok().build()));
        ResponseEntity updated = backOfficeController.updateUserById("123", TestFixture.testUserDto(),1L);

        assertEquals(ResponseEntity.ok().build(), updated);
    }




    /**
     *
     * LINE ENDPOINTS
     * */

    @Test
    public void createLineOk() throws LineNumberAlreadyExistException, UserSessionDoesNotExistException {
        Line line = TestFixture.testLine("2235472861");
        ResponseEntity<Line> response = ResponseEntity.ok(TestFixture.testLine("2235472861"));
        when(lineController.createLine("123", line)).thenReturn(response);
        ResponseEntity<Line> returnedLine = backOfficeController.createLine("123", line);

        assertEquals(response.getBody().getCreationDate(), returnedLine.getBody().getCreationDate());
        assertEquals(response.getBody().getNumber(), returnedLine.getBody().getNumber());

    }


    @Test
    public void findAllLinesOk() throws UserSessionDoesNotExistException {
        ResponseEntity<List<Line>> linesList = ResponseEntity.ok(TestFixture.testListOfLines());
        when(lineController.findAllLines("123")).thenReturn(linesList);
        ResponseEntity<List<Line>> returnedLists = backOfficeController.findAllLines("123");

        assertEquals(linesList.getBody().size(), returnedLists.getBody().size());
        assertEquals(linesList.getBody().get(0).getId(), returnedLists.getBody().get(0).getId());
        assertEquals(linesList.getBody().get(0).getStatus(), returnedLists.getBody().get(0).getStatus());
    }


    @Test
    public void findLineByIdOk() throws UserSessionDoesNotExistException, UserDoesNotExistException, LineDoesNotExistException {
        ResponseEntity<Line> line = ResponseEntity.ok(TestFixture.testLine("2235472861"));
        when(lineController.findLineById("123", 1L)).thenReturn(line);
        ResponseEntity<Line> returnedLine = backOfficeController.findLineById("123", 1L);

        assertEquals(line.getBody().getId(), returnedLine.getBody().getId());
        assertEquals(line.getBody().getStatus(), returnedLine.getBody().getStatus());
        assertEquals(1L, returnedLine.getBody().getId());

    }


    @Test
    public void deleteLineByIdOk() throws UserSessionDoesNotExistException, LineAlreadyDisabledException, LineDoesNotExistException {

        when(lineController.deleteLineById("123", 1L)).thenReturn((ResponseEntity.ok().build()));
        ResponseEntity deleted = backOfficeController.deleteLineById("123", 1L);

        assertEquals(ResponseEntity.ok().build(), deleted);
    }

    @Test
    public void updateLineByIdOk() throws UserSessionDoesNotExistException, LineDoesNotExistException {

        when(lineController.updateLineByIdLine("123", TestFixture.testLineDto(), 1L)).thenReturn((ResponseEntity.ok().build()));
        ResponseEntity updated = backOfficeController.updateLineByIdLine("123", TestFixture.testLineDto(),1L);

        assertEquals(ResponseEntity.ok().build(), updated);
    }


    /**
     *
     * RATE ENDPOINTS
     * */

    @Test
    public void findAllRatesOk() throws UserSessionDoesNotExistException {
        ResponseEntity<List<RateDto>> ratesList = ResponseEntity.ok(TestFixture.testListOfRatesDto());
        when(rateController.findAllRates("123")).thenReturn(ratesList);
        ResponseEntity<List<RateDto>> returnedRates = backOfficeController.findAllRates("123");

        assertEquals(ratesList.getBody().size(), returnedRates.getBody().size());
        assertEquals(ratesList.getBody().get(0).getId(), returnedRates.getBody().get(0).getId());
        assertEquals(ratesList.getBody().get(0).getPriceMinute(), returnedRates.getBody().get(0).getPriceMinute());
    }


    /**
     *
     * CALL ENDPOINTS
     * */

    @Test
    public void findCallsByUserIdOk() throws UserSessionDoesNotExistException, UserDoesNotExistException {
        ResponseEntity<List<Call>> call = ResponseEntity.ok(TestFixture.testListOfCalls());
        when(userController.findCallsByUserId("123", 1L)).thenReturn(call);
        ResponseEntity<List<Call>> returnedCalls = backOfficeController.findCallsByUserId("123", 1L);

        assertEquals(call.getBody().get(0).getId(), returnedCalls.getBody().get(0).getId());
        assertEquals(call.getBody().get(0).getDuration(), returnedCalls.getBody().get(0).getDuration());
        assertEquals(1L, returnedCalls.getBody().get(0).getId());
    }

    @Test
    public void findCallsByUserIdNoCallsFound() throws UserSessionDoesNotExistException, UserDoesNotExistException {
        ResponseEntity<List<Call>> call = ResponseEntity.ok(TestFixture.testListOfCalls());
        when(userController.findCallsByUserId("123", 1L)).thenReturn(call);
        ResponseEntity<List<Call>> returnedCalls = backOfficeController.findCallsByUserId("123", 1L);

        assertEquals(call.getBody().get(0).getId(), returnedCalls.getBody().get(0).getId());
        assertEquals(call.getBody().get(0).getDuration(), returnedCalls.getBody().get(0).getDuration());
        assertEquals(1L, returnedCalls.getBody().get(0).getId());
    }


    /**
     *
     * INVOICE ENDPOINT
     * */
    @Test
    public void findInvoicesByUserIdBetweenDatesOk() throws UserSessionDoesNotExistException, UserDoesNotExistException, ParseException {
        ResponseEntity<List<Invoice>> invoices = ResponseEntity.ok(TestFixture.testListOfInvoices());
        when(userController.findInvoicesByUserIdBetweenDates("123",1L, "05/01/2020", "19/06/2020")).thenReturn(invoices);
        ResponseEntity<List<Invoice>> returnedInvoices = backOfficeController.findInvoicesByUserIdBetweenDates("123",1L, "05/01/2020", "19/06/2020");

        assertEquals(invoices.getBody().get(0).getId(), returnedInvoices.getBody().get(0).getId());
        assertEquals(invoices.getBody().get(0).getNumberCalls(), returnedInvoices.getBody().get(0).getNumberCalls());
        assertEquals(1L, returnedInvoices.getBody().get(0).getId());
    }
}
