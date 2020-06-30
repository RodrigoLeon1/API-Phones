package com.phones.phones.controller.web;

import com.phones.phones.TestFixture;
import com.phones.phones.controller.LineController;
import com.phones.phones.controller.RateController;
import com.phones.phones.controller.UserController;
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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@PrepareForTest(RestUtils.class)
@RunWith(PowerMockRunner.class)
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
        PowerMockito.mockStatic(RestUtils.class);
        backOfficeController = new BackOfficeController(userController, lineController, rateController);
    }

/**
     *
     * USER ENDPOINTS
     *
*/


    @Test
    public void createUserOk() throws UserSessionDoesNotExistException, UserAlreadyExistException, UsernameAlreadyExistException {
        User user = TestFixture.testUser();
        User newUser = TestFixture.testUser();
        when(userController.createUser("123", user)).thenReturn(newUser);
        when(RestUtils.getLocation(newUser.getId())).thenReturn(URI.create("miUri.com"));
        ResponseEntity<User> response = backOfficeController.createUser("123", user);

        assertEquals(URI.create("miUri.com"), response.getHeaders().getLocation());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
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
        List<User> usersList = TestFixture.testListOfUsers();
        when(userController.findAllUsers("123")).thenReturn(usersList);


        ResponseEntity<List<User>> response = backOfficeController.findAllUsers("123");

        assertEquals(usersList.size(), response.getBody().size());
        assertEquals(usersList.get(0).getDni(), response.getBody().get(0).getDni());

    }


    @Test
    public void findAllUsersNoUsersFound() throws UserSessionDoesNotExistException {
        User loggedUser = TestFixture.testUser();
        List<User> noUsers = new ArrayList<>();
        ResponseEntity response = ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        when(userController.findAllUsers("123")).thenReturn(noUsers);

        ResponseEntity<List<User>> returnedUsers = backOfficeController.findAllUsers("123");

        assertEquals(response.getStatusCode(), returnedUsers.getStatusCode());
    }



    @Test (expected = UserSessionDoesNotExistException.class)
    public void findAllUsersUserSessionDoesNotExistException() throws UserSessionDoesNotExistException {
        when(userController.findAllUsers("123")).thenThrow(new UserSessionDoesNotExistException());
        backOfficeController.findAllUsers("123");
    }



    @Test
    public void findUserByIdOk() throws UserSessionDoesNotExistException, UserDoesNotExistException {
        User user = TestFixture.testUser();
        when(userController.findUserById("123", 1L)).thenReturn(user);
        ResponseEntity<User> returnedUser = backOfficeController.findUserById("123", 1L);

        assertEquals(user.getId(), returnedUser.getBody().getId());
        assertEquals(user.getDni(), returnedUser.getBody().getDni());
        assertEquals(user.getSurname(), returnedUser.getBody().getSurname());
        assertEquals(1L, returnedUser.getBody().getId());
    }




    @Test
    public void deleteUserByIdOk() throws UserSessionDoesNotExistException, UserDoesNotExistException, UserAlreadyDisableException {

        when(userController.deleteUserById("123", 1L)).thenReturn(true);
        ResponseEntity deleted = backOfficeController.deleteUserById("123", 1L);

        assertEquals(ResponseEntity.ok().build(), deleted);
    }

    @Test
    public void deleteUserByIdBadRequest() throws UserSessionDoesNotExistException, UserDoesNotExistException, UserAlreadyDisableException {

        when(userController.deleteUserById("123", 1L)).thenReturn(false);

        ResponseEntity deleted = backOfficeController.deleteUserById("123", 1L);

        assertEquals(HttpStatus.BAD_REQUEST, deleted.getStatusCode());
    }





    @Test
    public void updateUserByIdOk() throws UserSessionDoesNotExistException, UserDoesNotExistException, UserAlreadyDisableException, UsernameAlreadyExistException {
        User updatedUser = TestFixture.testClientUser();
        UserDto userUpdate = TestFixture.testUserDto();
        when(userController.updateUserById("123", userUpdate, 1L)).thenReturn(updatedUser);
        ResponseEntity updated = backOfficeController.updateUserById("123", TestFixture.testUserDto(),1L);

        assertEquals(ResponseEntity.ok().build(), updated);
    }


/**
     *
     * LINE ENDPOINTS
     *
*/

    @Test
    public void createLineOk() throws LineNumberAlreadyExistException, UserSessionDoesNotExistException {
        Line newLine = TestFixture.testLine("2235472861");

        when(lineController.createLine("123", newLine)).thenReturn(newLine);
        when(RestUtils.getLocation(newLine.getId())).thenReturn(URI.create("miUri.com"));
        ResponseEntity response = backOfficeController.createLine("123", newLine);

        assertEquals(URI.create("miUri.com"), response.getHeaders().getLocation());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

    }


    @Test
    public void findAllLinesOk() throws UserSessionDoesNotExistException {
        List<Line> linesList = TestFixture.testListOfLines();
        when(lineController.findAllLines("123")).thenReturn(linesList);
        ResponseEntity<List<Line>> returnedLists = backOfficeController.findAllLines("123");

        assertEquals(linesList.size(), returnedLists.getBody().size());
        assertEquals(linesList.get(0).getId(), returnedLists.getBody().get(0).getId());
        assertEquals(linesList.get(0).getStatus(), returnedLists.getBody().get(0).getStatus());
    }


    @Test
    public void findAllLinesNoLinesFound() throws UserSessionDoesNotExistException {

        List<Line> emptyList = new ArrayList<>();
        ResponseEntity response = ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        when(lineController.findAllLines("123")).thenReturn(emptyList);

        ResponseEntity returnedLines = backOfficeController.findAllLines("123");

        assertEquals(response.getStatusCode(), returnedLines.getStatusCode());
    }




    @Test
    public void findLineByIdOk() throws UserSessionDoesNotExistException, LineDoesNotExistException {
        Line line = TestFixture.testLine("2235472861");
        when(lineController.findLineById("123", 1L)).thenReturn(line);
        ResponseEntity<Line> returnedLine = backOfficeController.findLineById("123", 1L);

        assertEquals(line.getId(), returnedLine.getBody().getId());
        assertEquals(line.getStatus(), returnedLine.getBody().getStatus());
        assertEquals(line.getId(), returnedLine.getBody().getId());
        assertEquals(1L, returnedLine.getBody().getId());

    }



    @Test
    public void deleteLineByIdOk() throws UserSessionDoesNotExistException, LineAlreadyDisabledException, LineDoesNotExistException {

        when(lineController.deleteLineById("123", 1L)).thenReturn(1);
        ResponseEntity deleted = backOfficeController.deleteLineById("123", 1L);

        assertEquals(ResponseEntity.ok().build(), deleted);
    }


    @Test
    public void deleteLineByIdBadRequest() throws UserSessionDoesNotExistException, LineAlreadyDisabledException, LineDoesNotExistException {

        when(lineController.deleteLineById("123", 1L)).thenReturn(0);

        ResponseEntity deleted = backOfficeController.deleteLineById("123", 1L);

        assertEquals(HttpStatus.BAD_REQUEST, deleted.getStatusCode());
    }



    @Test
    public void updateLineByIdOk() throws UserSessionDoesNotExistException, LineDoesNotExistException {

        when(lineController.updateLineByIdLine("123", TestFixture.testLineDto(), 1L)).thenReturn(true);
        ResponseEntity updated = backOfficeController.updateLineByIdLine("123", TestFixture.testLineDto(),1L);

        assertEquals(ResponseEntity.ok().build(), updated);
    }




/**
     *
     * RATE ENDPOINTS
     *
*/

    @Test
    public void findAllRatesOk() throws UserSessionDoesNotExistException {
        List<RateDto> listOfRates = TestFixture.testListOfRatesDto();
        when(rateController.findAllRates("123")).thenReturn(listOfRates);
        ResponseEntity<List<RateDto>> returnedRates = backOfficeController.findAllRates("123");

        assertEquals(listOfRates.size(), returnedRates.getBody().size());
        assertEquals(listOfRates.get(0).getDestinationCity(), returnedRates.getBody().get(0).getDestinationCity());
        assertEquals(listOfRates.get(1).getDestinationCity(), returnedRates.getBody().get(1).getDestinationCity());
        assertEquals(listOfRates.get(1).getPriceMinute(), returnedRates.getBody().get(1).getPriceMinute());
    }


    @Test
    public void findAllRatesNoRatesFound() throws UserSessionDoesNotExistException {
        List<RateDto> emptyList = new ArrayList<>();
        ResponseEntity response = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        when(rateController.findAllRates("123")).thenReturn(emptyList);

        ResponseEntity<List<RateDto>> returnedRates = backOfficeController.findAllRates("123");

        assertEquals(response.getStatusCode(), returnedRates.getStatusCode());
    }





/**
     *
     * CALL ENDPOINTS
     *
*/

    @Test
    public void findCallsByUserIdOk() throws UserSessionDoesNotExistException, UserDoesNotExistException {
        List<Call> testCalls = TestFixture.testListOfCalls();
        when(userController.findCallsByUserId("123", 1L)).thenReturn(testCalls);
        ResponseEntity<List<Call>> returnedCalls = backOfficeController.findCallsByUserId("123", 1L);

        assertEquals(testCalls.size(), returnedCalls.getBody().size());
        assertEquals(testCalls.get(0).getOriginNumber(), returnedCalls.getBody().get(0).getOriginNumber());
    }

    @Test
    public void findCallsByUserIdNoCallsFound() throws UserSessionDoesNotExistException, UserDoesNotExistException {
        List<Call> emptyCalls = new ArrayList<>();

        when(userController.findCallsByUserId("123", 1L)).thenReturn(emptyCalls);
        ResponseEntity response = ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        ResponseEntity<List<Call>> returnedCalls = backOfficeController.findCallsByUserId("123", 1L);

        assertEquals(response.getStatusCode(), returnedCalls.getStatusCode());
    }


/**
     *
     * INVOICE ENDPOINT
     *
*/
    @Test
    public void findInvoicesByUserIdBetweenDatesOk() throws UserSessionDoesNotExistException, UserDoesNotExistException, ParseException {
        List<Invoice> testInvoices = TestFixture.testListOfInvoices();
        when(userController.findInvoicesByUserIdBetweenDates("123",1L, "05/01/2020", "19/06/2020")).thenReturn(testInvoices);
        ResponseEntity<List<Invoice>> returnedInvoices = backOfficeController.findInvoicesByUserIdBetweenDates("123",1L, "05/01/2020", "19/06/2020");

        assertEquals(testInvoices.size(), returnedInvoices.getBody().size());
        assertEquals(testInvoices.get(0).getTotalPrice(), returnedInvoices.getBody().get(0).getTotalPrice());
    }

    @Test
    public void findInvoicesByUserIdBetweenDatesNoInvoicesFound() throws UserSessionDoesNotExistException, UserDoesNotExistException, ParseException {

        List<Invoice> emptyInvoiceList = new ArrayList<>();

        ResponseEntity response = ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        when(userController.findInvoicesByUserIdBetweenDates("123",1L, "05/01/2020", "19/06/2020")).thenReturn(emptyInvoiceList);

        ResponseEntity<List<Invoice>> returnedInvoices = backOfficeController.findInvoicesByUserIdBetweenDates("123",1L, "05/01/2020", "19/06/2020");

        assertEquals(response.getStatusCode(), returnedInvoices.getStatusCode());
    }


}
